//
//  main.cpp
//  RomanNumerals
//
//  Created by Dylan Weiner on 8/21/25.
//

#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    int num;
    int leftOver = 0;
    
    std::cout << "Please input a number to receive its Roman Numeral. ";
    std::cin >> num;
    
    if(num >= 0) {
        int p;
        int m = num / 1000;
        leftOver = num - (m*1000);
        int cm = leftOver / 900;
        
        leftOver = leftOver - (cm*900);
        int d = leftOver / 500;
        
        leftOver = leftOver - (d*500);
        int cd = leftOver / 400;
        
        leftOver = leftOver - (cd*400);
        int c = leftOver / 100;
        
        leftOver = leftOver - (c*100);
        int xc = leftOver / 90;
        
        leftOver = leftOver - (xc*90);
        int l = leftOver / 50;
        
        leftOver = leftOver - (l*50);
        int xl = leftOver / 40;
        
        leftOver = leftOver - (xl*40);
        int x = leftOver / 10;
        
        leftOver = leftOver - (x*10);
        int ix = leftOver / 9;
        
        leftOver = leftOver - (ix*9);
        int v = leftOver / 5;
        
        leftOver = leftOver - (v*5);
        int iv = leftOver / 4;
        
        leftOver = leftOver - (iv*4);
        int i = leftOver / 1;
        
        leftOver = leftOver - (i*1);
        
        std::string numeral;
        
        for(p=0;p<m;p++){
            numeral += "M";
        }
        
        for(p=0;p<cm;p++){
            numeral += "CM";
        }
        
        for(p=0;p<d;p++){
            numeral += "D";
        }
        
        for(p=0;p<c;p++){
            numeral += "C";
        }
        
        for(p=0;p<xc;p++){
            numeral += "XC";
        }
        
        for(p=0;p<l;p++){
            numeral += "L";
        }
        
        for(p=0;p<xl;p++){
            numeral += "XL";
        }
        
        for(p=0;p<x;p++){
            numeral += "X";
        }
        
        for(p=0;p<ix;p++){
            numeral += "IX";
        }
        
        for(p=0;p<v;p++){
            numeral += "V";
        }
        
        for(p=0;p<iv;p++){
            numeral += "IV";
        }
        
        for(p=0;p<i;p++){
            numeral += "I";
        }
    std::cout << "\nYour Roman Numeral is: " << numeral << "\n";
    return 0;
    }
    else {
        std::cout << "\nThere are no negative Roman Numerals, try again!\n";
        return 1;
    }

}

