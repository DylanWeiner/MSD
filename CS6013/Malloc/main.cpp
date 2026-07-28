#include "MyMalloc.h"

MyMalloc myMal;

void* malloc(size_t capacity) {
    // std::cout << "Enters malloc" << std::endl;
    return myMal.allocate(capacity);
}

void free(void* address) {
    myMal.deallocate(address);
}

void smallObjLargeArr() {
    int arr_size = 1000;
    int* arr[arr_size];

    for(int i = 0; i < arr_size; i ++) {
        arr[i] = static_cast<int*>(malloc(sizeof(char)));
    }

    for(int i = 0; i < arr_size; i++) {
        std::cout << "Address " << i+1 << ": " << myMal.allocatedBlocks.getAddress(i) << std::endl;
    }

    std::cout << "Finishes reading addresses" << std::endl;
}

int main() {
    std::cout << "Enters main." << std::endl;
    smallObjLargeArr();
    return 0;
}