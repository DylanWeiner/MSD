//
//  Vector.h
//  Vector
//
//  Created by Dylan Weiner on 9/9/25.
//

#include "Vector.h"

vector::vector(int initialCapacity) {
    array = new int[initialCapacity];
    capacity = initialCapacity;
    size = 0;
}

int vector::viewCapacity() {
    return capacity;
}

int vector::viewSize() {
    return size;
}

void vector::freeVector() {
    delete[] array;
}

void vector::pushBack(int newVal) {
    *(array + size) = newVal;
    size++;
}

void vector::popBack(){
    array[size] = NULL;
    size--;
}

int vector::get(int index) {
    return *(array + index);
}

void vector::set(int index, int newValue) {
    *(array + index) = newValue;
}

void vector::growVector() {
    capacity *= 2;
    int* newCap = new int(capacity);
    for(int i = 0; i < size; i++) {
        *(newCap + i) = *(array + i);
    }
    freeVector();
    array = newCap;
}
