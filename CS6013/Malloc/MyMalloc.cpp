// Author: Dylan Weiner
// Date: 3/6/2026
// Assignment: Malloc Replacement

#include "MyMalloc.h"
#include <unistd.h>

void* MyMalloc::allocate(size_t bytesToAllocate) {
    size_t pageSize = static_cast<size_t>(getpagesize());
    size_t roundedSize = ((bytesToAllocate + pageSize -1) / pageSize) * pageSize;
    void* ptr = mmap(nullptr, roundedSize, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0); // Allocate memory using mmap
    allocatedBlocks.insert(ptr, roundedSize);
    return ptr;
}

void MyMalloc::deallocate(void*  ptr) {
    munmap(ptr, allocatedBlocks.remove(ptr));
}