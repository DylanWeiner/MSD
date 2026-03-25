#include "hashTable.h"

hashTable::hashTable() { 
    this->capacity = capacity; // Initial capacity
    this->addressTable->size = addressTable->size; // Initial size
    for(int i = 0; i < capacity; i++) {
        this->addressTable->address[i] = nullptr;
    }

    this->numBytes *= this->addressTable->capacity; // Initial number of bytes allocated
    addressTable = static_cast<hTable*>(mmap(this, numBytes, PROT_READ | PROT_WRITE, MAP_ANONYMOUS | MAP_SHARED, -1, 0)); // Allocate memory for the hash table
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
    if(this->addressTable->size >= this->capacity) {
        this->addressTable->grow(); // Grow the hash table if the size exceeds the capacity
    }
    for(int i = 0; i < this->capacity; i++) {
        if (this->addressTable->address[i] == nullptr) {
            this->addressTable->address[i] = address; // Insert the address into the hash table
            break;
        }
    }
    this->addressTable->size++; // Increment the size of the hash table
}

void hashTable::remove(void* address) {
    for(int i = 0; i < this->capacity; i++) {
        if (this->addressTable->address[i] == address) {
            this->addressTable->address[i] = nullptr; // Remove the address from the hash table
            break;
        }
    }
     this->addressTable->size--; // Decrement the size of the hash table
}

void* hashTable::getAddress() {
    return this->addressTable->address;
}

size_t hashTable::getSize() {
    return this->addressTable->size;
}

size_t hashTable::getCapacity() {
    return this->addressTable->capacity;
}