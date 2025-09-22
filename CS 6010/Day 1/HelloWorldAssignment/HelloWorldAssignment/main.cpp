//
//  main.cpp
//  HelloWorldAssignment
//
//  Created by Dylan Weiner on 8/18/25.
//

#include <iostream>
int main(int argc, const char * argv[]) {
    // insert code here...
    std::cout << "Here's some math: "; //   This is explanatory text
    std::cout << "2 * 18 = "; //The statements listed in lines 12, 14, and 16 are in place for explaining the math used.
    std::cout << 2 * 18; //The statements used in lines 13, 15, and 17 are used to perform the actual mathematical functions.
    std::cout << "   42 * 18 = "; //Text lines and mathematical operations must be separated of they will return a syntactical error.
    std::cout << 42 * 18;
    std::cout << "   19 * 24 = ";
    std::cout << 19 * 24;
    std::cout << "   "; //This is in place for formatting so the exit code doesn't look cluttered.
    
    return 0;
}
