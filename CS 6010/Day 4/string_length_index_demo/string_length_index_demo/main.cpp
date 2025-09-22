//
//  main.cpp
//  string_length_index_demo
//
//  Created by Dylan Weiner on 8/21/25.
//

#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    
    std::string word = "Grippy Whippy";
    
    
    
    char firstCharacterOfWord = word [0];
    char lastCharacterOfWord = word [word.length() -1];
    
    std::cout << word.length() << std::endl;
    
    return 0;
}
