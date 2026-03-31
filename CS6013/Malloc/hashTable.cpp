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

hashTable::~hashTable() { 
    this->numBytes *= this->addressTable->capacity; // Number of bytes allocated
    for(int i = 0; i < capacity; i++) {
        if (this->addressTable->address[i] != nullptr) {
            
            munmap(this->addressTable->address[i], this->addressTable->size); // Deallocate memory for each address in the hash table
        }
    }

    this->capacity = 0;
    this->addressTable->size = 0;

    munmap(addressTable, numBytes); // Deallocate memory for the hash table
}

void hashTable::insert(void* address, size_t sizeOfAllocation) {
    write(1, "Enters insert\n", 15);
    if(this->addressTable->size >= addressTable->capacity) {
        write(1, "Enters grow\n", 13);
        this->addressTable->grow(); // Grow the hash table if the size exceeds the capacity
    }
    // std::cout << "Checked insert size" << std::endl;
    for(int i = 0; i < this->capacity; i++) {
        // std::cout << "Enters insert for loop: " << i << std::endl;
        if (this->addressTable->address[i] == nullptr) {
            // std::cout << "Enters insert if statement" << std::endl;
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