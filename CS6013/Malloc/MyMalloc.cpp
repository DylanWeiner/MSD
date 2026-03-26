// Author: Dylan Weiner
// Date: 3/6/2026
// Assignment: Malloc Replacement

#include "MyMalloc.h"

void* MyMalloc::allocate(size_t bytesToAllocate) {
    void* ptr = mmap(nullptr, bytesToAllocate, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0); // Allocate memory using mmap
    std::cout << "Successful mmap()" << std::endl;
    allocatedBlocks->insert(ptr, bytesToAllocate);
    std::cout << "Successful insert" << std::endl;
    return ptr;
}

void MyMalloc::deallocate(void*  ptr) {
    allocatedBlocks->remove(ptr);
}