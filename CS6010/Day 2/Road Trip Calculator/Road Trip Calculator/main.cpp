//
//  main.cpp
//  Road Trip Calculator
//
//  Created by Dylan Weiner on 8/19/25.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    // insert code here...
    int miles;
    int mpg;
    float price;
    float gallon;
    
    std::cout << "Please input the distance in miles, your vehicle's miles/gallon, and the cost of gas.\n";
    
    std::cin >> miles >> mpg >> price;
    
    gallon = miles*price;
    
    std::cout << "The price of gas over the course of this road trip is $" << gallon/mpg << ". Make sure to budget for snacks!\n";
    
    return 0;
}
