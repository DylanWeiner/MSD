#include "hashTable.h"
#include <iostream>
#include <unistd.h>

hashTable::hashTable() { 
    this->capacity = 100; // Initial capacity
    this->numBytes = sizeof(hTable); // Initial number of bytes allocated
    
    addressTable = static_cast<hTable*>(mmap(this, numBytes, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0)); // Allocate memory for the hash table

    if (addressTable == MAP_FAILED) {
        addressTable = nullptr;
        capacity = 0;
        numBytes = 0;
        return;
    } // Catches in case of memory mapping failure

    addressTable->size = 0; // Initial size
    addressTable->capacity = capacity;

    for(int i = 0; i < addressTable->capacity; i++) {
        addressTable->address[i] = nullptr;
        addressTable->allocationSize[i] = 0;
    }
}

// hashTable::hashTable(int capacity) { 
//     this->capacity = 100; // Initial capacity
//     this->numBytes = sizeof(hTable); // Initial number of bytes allocated
    
//     addressTable = static_cast<hTable*>(mmap(this, numBytes, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0)); // Allocate memory for the hash table

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

hashTable::~hashTable() { 
    if (addressTable != nullptr) {
        munmap(addressTable, numBytes);
        addressTable = nullptr;
    }

    capacity = 0;
    numBytes = 0;
}

void hashTable::insert(void* address, size_t sizeOfAllocation) {
    if(this->addressTable->size >= addressTable->capacity-1) {
        *addressTable = this->addressTable->grow(); // Grow the hash table if the size exceeds the capacity
    }
    for(int i = 0; i < this->capacity; i++) {
        if (this->addressTable->address[i] == nullptr) {
            this->addressTable->allocationSize[i] = sizeOfAllocation;
            this->addressTable->address[i] = address; // Insert the address into the hash table
            this->addressTable->size++; // Increment the size of the hash table
            break;
        }
    }
}

size_t hashTable::remove(void* address) {
    size_t sizeOfAllocation;
    for(int i = 0; i < this->capacity; i++) {
        if (this->addressTable->address[i] == address) {
            this->addressTable->address[i] = nullptr; // Remove the address from the hash table
            sizeOfAllocation = addressTable->allocationSize[i];
            break;
        }
    }
    this->addressTable->size--; // Decrement the size of the hash table
    return sizeOfAllocation;
}

void* hashTable::getAddress(int location) {
    return this->addressTable->address[location];
}

size_t hashTable::getSize() {
    return this->addressTable->size;
}

size_t hashTable::getCapacity() {
    return this->addressTable->capacity;
}