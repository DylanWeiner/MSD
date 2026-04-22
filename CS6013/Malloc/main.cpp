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
    const size_t arr_size = 100;
    size_t* arr[arr_size];

    for(size_t i = 0; i < arr_size; i++) {
        arr[i] = static_cast<size_t*>(malloc(sizeof(size_t)));
        if(arr[i] != nullptr) {
            *arr[i] = i;  // Store the index as test data
        }
    }

    // Only print occupied entries (filter by state)
    size_t occupied = 0;
    std::cout << "Allocated Addresses:\n";
    for(size_t i = 0; i < myMal.allocatedBlocks.getCapacity(); i++) {
        if(myMal.allocatedBlocks.getState(i) == State::Occupied) {
            occupied++;
            std::cout << "  Address " << occupied << ": " << myMal.allocatedBlocks.getAddress(i) 
                      << " (size: " << myMal.allocatedBlocks.getSize() << ")\n";
        }
    }
    std::cout << "Total Occupied Slots: " << occupied << std::endl;

    // Verify data integrity
    bool dataOk = true;
    for(size_t i = 0; i < arr_size; i++) {
        if(arr[i] != nullptr && *arr[i] != static_cast<size_t>(i)) {
            dataOk = false;
            std::cout << "Data corruption at index " << i << std::endl;
        }
    }
    std::cout << "Data integrity check: " << (dataOk ? "PASSED" : "FAILED") << std::endl;

    // Deallocate all
    for(size_t i = 0; i < arr_size; i++) {
        free(arr[i]);
    }
}

int main() {
    std::cout << "Enters main." << std::endl;
    smallObjLargeArr();
    return 0;
}