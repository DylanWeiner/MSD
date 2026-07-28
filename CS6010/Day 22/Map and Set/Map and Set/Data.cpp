//
//  Data.cpp
//  Map and Set
//
//  Created by Dylan Weiner on 9/17/25.
//

#include "Data.h"

void getUniqueWords(string s) {
    ifstream in(s);
    string word;
    set<string> uniqueWord;
    
    while(in >> word) {
        uniqueWord.insert(word);
    }
    in.close();
    cout << "There are " << uniqueWord.size() << " unique words!" << endl;
}

void getWordsCount(string s) {
    ifstream in(s);
    string word;
    map<string, int> wordOccur;
    
    while(in >> word) {
            if(wordOccur.count(word)) {
                wordOccur[word]++;
            }
            else {
                wordOccur[word] = 1;
            }
            cout << "Word: " << word << " shows up " << wordOccur[word] << " times." << endl;
        }
    in.close();
}
