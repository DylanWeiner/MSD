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

int stringToHex(char hexTrack) {
    int binaryAsciiDiff = 87; // 87 is the difference between the character's ASCII value and its binary value. Rename later.
    
    return hexTrack - binaryAsciiDiff;
}

//char hexToChar(int v) {
//    char hex;
//    
//    if(v < 10) {
//        hex = v;
//        return hex;
//    }
//    else {
//        return 'A' + (v - 10);
//    }
//}

int stringToNumber(string value, int base) {
    int convertedNumber = 0;
    
    for(int i = 0; i < value.size(); i++){
        
        int track = value.size()-1-i;
        if(value[track] == '-') {
            convertedNumber = -1 * convertedNumber;
            break;
        }
        
        int numericValue = value[track] - '0';
        
        if(value[track] >= 'a') { // a has a numerical equivalent so make use of this.
            numericValue = stringToHex(value[track]);
        }
        convertedNumber += numericValue*(pow(base, i));
    }
    return convertedNumber;
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
    
    int fullVal = stringToNumber(value, base);
    
    cout << fullVal << endl;
    
    intToDecimalString(fullVal);
    
    intToBinaryString(fullVal);
    
    intToHexadecimalString(fullVal);
    
    return 0;
}
