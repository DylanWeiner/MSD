//
//  main.cpp
//  ForLoopPractice
//
//  Created by Dylan Weiner on 8/20/25.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    
    int c = 1;
    int num1;
    int num2;
    int odd1 = 1;
    int uval = 0;
    int total = 0;
    
    for(int i = 1; i <= 10; i++) {
        std::cout<<"Value: " <<i<< std::endl;
    }
    while (c <= 10) {
        std::cout << "Value: " << c << " ";
        
        c++;
    } // The while loop has better formatting and produces cleaner text
    
    std::cout<< "\n";
    
    std::cout<< "Let's trying something, give me two numbers and watch me count from one to the other.\n";
    std::cin >> num1;
    std::cin >> num2;
    if(num1 < num2) {
        while (num1 <= num2) {
            std::cout << num1 << ", ";
            num1++;
        }
    }
        else{
            while (num2 <= num1) {
                std::cout << num2 << ", ";
                num2++;
            }
        }
    
    
    std::cout <<"\n\n";
    if(odd1 == 1) {
        while (odd1 < 20) {
            std::cout << odd1 << ", ";
            odd1++;
            odd1++;
        }
        std::cout <<"\n";
    for(int odd2 = 1; odd2 < 20; odd2+=2) {
            std::cout << odd2 << std::endl;
        } //The for statement is much tidier and takes both less code and, subsequently, less memory.
    }
    std::cout <<"\n\n";
    
    while(uval >= 0) {
        std::cout << "Please input a whole positive number (decimals will get dropped).  ";
        std::cin >> uval;
        if(uval < 0) {
            std::cout << "Your total is " << total << "\n";
            break;
        }
        total = total + uval;
    }
    
    for(int mult = 1; mult <= 5; mult++) {
        std::cout<< mult << "x*: " ;
        for(int x = 1; x <=5; x++){
            std::cout << mult * x << " ";
        }
        std::cout << std::endl;
    }
    
    return 0;
}
