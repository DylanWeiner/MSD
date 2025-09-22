//
//  letterhelpers.cpp
//  StringAnalyzer
//
//  Created by Dylan Weiner on 8/25/25.
//

bool IsPunctuation(char c) {
    if(c == '!' || c == '?' || c == '.') {
        return true;
    }
    else {
        return false;
    }
}

bool IsVowel(char c) {
    if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y' || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y') {
        return true;
    }
    else {
        return false;
    }
}

bool IsConsonants(char c) {

    if(!IsVowel(c) && !IsPunctuation(c) && c != ' ') {
        return true;
    }
    else {
        return false;
    }
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

