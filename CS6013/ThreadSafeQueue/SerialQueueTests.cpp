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
#include <assert.h>

// void MultTesting(SerialQueue<int> exQueue, int sampleSize) {
//     for(int i = 1; i <= sampleSize; i*=2) {
//         int val = i;
//         exQueue.enqueue(val);
//     }

//     int ret;

//     for(int i = 1; i <= sampleSize; i*=2) {
//         exQueue.dequeue(&ret);
//         std::cout << ret;
//         if(i != sampleSize) {
//             std::cout << ", ";
//         }
//         assert(ret == i);
//     }

//     assert(exQueue.dequeue(&ret) == false);
// }

int main() {
    SerialQueue<int> exQueue;
    int sampleSize = 1000;

    for(int i = 1; i <= sampleSize; i++) {
        int val = i;
        exQueue.enqueue(val);
    }

    int ret;

    for(int i = 1; i <= sampleSize; i++) {
        exQueue.dequeue(&ret);
        std::cout << ret;
        if(i != sampleSize) {
            std::cout << ", ";
        }
        if(i % 30 == 0) {
            std::cout << std::endl;
        }
        assert(ret == i);
    }
    std::cout << std::endl;

    assert(exQueue.dequeue(&ret) == false);

    for(int i = 1; i <= sampleSize; i*=2) {
        int val = i;
        exQueue.enqueue(val);
    }

    for(int i = 1; i <= sampleSize; i*=2) {
        exQueue.dequeue(&ret);
        std::cout << ret;
        if(i != sampleSize) {
            std::cout << ", ";
        }
        assert(ret == i);
    }

    assert(exQueue.dequeue(&ret) == false);
    
    return 0;
}