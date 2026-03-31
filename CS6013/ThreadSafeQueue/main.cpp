////////////////////////////////////////////////////////////////////////
//
// Author: Dylan Weiner
// Date:4/3/26
// Assignment 5 - Thread Safe Queue
// CS 6013
//
// Main file
//  
////////////////////////////////////////////////////////////////////////

#include "SerialQueue.hpp"
// #ifndef SERIALQUEUE_HPP
// #define SERIALQUEUE_HPP

// SerialQueue();
// template <typename T>

int main() {
    SerialQueue<int> exQueue;

    int val1 = 17;
    int val2 = 3;
    int val3 = 16;
    int val4 = 12;
    int val5 = 1;

    exQueue.enqueue(val1);
    exQueue.enqueue(val2);
    exQueue.enqueue(val3);
    exQueue.enqueue(val4);
    exQueue.enqueue(val5);

    // int * ptr = exQueue.head_;
    
    std::cout << exQueue.dequeue(&val1) << std::endl;
    std::cout << exQueue.dequeue(&val2) << std::endl;
    std::cout << exQueue.dequeue(&val3) << std::endl;
    std::cout << exQueue.dequeue(&val1) << std::endl;

    // std::cout <<
    
    return 0;

}

// #endif