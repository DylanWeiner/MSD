//
//  TripleTime.h
//  TripleTemplate
//
//  Created by Dylan Weiner on 9/11/25.
//
#include <iostream>

#include <vector>

#include <string>

using namespace std;

template <typename T>

struct Triple {
    T a;
    T b;
    T c;
    
    T addVar();
};

template <typename T>
T Triple<T>::addVar() {
    return a + b + c;
}


