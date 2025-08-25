//
//  wordNum.h
//  StringAnalyzer
//
//  Created by Dylan Weiner on 8/25/25.
//

#include "letterhelpers.h"

int NumWords(std::string s) {
    int space = 1;
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

int NumSentences(std::string s) {
    int per = 0;
    for(int i = 0; i < s.length()-1; i++) {
        //std::cout << s[i];
        if(IsPunctuation(s[i])) {
            per += 1;
        }
    }
    per += 1
    return per;
}
