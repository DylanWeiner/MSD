//
//  Data.cpp
//  BookAnalyzer
//
//  Created by Dylan Weiner on 8/29/25.
//

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include "Data.h"

void getTitle(string s) {
    ifstream in(s);
    vector<string> saveLine;
    string line;
    
    while(getline(in, line)) {
        saveLine.push_back(line);
        //        cout << line << endl;
    }
    cout << saveLine[10] << endl;
    in.close();
}

void getAuthor(string s) {
    ifstream in(s);
    vector<string> saveLine;
    string line;
    
    while(getline(in, line)) {
        saveLine.push_back(line);
        //        cout << line << endl;
    }
    cout << saveLine[12] << endl;
    in.close();
}

void getWordCount(string s) {
    ifstream in(s);
    vector<string> saveWord;
    string word;
    int wordC = 0;
    
    while(in >> word) {
        saveWord.push_back(word);
    }
    for(int i = 0; i < saveWord.size(); i++) {
        wordC += 1;
    }
    cout << "The word count is: " << wordC << endl;
    in.close();
}

void getCharCount(string s) {
    ifstream in(s);
    vector<string> saveWord;
    string word;
    int charC = 0;
    
    while(in >> word) {
        saveWord.push_back(word);
    }
    for(int i = 0; i < saveWord.size(); i++) {
        charC +=saveWord[i].size();
    }
    cout << "The character count is: " << charC << endl;
    in.close();
}

void isLongestWord(string s) {
    ifstream in(s);
    vector<string> saveWord;
    string word;
    string longest;
    long compare;
    
    while(in >> word) {
        saveWord.push_back(word);
        
    }
    compare = saveWord[0].size();
    for(int i = 1; i < saveWord.size(); i++) {
        if(compare < saveWord[i].size()) {
            compare = saveWord[i].size();
            longest = saveWord[i];
        }
    }
    cout << "The longest word is: " << longest << endl;
    in.close();
}

void isShortestWord(string s) {
    ifstream in(s);
    vector<string> saveWord;
    string word;
    string shortest;
    
    while(in >> word) {
        saveWord.push_back(word);
        
    }
    long compare = saveWord[0].size();
    for(int i = 1; i < saveWord.size(); i++) {
        if(compare > saveWord[i].size()) {
            compare = saveWord[i].size();
            shortest = saveWord[i];
        }
    }
    cout << "The shortest word is: " << shortest << endl;
    in.close();
}

void getKeyWord(string s, string m) {
    ifstream in(m);
    vector<string> saveWord;
    string word;
    vector<string> app;
    int appears = 0;
    vector<int> percent;
    
    
    while(in >> word) {
        saveWord.push_back(word);
    }
    for(int i = 0; i < saveWord.size(); i++) {
        if(saveWord[i] == s) {
            appears += 1;
            
            app.push_back(saveWord[i - 1]);
            app.push_back(saveWord[i]);
            app.push_back(saveWord[i + 1]);
            
            double x = i;
            double y = saveWord.size();
            
            percent.push_back((x/y)*100);
            percent.push_back((x/y)*100);
            percent.push_back((x/y)*100);
        }
    }
    cout << s << " appears " << appears << " times at: " << endl;
    
    for(int i = 1; i < app.size(); i+=3) {
        cout << "at " << percent[i] << "%: " << app[i - 1] << " " << app[i] << " " << app[i + 1] << endl;
    }
    in.close();
}
