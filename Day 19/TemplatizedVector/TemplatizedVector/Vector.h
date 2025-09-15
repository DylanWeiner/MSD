//
//  Vector.h
//  TemplatizedVector
//
//  Created by Dylan Weiner on 9/11/25.
//

#ifndef VECTOR_H
#define VECTOR_H

#include <iostream>

#include <cassert>

#include <string>

template <typename T>
class vector {

    
public:
    vector();
    vector(int capacity);
    vector makeVector(int initialCapacity);
    vector(const vector& rhs);
    
    T& operator=(const vector rhs);
    
    int& operator[](const int index);
    int operator[](const int index)const;
    
    int viewCapacity();
    int viewSize();
    void freeVector();
    void pushBack(T newVal);
    void popBack();
    int get(int index);
    void set(int index, T newValue);
    void growVector();
    
    bool operator==(const vector & rhs)const;
    bool operator!=(const vector & rhs)const;
    bool operator<(const vector & rhs)const;
    bool operator<=(const vector & rhs)const;
    bool operator>(const vector & rhs)const;
    bool operator>=(const vector & rhs)const;
    
private:
    T * array;
    int capacity; //absolute max.
    int size; //active ints being stored
};

template <typename T>
vector<T>::vector() {
    array = new T[10];
    capacity = 10;
    size = 0;
}

template <typename T>
vector<T>::vector(int initialCapacity) {
    array = new T[initialCapacity];
    capacity = initialCapacity;
    size = 0;
}

template <typename T>
T& vector<T>::operator=(const vector rhs) {
    swap((*this).size, rhs.size);
    swap((*this).array, rhs.array);
    
    return (*this);
}

template <typename T>
vector<T>::vector(const vector& rhs) {
    (*this).array = new int[rhs.capacity];
    (*this).capacity = rhs.capacity;
    (*this).size = rhs.size;
    
    for(int i = 0; i < size; i++) {
        this->array[i] = rhs.array[i];
    }
}

template <typename T>
int& vector<T>::operator[](const int index) {
    return array[index];
} // This redefines a value at an index.

template <typename T>
int vector<T>::operator[](const int index)const {
    return array[index];
} // This reads a value in an array so it can be output.

template <typename T>
int vector<T>::viewCapacity() {
    return capacity;
}

template <typename T>
int vector<T>::viewSize() {
    return size;
}

template <typename T>
void vector<T>::freeVector() {
    delete[] array;
}

template <typename T>
void vector<T>::pushBack(T newVal) {
    array[size] = newVal;
    size++;
}

template <typename T>
void vector<T>::popBack(){
    array[size] = NULL;
    size--;
}

template <typename T>
int vector<T>::get(int index) {
    return array[index];
}

template <typename T>
void vector<T>::set(int index, T newValue) {
    array[index] = newValue;
}

template <typename T>
void vector<T>::growVector() {
    capacity *= 2;
    int* newVec = new int(capacity);
    for(int i = 0; i < size; i++) {
        newVec[i] = array[i];
    }
    freeVector();
    array = newVec;
}

template <typename T>
bool vector<T>::operator==(const vector & rhs)const {
    if((*this).size != rhs.size) {
        return false;
        for (int i = 0; i < (*this).size; i++) {
            if((*this).array[i] != rhs.array[i])
                return false;
        }
    }
    return true;
}

template <typename T>
bool vector<T>::operator!=(const vector & rhs)const {
    return !((*this)==rhs);
}

template <typename T>
bool vector<T>::operator<(const vector & rhs)const {
    if((*this).size > rhs.size) {
        return false;
        for(int i = 0; i < rhs.size; i++) {
            if((*this).array[i] > rhs.array[i])
                return false;
        }
    }
    return true;
}

template <typename T>
bool vector<T>::operator<=(const vector & rhs)const {
    return ((*this) < rhs || (*this) == rhs);
}

template <typename T>
bool vector<T>::operator>(const vector & rhs)const {
    return !((*this) < rhs || (*this) == rhs);
}

template <typename T>
bool vector<T>::operator>=(const vector & rhs)const {
    return !((*this) < rhs);
}

#endif
