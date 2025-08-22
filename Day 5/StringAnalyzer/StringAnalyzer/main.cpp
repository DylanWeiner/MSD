//
//  main.cpp
//  StringAnalyzer
//
//  Created by Dylan Weiner on 8/22/25.
//

#include <iostream>
#include <string>

bool IsPunctuation(char c) {
    char exc = c;
    bool punc;
    if(exc == '!' || exc == '?' || exc == '.') {
        punc = true;
    }
    else {
        punc = false;
    }
    return punc;
}

bool IsVowel(char c) {
    char vowel = c;
    bool v;
    if(vowel == 'a' || vowel == 'e' || vowel == 'i' || vowel == 'o' || vowel == 'u' || vowel == 'y' || vowel == 'A' || vowel == 'E' || vowel == 'I' || vowel == 'O' || vowel == 'U' || vowel == 'Y') {
        v = true;
    }
    else {
        v = false;
    }
    return v;
}

bool IsConsonants(char c) {
    bool cons;
    if(!IsVowel(c) && !IsPunctuation(c) && c != ' ') {
        cons = true;
    }
    else {
        cons = false;
    }
    return cons;
}



int NumSentences(std::string s) {
    int per = 0;
    for(int i = 0; i < s.length()-1; i++) {
        //std::cout << s[i];
        if(IsPunctuation(s[i])) {
            per += 1;
        }
    }
    return per;
}

int NumVowels(std::string s) {
    int vow = 0;
    for(int i = 0; i < s.length()-1; i++) {
        //std::cout << s[i];
        if(IsVowel(s[i])) {
            vow += 1;
        }
    }
    return vow;
}

int NumConsonants(std::string s) {
    int con = 0;
    for(int i = 0; i < s.length()-1; i++) {
        //std::cout << s[i];
        if(IsConsonants(s[i])) {
            con += 1;
        }
    }
    return con;
}

int NumWords(std::string s) {
    int space = 0;
    auto pos = s.find(" "); // int and string are different, since npos is defined in string, auto will accept both.
    std::string spaceless = s;
    while(pos != std::string::npos) {
        spaceless.replace(pos, 1, "|");
        space += 1;
        pos = spaceless.find(" ");
    }
    return space;
}



double AverageWordLength(std::string s) {
    double avg = (NumVowels(s)+NumConsonants(s))/NumWords(s);
    return avg;
}

double AverageVowelsPerWord(std::string s) {
    double avgvow = NumVowels(s)/NumWords(s);
    return avgvow;
}
 


int main(int argc, const char * argv[]) {
    std::string phrase;
    
    std::cout << "Please input a phrase to be analyzed" << std::endl;
    
    std::getline(std::cin, phrase);
    
    char punctuation = (phrase.back()); // Takes last letter.
    //std::cout << punctuation;
    if(punctuation != '.' &&
       punctuation != '?' &&
       punctuation != '!') {
        std::cout << "This is an invalid sentence; please punctuate.\n";
        return 1;
    }
    
    else{
        std::cout << "Word count is: " << NumWords(phrase) + 1 << std::endl;
        std::cout << "Sentence count is: " << NumSentences(phrase) + 1 << std::endl;
        std::cout << "Vowel count is: " << NumVowels(phrase) << std::endl;
        std::cout << "Consonant count is: " << NumConsonants(phrase) << std::endl;
        std::cout << "Average Word length is: " << AverageWordLength(phrase) << std::endl;
        std::cout << "Average Vowels per word is: " << AverageVowelsPerWord(phrase) << std::endl;
    }
    return 0;
}
