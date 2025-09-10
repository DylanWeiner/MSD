//
//  main.cpp
//  TestingForTest
//
//  Created by Dylan Weiner on 9/4/25.
//

#include <iostream>
#include <vector>

using namespace std;

int main(int argc, const char * argv[]) {
    vector<int> numbers = {4,9,3,17,6,43,100,70,62};
    int bigThree = 3;
    vector<int> three;
    
//    for(int i = 0; i < numbers.size()-1; i++) {
//        if(numbers[i] < numbers[1+i]) {
//            int temp = numbers[i];
//            numbers[i] = numbers[i+1];
//            numbers[i+1] = temp;
//        }
//        else{continue;}
//        cout << numbers[i] << endl;
//    }

    
    for(int i = 0; i < bigThree; i++) {
        int biggest[3];
        biggest[i] = numbers[0];
        for(int j = 1; j < numbers.size(); j++) {
            if(biggest[i] < numbers[j] && biggest[i+1] != biggest[i]) {
                biggest[i] = numbers[j];
                
            }
            else {
                continue;
            }
        three.push_back(biggest[i]);
        }
        
        cout << three[i] << endl;
    }
    
    return 0;
}
