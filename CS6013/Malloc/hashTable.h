#include <cstddef>
#include <sys/mman.h> 

struct hTable {
    void* address[100];
    size_t size;
    size_t capacity;

    int find(void* desiredAddress) {
        for(int i = 0; i < size; i++) {
            if (address[i] == desiredAddress) {
                int* ptr = (int*)address[i];
                return *ptr;
            } //This might be wrong
        }
        return -1; // Address not found
    }

    hTable grow() {
        hTable newTable;
        newTable.capacity = capacity * 2;
        // newTable.address[newTable.capacity];
        newTable.size = size;
        for(int i = 0; i < size; i++) {
            newTable.address[i] = address[i];
        }
        return newTable;
    }
};

class hashTable {
public:
    hashTable();
    ~hashTable();
    void* getAddress(int location);
    size_t getSize();
    size_t getCapacity();
    void insert(void* address, size_t sizeOfAllocation);
    void remove(void* address);
    
private:
    size_t capacity;
    size_t numBytes = sizeof(hTable);
    hTable * addressTable;
};