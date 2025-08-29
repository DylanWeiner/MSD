//
//  main.cpp
//  BookAnalyzer
//
//  Created by Dylan Weiner on 8/29/25.
//

#include "Data.h"

int main(int argc, const char * argv[]) {
    string bookName;
    
    for(int i = 0; i< argc; i++)
    bookName = argv[i];
    
    string search;
    
    if(bookName != "mobyDick.txt" && bookName != "RomeoJuliet.txt" && bookName != "Frankenstein.txt" && bookName != "PridePrejudice.txt" && bookName != "LittleWomen.txt") {
        cout << "Not a valid file name!";
        return 1;
    }
    
    getTitle(bookName);
    
    getAuthor(bookName);
    
    getWordCount(bookName);
    
    getCharCount(bookName);
    
    isLongestWord(bookName);
    
    isShortestWord(bookName);
    
    cout << "Please input a word to search for: " << endl;
    
    getline(cin, search);
    
    getKeyWord(search, bookName);
    
    cout << "Done reading the file ::)" << endl;
    return 0;
}
