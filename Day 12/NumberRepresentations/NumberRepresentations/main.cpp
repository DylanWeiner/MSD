//
//  main.cpp
//  NumberRepresentations
//
//  Created by Dylan Weiner on 9/3/25.
//

#include <iostream>
#include <cstdint>
#include <iomanip>
#include <cassert>
#include <cmath>
#include <fstream>
#include <vector>

using namespace std;

bool approxEquals(double sum, double compare, double tolerance) {
    double absolute = abs(sum - compare);
    if(absolute > tolerance) {
        return false;
    }
    else {
        return true;
    }
}

void fileOutput(string s) {
    ifstream in(s);
    string word;
    vector<char> c;
    vector<uint8_t> cValue;
    int asc = 0;
    int uni = 0;
    
    while(in >> word) {
        for(int i = 0; i < word.size(); i++) {
            c.push_back(word[i]);
            cValue.push_back(word[i]);
            cout << word[i] << endl;
            if(cValue.back() <= 127) {
                asc += 1;
            }
            else {
                uni += 1;
            }
        }
    }
    
    cout << "ASCII: " << asc << "\n" << "Unicode: " << uni << endl;
    in.close();
}

int main(int argc, const char * argv[]) {
    uint8_t max8 = 0xFF; // adding 1 caused it to loop back to zero.
    uint8_t min8 = 0; // subtracting one cause it to loop to the max value.
    uint16_t max16 = 0xFFFF;
    uint16_t min16 = 0;
    uint64_t max64 = 0xFFFFFFFFFFFFFFFF; //adding 1 here just errored it out
    uint64_t min64 = 0;
    
    char character;
    double doub;
    float flo;
    int integer;
    
    cout << sizeof(+character) << "\n" << sizeof(+doub) << "\n" << sizeof(+flo) << "\n" << sizeof(+integer) << endl << endl;
    
    cout << +min8 << "\n" << +max8 << endl;
    cout << +min16 << "\n" << +max16 << endl;
    cout << +min64 << "\n" << +max64 << endl;
    
    int8_t smax8 = 0x7F; // adding 1 caused it to loop back to zero.
    int8_t smin8 = 0x81; // subtracting one cause it to loop to the max value.
    int16_t smax16 = 0x7FFF; //It uses 7 instead of another F bc half the storage is negative so the very front 0/1 is responsible for assigning negative and 7 is 0111.
    int16_t smin16 = 0x8001;
    int64_t smax64 = 0x7FFFFFFFFFFFFFFF;
    int64_t smin64 = 0x8000000000000001;
    
    //    smax8 += 1;
    //    smax16 += 1; // This sets the value to -32768, which is one greater but is now negative as there is one more value in the negative numbers than the positive.
    //    smax64 += 1; //Sanitizing undefined behavior triggers an overflow at this point.
    smin8 -= 1;
    smin16 -= 1; //Likewise, this sets the value to -32768, so the true min is one less than the general output.
    smin64 -= 1;
    
    cout << "\n" << +smin8 << "\n" << +smax8 << endl;
    cout << +smin16 << "\n" << +smax16 << endl;
    cout << +smin64 << "\n" << +smax64 << endl;
    
    double sum = abs(0.1 + 0.2);
    double compare = abs(0.3);
    
    assert(approxEquals(sum, compare, 0.01) == true);
    
    cout << sum << endl;
    
    string fileName = "ASCII.txt";
    
    fileOutput(fileName); //The text doesn't make much sense from what I can tell, the file is a majority Unicode. Most of the text is garbled.
    
    return 0;
}
