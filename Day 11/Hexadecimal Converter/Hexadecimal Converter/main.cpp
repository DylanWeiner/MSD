//
//  main.cpp
//  Hexadecimal Converter
//
//  Created by Dylan Weiner and Jaied Tribe on 9/2/25.
//

#include <iostream>
#include <string>
#include <cmath>

using namespace std;

int stringToInt(string value, int base) {
    int valFin = 0;
    
    for(int i = 0; i < value.size(); i++){
        
        int track = value.size()-1-i;
        if(value[track] == '-') {
            valFin = -1 * valFin;
            break;
        }
        char val1 = value[track];
        int numericValue = val1 - '0';
        
        char hexTrack = value[track];
        if(hexTrack == 'a') {
            numericValue = 10;
        }
        else if(hexTrack == 'b') {
            numericValue = 11;
        }
        else if(hexTrack == 'c') {
            numericValue = 12;
        }
        else if(hexTrack == 'd') {
            numericValue = 13;
        }
        else if(hexTrack == 'e') {
            numericValue = 14;
        }
        else if(hexTrack == 'f') {
            numericValue = 15;
        }
        
        valFin += numericValue*(pow(base, i));
    }
    return valFin;
}

string intToDecimalString(int v) {
    int remainder = v;
    string decValue;
    int sin = 0;
    
    if(v < 0) {
        v *= -1;
        sin = -1;
    }
    
    while(v > 0) {
        remainder = v % 10;
        v /= 10;
        
        decValue = to_string(remainder) + decValue;
    }
    if(sin < 0) {
        cout << "-" << decValue << endl;
    }
    else {
        cout << decValue << endl;
    }
    return decValue;
}

string intToBinaryString(int v) {
    int remainder = v;
    string decValue;
    int sin = 0;
    
    if(v < 0) {
        v *= -1;
        sin = -1;
    }
    
    while(v > 0) {
        remainder = v % 2;
        v /= 2;
        
        decValue = to_string(remainder) + decValue;
    }

    if(sin < 0) {
        cout << "-" << decValue << endl;
    }
    else {
        cout << decValue << endl;
    }
    return decValue;
}

string intToHexadecimalString(int v) {
    int remainder = 0;
    string decValue;
    string hexVal;
    int sin = 1;
    
    if(v < 0) {
        v *= -1;
        sin = -1;
    }
    
    while(v > 0) {
        remainder = v % 16;
        v /= 16;
        
        //        cout << remainder << endl;
        
        if(remainder == 10) {
            hexVal = "a";
        }
        else if(remainder == 11) {
            hexVal = "b";
        }
        else if(remainder == 12) {
            hexVal = "c";
        }
        else if(remainder == 13) {
            hexVal = "d";
        }
        else if(remainder == 14) {
            hexVal = "e";
        }
        else if(remainder == 15) {
            hexVal = "f";
        }
        else {
            hexVal = remainder + '0';
        }
        
        decValue = hexVal + decValue;
//        cout << decValue << endl;
    }
    if(sin < 0) {
        cout << "-" << decValue << endl;
    }
    else {
        cout << decValue << endl;
    }
    return decValue;
}



int main(int argc, const char * argv[]) {
    string value;
    int base = 0;
    
    cout << "Please input a value: ";
    
    getline(cin, value);
    
    cout << "Please input a base: ";
    
    cin >> base;
    
    int fullVal = stringToInt(value, base);
    
    cout << fullVal << endl;
    
    intToDecimalString(fullVal);
    
    intToBinaryString(fullVal);
    
    intToHexadecimalString(fullVal);
    
    return 0;
}
