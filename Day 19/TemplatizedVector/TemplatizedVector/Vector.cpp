//
//  Vector.h
//  TemplatizedVector
//
//  Created by Dylan Weiner on 9/11/25.
//

#include "Vector.h"

vector::vector(int initialCapacity) {
    array = new int[initialCapacity];
    capacity = initialCapacity;
    size = 0;
}

void vector::operator=(const vector & rhs) {
    (*this).array = rhs.array;
    (*this).capacity = rhs.capacity;
    (*this).size = rhs.size;
}

vector::vector(const vector& rhs) {
    (*this).array = new int[rhs.capacity];
    (*this).capacity = rhs.capacity;
    (*this).size = rhs.size;
    
    for(int i = 0; i < size; i++) {
        this->array[i] = rhs.array[i];
    }
}

int& vector::operator[](const int index) {
    return *(array + index);
} // This redefines a value at an index.

int vector::operator[](const int index)const {
    return *(array + index);
} // This reads a value in an array so it can be output.

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
