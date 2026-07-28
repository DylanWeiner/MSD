import {useState} from "react";

function Trivia() {

const[question, setQuestion] = useState("");
const[answer, setAnswer] = useState("");
const[showAnswer, setShowAnswer] = useState(false);

async function handleGetTrivia() {
	setShowAnswer(false);

	const response = await fetch("https://opentdb.com/api.php?amount=1")
	console.log("response:", response);
	if(!response.ok) {
		console.log("Response Error: ", response);
		throw new Error("Need to wait 5 seconds");
	}

	let data = await response.json();
	let result = data.results[0]
	console.log("result is:", result);

	for(let field in result) {
	console.log("field:", field);
	}

	setQuestion(result.question);
	setQuestion(result.correct_answer);
	}

	function displayAnswer() {
		setShowAnswer(true);
}

return (
	<div>
		<button onClick={handleGetTrivia}>Get Trivia Question</button>
		<p>{question}</p>
		{ (question !== "") && <button onClick={displayAnswer}>Show Answer</button>}
		<p>showAnswer === true && {answer}</p>
	</div>);
};

export default Trivia;