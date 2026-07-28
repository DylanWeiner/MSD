//
//  main.cpp
//  CommandLineArgs
//
//  Created by Dylan Weiner on 8/29/25.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    for(int i = 0; i < argc; i++) {
        std::cout << argv[i] << std::endl;
    }
    return 0;
}
