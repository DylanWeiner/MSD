//
//  main.cpp
//  StringNumbersConversion
//
//  Created by Dylan Weiner on 8/21/25.
//

#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    
    int num = 40;
    std::string strNum = std::to_string(num); //makes the number a string so it concatenates rather than adding.
    
    std::string strNum2 = "2000";
    
    int numValue = std::stoi(strNum2);
    
    std::string strNum3 = "2000";
    
    for (int i=0; i < strNum3.length(); i++) { //defines i as 0 and increases it by 1 everytime i is less than 2000
        if(strNum3[i] == '2') //checks if i is equal to 2
            strNum3[i] = '9'; // once i is equal to 2 it takes every 2 in strNum3 and replaces it with a 9
    }
    std::cout << strNum3; //outputs 9000
    
    return 0;
}
