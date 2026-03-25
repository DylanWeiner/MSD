// Author: Dylan Weiner
// Date: 3/6/2026
// Assignment: Malloc Replacement

#include "MyMalloc.h"

std::unordered_map<void*, size_t> allocatedBlocks; // Hash table to keep track of addresses and the associated size of memory allocated to that address

void* MyMalloc::malloc(size_t bytesToAllocate) {
    
}

void* MyMalloc::allocate(size_t bytesToAllocate) {
    void* ptr = malloc(bytesToAllocate);
    allocatedBlocks[ptr] = bytesToAllocate;
    return ptr;
}

void MyMalloc::deallocate(void*  ptr) {
    auto it = allocatedBlocks.find(ptr);
    if (it != allocatedBlocks.end()) {
        free(ptr);
        allocatedBlocks.erase(it);
    }
}

