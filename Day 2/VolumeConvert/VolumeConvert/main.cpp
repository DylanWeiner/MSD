//
//  main.cpp
//  VolumeConvert
//
//  Created by Dylan Weiner and Alexis Blood on 8/19/25.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    float oz; //defines all of our variables
    float cup;
    float pint;
    float gal;
    float L;
    float cbin;
    
    std::cout << "Please write a number of Ounces to be converted.\n"; //Prompts the user for the necessary input.
    std::cin >> oz; //requests the input.
    
    cup = oz / 8; // provides the mathematics to quantify how to the variables should convert in accordance with the input.
    pint = oz / 16;
    gal = oz / 128;
    L = oz * 0.0296;
    cbin = oz * 1.8;
    
    std::cout << oz << " Ounce(s):\n" << cup << " Cup(s)\n" << pint << " Pint(s)\n" << gal << " Gallon(s)\n" << L << " Liter(s)\n" << cbin << " Cubic Inch(es)\n"; //Lists out the values in accordance with the input.
    
    return 0;
}
