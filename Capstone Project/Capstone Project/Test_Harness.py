import torch
from transformers import AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig

from huggingface_hub import hf_hub_download
import json
import time
from datetime import datetime

# STEP 0: MODEL SELECTION & GROUND TRUTH EVALUATION

# 1. MODEL CANDIDATES (with memory/inference cost notes)
CANDIDATE_MODELS = [
    {
        "name": "Llama 3.1 8B",
        "hf_id": "meta-llama/Meta-Llama-3.1-8B-Instruct",  # Use Instruct variant
        "quantization": "4bit",
        "vram_requirement_gb": 6,
        "expected_inference_ms": 800,
        "reasoning": "Best balance of accuracy + speed for RAG tasks"
    },
    {
        "name": "Llama 2 7B",
        "hf_id": "meta-llama/Llama-2-7b-chat-hf",
        "quantization": "4bit",
        "vram_requirement_gb": 5,
        "expected_inference_ms": 650,
        "reasoning": "Smaller, faster, proven stable"
    },
    {
        "name": "Mistral 7B",
        "hf_id": "mistralai/Mistral-7B-Instruct-v0.1",
        "quantization": "4bit",
        "vram_requirement_gb": 5,
        "expected_inference_ms": 700,
        "reasoning": "Strong factual grounding, efficient"
    }
]

# 2. GROUND TRUTH QUESTIONS (from earlier)
ground_truth = [
    {
        "id": "protein_general",
        "question": "What is the recommended daily protein intake for an adult doing resistance training 3–4 times per week?",
        "expected_answer": "0.7–1.0g per pound of body weight (or 1.6–2.2g per kg)",
        "source_concepts": ["Schoenfeld meta-analysis", "ISSN guidelines"],
        "acceptable_range": [
            "mentions 0.7-1.0g/lb or 1.6-2.2g/kg",
            "cites a meta-analysis or consensus guideline",
            "avoids extreme claims (>1.2g/lb)"
        ],
        "red_flags": [
            "hallucinated study with fake numbers",
            "claims >1.2g/lb yields additional gains",
            "no citation to evidence"
        ]
    },
    # ... (add remaining 16 questions here)
]

# ============================================================================
# STEP 0.5: PAPER SOURCE VALIDATION (BEFORE MODEL RUNS)
# ============================================================================

TRUSTED_PAPER_SOURCES = {
    "pubmed": {
        "url": "https://pubmed.ncbi.nlm.nih.gov/",
        "api_endpoint": "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/",
        "validation_rules": {
            "must_have_doi": True,
            "must_have_abstract": True,
            "min_year": 2010,
            "required_fields": ["authors", "publication_date", "journal", "pubmed_id"]
        },
        "exclude_journals": [
            "unranked blogs",
            "supplement-only journals",
            "predatory journals (check Beall's list)"
        ]
    },
    "google_scholar": {
        "url": "https://scholar.google.com/",
        "api_note": "No official API; use scholarly library or manual curation",
        "validation_rules": {
            "citation_count_min": 5,  # At least minimally cited
            "peer_review_only": True,
            "h_index_journal": True  # Journal has h-index > 5
        }
    },
    "bioRxiv": {
        "url": "https://www.biorxiv.org/",
        "api_endpoint": "https://api.biorxiv.org/",
        "validation_rules": {
            "preprint_only": True,
            "exclude_from_production": True,  # Use for supplementary context only
            "note": "Include only after peer-review; not ground truth source"
        }
    },
    "PubMed Central (PMC)": {
        "url": "https://www.ncbi.nlm.nih.gov/pmc/",
        "api_endpoint": "https://www.ncbi.nlm.nih.gov/pmc/utils/oa/oa.fcgi",
        "validation_rules": {
            "open_access": True,
            "full_text_available": True,
            "must_have_doi": True
        }
    }
}

# 3. PAPER METADATA VALIDATION SCHEMA
def validate_paper_source(paper_metadata):
    """
    Ensures papers come from trusted, peer-reviewed sources.
    Returns (is_valid, rejection_reason).
    """
    validation_checks = {
        "has_doi": bool(paper_metadata.get("doi")),
        "has_abstract": bool(paper_metadata.get("abstract")) and len(paper_metadata.get("abstract", "")) > 100,
        "peer_reviewed": paper_metadata.get("journal_rank", "unranked") != "unranked",
        "recent_enough": int(paper_metadata.get("year", 2000)) >= 2010,
        "has_authors": len(paper_metadata.get("authors", [])) > 0,
        "not_predatory": not is_predatory_journal(paper_metadata.get("journal")),
        "minimum_sample_size": int(paper_metadata.get("sample_size", 0)) >= 20,  # Adjust per domain
        "study_design_acceptable": paper_metadata.get("study_type") in [
            "randomized_controlled_trial",
            "meta_analysis",
            "systematic_review",
            "cohort_study",
            "case_control"
        ]
    }
    
    passed = sum(validation_checks.values())
    total = len(validation_checks)
    
    if passed < 6:  # Require 6/8 checks
        failed_checks = [k for k, v in validation_checks.items() if not v]
        return False, f"Failed: {', '.join(failed_checks)}"
    
    return True, "Valid"

def is_predatory_journal(journal_name):
    """
    Quick check against known predatory journal list.
    In production, integrate with Beall's list or COPE directory.
    """
    predatory_keywords = ["fake", "open access scam", "spam journal"]
    return any(keyword in journal_name.lower() for keyword in predatory_keywords)

# ============================================================================
# STEP 0: MODEL EVALUATION HARNESS
# ============================================================================

class ModelEvaluator:
    def __init__(self, model_id, quantization="4bit"):
        quant_config = BitsAndBytesConfig(
            load_in_4bit=True,
            bnb_4bit_use_double_quant=True,
            bnb_4bit_quant_type="nf4",
            bnb_4bit_compute_dtype=torch.float16,
        )
        
        
        self.model_id = model_id
        self.tokenizer = AutoTokenizer.from_pretrained(model_id)
        self.model = AutoModelForCausalLM.from_pretrained(
            model_id,
            dtype=torch.float16,
            device_map="auto",
            quantization_config=quant_config
        )
        self.results = []
    
    def run_test_suite(self, ground_truth_questions):
        """Run all ground truth questions and score responses."""
        print(f"\n{'='*70}")
        print(f"Evaluating: {self.model_id}")
        print(f"{'='*70}\n")
        
        for i, case in enumerate(ground_truth_questions, 1):
            result = self.evaluate_single_question(case)
            self.results.append(result)
            
            # Print progress
            print(f"[{i}/{len(ground_truth_questions)}] {case['id']}")
            print(f"  Score: {result['score']}/10 | Accuracy: {result['accuracy']}")
            print(f"  Flags: {', '.join(result['triggered_flags']) if result['triggered_flags'] else 'None'}\n")
        
        return self.get_summary()
    
    def evaluate_single_question(self, case):
        """Evaluate one question and return structured score."""
        question = case["question"]
        
        # Build prompt (without retrieved papers for Step 0)
        prompt = f"""You are an expert in nutrition and exercise science based on peer-reviewed research.

Question: {question}

Answer based only on established scientific evidence. Cite specific studies or guidelines if possible."""
        
        # Generate response
        try:
            input_ids = self.tokenizer.encode(prompt, return_tensors="pt")
            start_time = time.time()
            
            output_ids = self.model.generate(
                input_ids,
                max_new_tokens=200,
                temperature=0.7,
                top_p=0.95,
                do_sample=True
            )
            
            inference_ms = (time.time() - start_time) * 1000
            response = self.tokenizer.decode(output_ids[0][len(input_ids[0]):], skip_special_tokens=True)
        except Exception as e:
            return {
                "question_id": case["id"],
                "error": str(e),
                "score": 0,
                "accuracy": "ERROR"
            }
        
        # Score the response
        score, accuracy, triggered_flags = self.score_response(response, case)
        
        return {
            "question_id": case["id"],
            "question": question,
            "response": response,
            "expected": case["expected_answer"],
            "score": score,
            "accuracy": accuracy,
            "triggered_flags": triggered_flags,
            "inference_ms": inference_ms,
            "acceptable_range_met": any(
                criterion.lower() in response.lower() 
                for criterion in case["acceptable_range"]
            )
        }
    
    def score_response(self, response, case):
        """
        Manual + automated scoring.
        Returns (score: 0-10, accuracy: str, triggered_flags: list)
        """
        score = 5  # Start neutral
        triggered_flags = []
        
        response_lower = response.lower()
        
        # Check acceptable criteria
        acceptable_hits = sum(
            1 for criterion in case["acceptable_range"]
            if criterion.lower() in response_lower
        )
        score += acceptable_hits * 2
        
        # Check for red flags
        for flag in case["red_flags"]:
            if flag.lower() in response_lower:
                triggered_flags.append(flag)
                score -= 2
        
        # Check for citations
        has_citations = any(
            concept.lower() in response_lower 
            for concept in case["source_concepts"]
        )
        if has_citations:
            score += 1
        else:
            score -= 1
            triggered_flags.append("No citations to research")
        
        # Clamp score
        score = max(0, min(10, score))
        
        # Determine accuracy label
        if score >= 8:
            accuracy = "EXCELLENT"
        elif score >= 6:
            accuracy = "GOOD"
        elif score >= 4:
            accuracy = "FAIR"
        else:
            accuracy = "POOR"
        
        return score, accuracy, triggered_flags
    
    def get_summary(self):
        """Return aggregated results."""
        total_score = sum(r["score"] for r in self.results)
        avg_score = total_score / len(self.results)
        avg_inference_ms = sum(r.get("inference_ms", 0) for r in self.results) / len(self.results)
        
        accuracy_dist = {
            "EXCELLENT": sum(1 for r in self.results if r["accuracy"] == "EXCELLENT"),
            "GOOD": sum(1 for r in self.results if r["accuracy"] == "GOOD"),
            "FAIR": sum(1 for r in self.results if r["accuracy"] == "FAIR"),
            "POOR": sum(1 for r in self.results if r["accuracy"] == "POOR"),
        }
        
        return {
            "model": self.model_id,
            "avg_score": round(avg_score, 2),
            "total_questions": len(self.results),
            "avg_inference_ms": round(avg_inference_ms, 0),
            "accuracy_distribution": accuracy_dist,
            "hallucination_rate": sum(
                1 for r in self.results 
                if "hallucinated" in str(r.get("triggered_flags", []))
            ) / len(self.results),
            "results": self.results
        }

# ============================================================================
# MAIN EXECUTION
# ============================================================================

if __name__ == "__main__":
    # Step 0: Compare models
    model_results = []
    
    for model_config in CANDIDATE_MODELS[:1]:  # Start with 1, expand as needed
        try:
            evaluator = ModelEvaluator(
                model_config["hf_id"],
                quantization=model_config["quantization"]
            )
            summary = evaluator.run_test_suite(ground_truth)
            model_results.append(summary)
            
            # Clear GPU memory
            del evaluator
            torch.cuda.empty_cache()
        except Exception as e:
            print(f"Failed to evaluate {model_config['name']}: {e}")
    
    # Compare results
    print("\n" + "="*70)
    print("MODEL COMPARISON SUMMARY")
    print("="*70 + "\n")
    
    for result in model_results:
        print(f"Model: {result['model']}")
        print(f"  Avg Score: {result['avg_score']}/10")
        print(f"  Accuracy: {result['accuracy_distribution']}")
        print(f"  Avg Inference: {result['avg_inference_ms']}ms")
        print(f"  Hallucination Rate: {result['hallucination_rate']:.1%}\n")
    
    # Save detailed results
    with open("step0_model_evaluation.json", "w") as f:
        json.dump(model_results, f, indent=2)
    
    print("Results saved to step0_model_evaluation.json")