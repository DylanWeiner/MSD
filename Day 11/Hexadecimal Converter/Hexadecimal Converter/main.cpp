//
//  main.cpp
//  Hexadecimal Converter
//
//  Created by Dylan Weiner and Jaied Tribe on 9/2/25.
//

#include <iostream>
#include <string>
#include <cmath>
#include <cassert>

using namespace std;

int stringToHex(char hexTrack) {
    if (hexTrack >= '0' && hexTrack <= '9') {
        return hexTrack - '0';
    } else if (hexTrack >= 'a' && hexTrack <= 'f') {
        return hexTrack - 'a' + 10;
    } else if (hexTrack >= 'A' && hexTrack <= 'F') {
        return hexTrack - 'A' + 10;
    } else {
        cout << "Invalid hex character: " << hexTrack << endl;
        return 0;
    }
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

    for (int i = 0; i < value.size(); i++) {
        int track = value.size() - 1 - i;
        char currentChar = value[track];

        if (currentChar == '-') {
            convertedNumber *= -1;
            break;
        }

        int numericValue = stringToHex(currentChar); // Correctly handles 0-9, a-f, A-F

        convertedNumber += numericValue * pow(base, i);
    }

    return convertedNumber;
}

//bool isNegative(int & v) {
//        if(v < 0) {
//            return true;
//        }
//        else {
//            return false;
//        }
//}

//string negativeResponse(int & v, string & d) {
//    string ans;
//    if(v) {
//        return "-" + d;
//    }
//    else {
//        return d;
//    }
//}



string intToStringConversion(int v, int x) {
    int remainder = v;
    string decValue;
    int sin = 1;
    
    if(v < 0) {
        v *= -1;
        sin = -1;
    }
    while(v > 0) {
        remainder = v % x;
        v /= x;
        
        decValue = to_string(remainder) + decValue;
    }
    
    if(x == 2 && sin < 0) {
        for(int i = 0; i < decValue.size(); i++) {
            if(decValue[i] == '0') {
                decValue[i] = '1';
            }
            else{
                decValue[i] = '0';
            }
        }
        decValue[decValue.size()-1] = '1';
        cout << decValue << endl;
    }
    
    else if(sin < 0) {
        cout << "-" << decValue << endl;
    }
    else {
        cout << decValue << endl;
    }
    return decValue;
}

string intToDecimalString(int v) {
    return intToStringConversion(v, 10);
}

string intToBinaryString(int v) {
    return intToStringConversion(v, 2);
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
    
    assert(stringToNumber("99", 10) == 99);
    assert(stringToNumber("F", 16) == 15);
    assert(stringToNumber("-F", 16) == -15);
    assert(stringToNumber("1010", 2) == 10);
    assert(stringToNumber("10", 2) == 2);
    
    
    
    return 0;
}
