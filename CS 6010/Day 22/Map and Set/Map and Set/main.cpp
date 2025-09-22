//
//  main.cpp
//  Map and Set
//
//  Created by Dylan Weiner on 9/17/25.
//

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <set>
#include "Data.h"

int main(int argc, const char * argv[]) {
    string bookName;
    
    for(int i = 0; i< argc; i++)
    bookName = argv[i];
    
    if(bookName != "mobyDick.txt" && bookName != "RomeoJuliet.txt" && bookName != "Frankenstein.txt" && bookName != "PridePrejudice.txt" && bookName != "LittleWomen.txt") {
        cout << "Not a valid file name!";
        return 1;
    }
    
    getUniqueWords(bookName);
        
    getWordsCount(bookName);
    
    cout << "Done reading the file ::)" << endl;
    return 0;
}
