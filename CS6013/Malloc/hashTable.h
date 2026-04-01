#include <cstddef>
#include <sys/mman.h> 

struct HashEntry {
    void* address;
    size_t allocationSize;
    int state; // 0 = empty, 1 = occupied
};

class hashTable {
public:
    hashTable();
    ~hashTable();
    // hashTable(int capacity);
    void* getAddress(int location);
    size_t getSize();
    size_t getCapacity();
    void insert(void* address, size_t sizeOfAllocation);
    size_t remove(void* address);
    
private:
    size_t size;
    size_t capacity;
    size_t numBytes = sizeof(HashEntry);
    HashEntry * addressTable;
    void grow();
    int find(void* desiredAddress);
};