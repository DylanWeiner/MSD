//
//  main.cpp
//  VectorPractice
//
//  Created by Dylan Weiner on 8/24/25.
//
#include <vector>
#include <iostream>
#include <string>

bool IsDone(std::string s) {
    if(s == "done" || s == "Done") {
        return true;
    }
    else {
        return false;
    }
}

std::vector<int> Sum(int n) {
    std::vector<int> intSum;
    intSum.push_back(n);
    return intSum;
}

std::vector<char> stringToVec(std::string s) {
    std::vector<char> letList;
    
    for(int i = 0; i < s.size(); i++) {
        letList.push_back(s[i]);
    }
    return letList;
}

std::vector<int> reverse(std::vector<int> s) {
    int single;
    std::vector<int> reverseList;
    
    for (int i=0; i <= (s.size()-1); i++) {
        single = s[s.size() -1 - i];
        reverseList.push_back(single);
    }
    return reverseList;
}

int main(int argc, const char * argv[]) {
    std::string response;
    int vecSum = 0;
    int value = 0;
    std::string digit;
    std::string sentence;
    std::cout << "Please input numbers to obtain their sum. Type done when you are finished: ";
    
    
    while(std::getline(std::cin, response)) {
        std::cout<<std::stoi(response);
        
        if(IsDone(response)) {
            break;
        }
        
        else if(IsDone(response) == false) {
            value = std::stoi(response);
            vecSum += value;
        }
        std::cout << "The sum of your vector's values are: " << vecSum << std::endl;
        
        std::cout << "Please provide a statement to break down to its characters: ";
        
        std::getline(std::cin, sentence); // This will automatically take the most recent cin so all the inputs need to be getline.
        
        std::cout << "The characters of your sentence are as follows:" << std::endl;
        
        std::vector<char> phrase = stringToVec(sentence);
        
        for(int i = 0; i < phrase.size(); i++) {
            std::cout << phrase[i] << std::endl;
        }
        
        std::cout << "Please input a list of numbers separated by a commas to be reversed, press ctrl + d when you are finished: ";
        
        std::vector<int> variable;
        
        while(std::getline(std::cin, digit, ',')) {
            int var = std::stoi(digit);
            variable.push_back(var);
        }
        
        variable = reverse(variable);
        
        std::cout << "Your numbers in reverse are: " << std::endl;
        
        for(int i = 0; i < variable.size(); i++) {
            std::cout << variable[i] << std::endl;
        }
        return 0;
    }
}
