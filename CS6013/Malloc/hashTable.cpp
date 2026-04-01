#include "hashTable.h"
#include <iostream>
#include <unistd.h>

hashTable::hashTable() { 
    this->capacity = 100; // Initial capacity
    this->numBytes = sizeof(HashEntry) * capacity; // Initial number of bytes allocated
    
    addressTable = static_cast<HashEntry*>(mmap(this, numBytes, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0)); // Allocate memory for the hash table

    if (addressTable == MAP_FAILED) {
        addressTable = nullptr;
        capacity = 0;
        numBytes = 0;
        return;
    } // Catches in case of memory mapping failure

    size = 0; // Initial size

    for(size_t i = 0; i < capacity; i++) {
        addressTable[i].address = nullptr;
        addressTable[i].allocationSize = 0;
        addressTable[i].state = 0;
    }
}

// hashTable::hashTable(int capacity) { 
//     this->capacity = capacity; // Initial capacity
//     this->numBytes = sizeof(HashEntry); // Initial number of bytes allocated
    
//     addressTable = static_cast<HashEntry*>(mmap(this, numBytes, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0)); // Allocate memory for the hash table

//     if (addressTable == MAP_FAILED) {
//         addressTable = nullptr;
//         capacity = 0;
//         numBytes = 0;
//         return;
//     } // Catches in case of memory mapping failure

//     addressTable->size = 0; // Initial size
//     addressTable->capacity = capacity;

//     for(int i = 0; i < addressTable->capacity; i++) {
//         addressTable->address[i] = nullptr;
//         addressTable->allocationSize[i] = 0;
//     }
// }

int hashTable::find(void* desiredAddress) {
    for(size_t i = 0; i < capacity; i++) {
        if (addressTable[i].address == desiredAddress && addressTable[i].state == 1) {
            int* ptr = (int*)addressTable[i].address;
            return *ptr;
        } //This might be wrong
    }
    return -1; // Address not found
}

void hashTable::grow() {
    size_t oldCapacity = capacity;
    size_t oldBytes = numBytes;
    HashEntry* oldTable = addressTable;
    capacity = oldCapacity * 2;
    numBytes = oldBytes * capacity; // Initial number of bytes allocated
    addressTable = static_cast<HashEntry*>(mmap(this, numBytes, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0)); // Allocate memory for the hash table

    for(size_t i = 0; i < oldCapacity; i++) {
        insert(oldTable[i].address, oldTable[i].allocationSize);
    }
    munmap(oldTable, oldBytes);
}



hashTable::~hashTable() { 
    if (addressTable != nullptr) {
        munmap(addressTable, numBytes);
        addressTable = nullptr;
    }

    capacity = 0;
    numBytes = 0;
}

void hashTable::insert(void* address, size_t sizeOfAllocation) {
    if(this->size >= capacity - 1) {
        this->grow(); // Grow the hash table if the size exceeds the capacity
    }
    for(int i = 0; i < this->capacity; i++) {
        if (addressTable[i].address == nullptr) {
            addressTable[i].allocationSize = sizeOfAllocation;
            addressTable[i].address = address; // Insert the address into the hash table
            addressTable[i].state = 1;
            size++; // Increment the size of the hash table
            break;
        }
    }
}

size_t hashTable::remove(void* address) {
    size_t sizeOfAllocation;
    for(int i = 0; i < this->capacity; i++) {
        if (addressTable[i].address == address) {
            addressTable[i].address = nullptr; // Remove the address from the hash table
            sizeOfAllocation = addressTable[i].allocationSize;
            addressTable[i].state = 0;
            break;
        }
    }
    this->size--; // Decrement the size of the hash table
    return sizeOfAllocation;
}

void* hashTable::getAddress(int location) {
    return addressTable[location].address;
}

size_t hashTable::getSize() {
    return this->size;
}

size_t hashTable::getCapacity() {
    return this->capacity;
}