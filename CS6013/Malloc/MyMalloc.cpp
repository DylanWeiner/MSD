// Author: Dylan Weiner
// Date: 3/6/2026
// Assignment: Malloc Replacement

#include "MyMalloc.h"
#include <unistd.h>
#include <stdexcept>

void* MyMalloc::allocate(size_t bytesToAllocate) {
    size_t pageSize = static_cast<size_t>(getpagesize());
    size_t roundedSize = ((bytesToAllocate + pageSize -1) / pageSize) * pageSize;
    void* ptr = mmap(nullptr, roundedSize, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0); // Allocate memory using mmap
    
    // Check if mmap() failed
    if (ptr == MAP_FAILED) {
        throw std::runtime_error("allocate() failed: mmap() returned MAP_FAILED for size " + std::to_string(roundedSize));
    }
    
    allocatedBlocks.insert(ptr, roundedSize);
    return ptr;
}

void MyMalloc::deallocate(void*  ptr) {
    // Remove returns the size if found, or -1 if not found
    size_t size = allocatedBlocks.remove(ptr);
    
    // Check if pointer was found in the hash table
    if (size == static_cast<size_t>(-1)) {
        throw std::runtime_error("deallocate() failed: pointer " + std::to_string(reinterpret_cast<uintptr_t>(ptr)) + 
                                 " not found in allocated blocks");
    }
    
    // Now safely munmap with the correct size
    if (munmap(ptr, size) == -1) {
        throw std::runtime_error("deallocate() failed: munmap() returned -1 for pointer " + 
                                 std::to_string(reinterpret_cast<uintptr_t>(ptr)));
    }
}