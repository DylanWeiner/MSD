//
//  main.cpp
//  IfStatementPractice
//
//  Created by Jæid Tribe, Roman Burningham, Dylan Weiner on 8/20/25.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    int age;
    char weekdayResponse;
    char holiday;
    char parent;
    
    std::cout << "Please input your age.\n";
    std::cin >> age;
    
    if(age>=80) {
        std::cout << "Then, you're part of the greatest generation, oldhead!\n";
    }
    else if(age>= 60) {
        std::cout<< "Congrats, you got your result boomer, now move on.\n";
    }
    else if(age>=40) {
        std::cout<< "You're a part of Gen X. I gotta be honest, I don't have anything witty for you, but good job.\n";
    }
    else if(age>=20) {
        std::cout << "You're a millenial, good luck with the economy!\n";
    }
    else {
        std::cout << "You're a Zoomer, good luck with the boomers.\n";
    }
    
    std::cout << "Good morning, I'm an Amazon representative looking to check in. Time to collect your data (For charity reasons of course). Is it a weekday right now? (Y/N)?\n";
    std::cin >> weekdayResponse;
    std::cout << "Is it a holiday right now (Y/N)?\n";
    std::cin >> holiday;
    std::cout << "Cool (Or not, I'm not paying great attention.) Last question, are you a parent (Y/N)?\n";
    std::cin >> parent;
    std::cout << "Mr. Bezos appreciates your honesty.\n";
    
    bool isWeekday = weekdayResponse == 'Y' || weekdayResponse == 'y';
    bool isHoliday = holiday == 'Y' || holiday == 'y';
    bool isParent = parent == 'Y' || parent == 'y';
    
    

    if(isParent) {
        std::cout << "Mr. Bezos said you need to wake up!\n";
    }
    else if(isHoliday) {
        std::cout << "Go get some rest, you aren't busy or increasing shareholder value today.\n";
    }
    else if(isWeekday) {
        std::cout << "Mr. Bezos said you need to wake up!\n";
    }
    else {
        std::cout << "Go get some rest, you aren't busy or increasing shareholder value today.\n";
    }


    
    return 0;
}
