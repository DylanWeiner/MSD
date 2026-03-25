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

void hashTable::insert(void* address, size_t size) {
    this->addressTable->address[0] = address;
    this->addressTable->size = size;
}

void hashTable::remove(void* address) {
    // Implementation for removing an entry from the hash table
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

