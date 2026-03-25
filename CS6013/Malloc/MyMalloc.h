#include <cstddef>
#include <unordered_map>
#include <iostream>
class MyMalloc {
public:
    std::unordered_map<void*, size_t> allocatedBlocks;
    size_t bytesToAllocate;
    void* address;
    void* malloc(size_t bytesToAllocate);
    void* allocate(size_t bytesToAllocate);
    void deallocate(void*  ptr);
    void* insert();
    void remove();
private:
    void* find();
    void*  grow();
};
