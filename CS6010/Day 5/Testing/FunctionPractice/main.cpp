//
//  main.cpp
//  Testing
//
//  Created by Dylan Weiner on 8/22/25.
//

#include <iostream>
#include <cassert>
#include <string>
#include <cmath>
#include <ctime>

float side(float a, float b) {
    float h = std::sqrt((a*a) + (b*b));
    return h;
}

double xvelocity(double speed, double angle) {
    return speed*std::cos(angle);
}

double yvelocity(double speed, double angle) {
    return speed*std::sin(angle);
} //Using std::cin within the function assigns the function more than one task which does not separate concerns

bool isCapitalized(std::string word1) {
    bool cap;
    char letter = word1[0];
    if(letter < 'a') {
        cap = true;
    }
    else {
        cap = false;
    }
    return cap;
}

bool isPrime(int uNum) {
    for(int i = 2; i < uNum; i++) {
        if((uNum % i) == 0) {
            return false;
        }
    }
        return true;
}
    
    std::string boolToString(bool cap) {
        if(cap) {
            return "true";
        }
        else {
            return "false";
        }
        
    }


int main()
{
    float x;
    float y;
    double zoom;
    double ang;
    std::string word;
    int prime;
    
    std::cout << "Please input two lengths to find the hypotenuse of a triangle: ";
    std::cin >> x;
    std::cin >> y;
    if(x > 0 && y > 0) {
        float hyp = side(x, y);
        std::cout << hyp << "\n";
    }
    
    else {
        std::cout << "This won't work.";
    }
    
    
    std::cout << "Please input your speed and angle to find your x and y velocity: ";
    std::cin >> zoom;
    std::cin >> ang;
    
    if(zoom > 0 && ang > 0) {

        
        double velx = xvelocity(zoom, ang);
        std::cout <<"Your X Velocity is: " << velx << "\n";
        
        double vely = yvelocity(zoom, ang);
        std::cout <<"Your Y Velocity is: " << vely << "\n";
    }
    
    else {
        std::cout << "This won't work.";
    }
    
    //std::cout << "Please input a number to see if it is prime: ";
    //std::cin >> isPrime(prime);
    
    
    std::cout << "Please input a word to see if it's capitalized: ";
    std::cin >> word;
    bool boolCapital = isCapitalized(word);
    std::string fin = boolToString(boolCapital);
    std::cout << fin << std::endl;
    
    std::time_t result = std::time(nullptr);
    std::cout << std::asctime(std::localtime(&result))
              << result << " seconds since the Epoch\n";
}
