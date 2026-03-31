// Author: Dylan Weiner
// Date: 3/6/2026
// Assignment: Malloc Replacement

#include "MyMalloc.h"

void* MyMalloc::allocate(size_t bytesToAllocate) {
    void* ptr = mmap(nullptr, bytesToAllocate, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0); // Allocate memory using mmap
    allocatedBlocks.insert(ptr, bytesToAllocate);
    // size_t pageSize = static_cast<size_t>(getpagesize());
    return ptr;
}

void MyMalloc::deallocate(void*  ptr) {
    munmap(ptr, allocatedBlocks.remove(ptr));
}