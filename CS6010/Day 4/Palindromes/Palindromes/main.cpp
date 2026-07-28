//
//  main.cpp
//  Palindromes
//
//  Created by Dylan Weiner & almost everyone in the cohort on 8/21/25.
//

#include <iostream>
#include <string>


    

int main(int argc, const char * argv[]) {
    std::string uval;
    char character;
    std::string palin;

    std::cout << "Please input a word and I will check if it's a palindrome: ";
    std::cin >> uval;

    for (int i=0; i <= (uval.length()-1); i++) {
        //std::string temp = std::string() +  uval[uval.length() -1 - i];
        character = uval[uval.length() -1 - i];
        palin = palin + character;
    }
    
    std::cout << palin << std::endl;
    
    if(uval == palin) {
        std::cout << uval << " is a palindrome! ::)\n";
    }
    else {
        std::cout << uval << " isn't a palindrome ::(\n";
    }
    
    return 0;
}
