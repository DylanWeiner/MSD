//
//  main.cpp
//  StringAnalyzer
//
//  Created by Dylan Weiner on 8/22/25.
//

#include <iostream>
#include <string>
#include "letterhelpers.h"
#include "wordfunc.h"


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
        std::cout << "Word count is: " << NumWords(phrase) << std::endl;
        std::cout << "Sentence count is: " << NumSentences(phrase) << std::endl;
        std::cout << "Vowel count is: " << NumVowels(phrase) << std::endl;
        std::cout << "Consonant count is: " << NumConsonants(phrase) << std::endl;
        std::cout << "Average Word length is: " << AverageWordLength(phrase) << std::endl;
        std::cout << "Average Vowels per word is: " << AverageVowelsPerWord(phrase) << std::endl;
    }
    return 0;
}
