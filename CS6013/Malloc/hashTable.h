#pragma once

#include <cstddef>
#include <sys/mman.h> 

enum class State{
    Free,
    Occupied,
    Deleted
};

struct HashEntry {
    void* address;
    size_t allocationSize;
    State state;
};

class hashTable {
public:
    hashTable();
    ~hashTable();
    // hashTable(int capacity);
    void* getAddress(int location);
    size_t getSize();
    size_t getCapacity();
    State getState(int location);
    void insert(void* address, size_t sizeOfAllocation);
    size_t remove(void* address);
    
private:
    size_t size;
    size_t capacity;
    size_t numBytes = sizeof(HashEntry);
    HashEntry * addressTable;
    int hash(void* address);
    void grow();
    int find(void* desiredAddress);
};