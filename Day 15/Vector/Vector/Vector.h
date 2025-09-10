//
//  Vector.h
//  Vector
//
//  Created by Dylan Weiner on 9/9/25.
//

#ifndef VECTOR_H
#define VECTOR_H

#include <iostream>

#include <cassert>

class vector {
private:
    int * array;
    int capacity; //absolute max.
    int size; //active ints being stored
    
public:
    vector(int capacity);
    
    int viewCapacity();
    
    int viewSize();
    
    vector makeVector(int initialCapacity);

    void freeVector();

    void pushBack(int newVal);

    void popBack();

    int get(int index);

    void set(int index, int newValue);

    void growVector();
};

#endif
