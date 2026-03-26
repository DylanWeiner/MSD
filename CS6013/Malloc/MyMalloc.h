#include <cstddef>
#include <unordered_map>
#include <iostream>
#include "hashTable.h"

class MyMalloc {
public:
    hashTable * allocatedBlocks;
    void* allocate(size_t bytesToAllocate);
    void deallocate(void*  ptr);
};
