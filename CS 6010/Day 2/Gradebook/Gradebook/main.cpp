//
//  main.cpp
//  Gradebook
//
//  Created by Dylan Weiner on 9/3/25.
//

#include <iostream>
#include <vector>

using namespace std;

float calculateAverage(vector<int> s) {
    float sum = 0;
    
    for(int num:s) {
        sum += num;
    }
    return sum/s.size();
}

int main(int argc, const char * argv[]) {
    vector<int> grades = {85, 62, 97, 100, 89};
    
    std::cout << calculateAverage(grades) << endl;
    return 0;
}
