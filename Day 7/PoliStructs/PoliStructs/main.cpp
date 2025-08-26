//
//  main.cpp
//  PoliStructs
//
//  Created by Dylan Weiner on 8/25/25.
//

#include <iostream>
#include <string>
#include <vector>

struct politician {
    std::string name;
    std::string party;
    std::string level;
};

std::vector<politician> JavacansTest(std::vector<politician> politicians){
    std::vector<politician> javacansPoliticians;
    
    for (int i =0; i< politicians.size();i++){
        if((politicians[i].party) == "Javacans"){
            javacansPoliticians.push_back(politicians[i]);
        }
    }
    return javacansPoliticians;
}

std::vector<politician> FedC(std::vector<politician> politicians){
    std::vector<politician> javacansPoliticians;
    
    for (int i =0; i< politicians.size();i++){
        if((politicians[i].party) == "Cplusers" && (politicians[i].level) == "Federal"){
            javacansPoliticians.push_back(politicians[i]);
        }
    }
    return javacansPoliticians;
}

int main(int argc, const char * argv[]) {
    std::vector<politician> val;
    std::string response;
    politician pVal;
    std::string done;
    
    politician p1 = {"Hank Green", "Javacans", "Federal"};
    
    politician p2 = {"Matthew Patrick", "Cplusers", "Federal"};
    
    politician p3 = {"Adam Sandler", "Javacans", "State"};
    
    politician p4 = {"Ellieanor Tanner", "Cplusers", "State"};
    
    val.push_back(p1);
    val.push_back(p2);
    val.push_back(p3);
    val.push_back(p4);
    
    for(int i = 0; i < val.size(); i++) {
        std::cout << "Please input a politician's name: ";
        getline(std::cin, pVal.name);
        
        std::cout << "Please input a politician's party (Javacans or Cplusers): ";
        getline(std::cin, pVal.party);
        
        std::cout << "Is this politician federal or state level: ";
        getline(std::cin, pVal.level);
        
        val.push_back(pVal);
        
        std::cout << "Are you done? (Y/N): ";
        
        getline(std::cin, done);
        
        if(done == "Y" || done == "y") {
            break;
        }
        else {
            continue;
        }
    }
    
    std::vector<politician> javas = JavacansTest(val);
    std::vector<politician> feds = FedC(val);
    
    for(int i = 0; i < javas.size(); i++) {
        std::cout << (javas[i]).name << ", " << (javas[i]).party << ", " << (javas[i]).level << std::endl;
    }
    
    for(int i = 0; i < feds.size(); i++) {
        std::cout << (feds[i]).name << ", " << (feds[i]).party << ", " << (feds[i]).level << std::endl;
    }
    
    return 0;
}

