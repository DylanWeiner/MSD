#include "MyMalloc.h"

MyMalloc * myMal = new MyMalloc;

void* malloc(size_t capacity) {
    // std::cout << "Enters malloc" << std::endl;
    return myMal->allocate(capacity);
}

void free(void* address) {
    myMal->deallocate(address);
}

void smallObjLargeArr() {
    // std::cout << "Enters smallObjLargeArr." << std::endl;
    int* arr[10];

    for(int i = 0; i < 10; i ++) {
        // std::cout << "for loop: " << i << std::endl;
        arr[i] = static_cast<int*>(malloc(sizeof(char)));
        
        std::cout << arr[i];
    }
    // std::cout << "Finishes construction." << std::endl;

    for(int i = 0; i < 10; i++) {
        std::cout << "Address " << i << ": " << myMal->allocatedBlocks->getAddress(i) << std::endl;
    }

    std::cout << "Finishes reading addresses" << std::endl;
    // Allocate a large number of small memory objects
    //     Manipulate the data in those objects
    //     Create more objects
    //     Verify the data in the objects stays valid
}

int main(int argc, char **argv) {
    std::cout << "Enters main." << std::endl;
    smallObjLargeArr();
    // Allocate a large number of large memory objects
    //     Test them
    // Deallocate your objects
    //     Verify MyMalloc / hash table are correct
    // Allocate small and large blocks of memory
    // Time how long it takes to malloc() and free() memory.  Note, if you have overloaded malloc() and free() (to allocate() and deallocate()), these times will be for your version.  Un-overload them when timing the system malloc() and free().
    // Come up with more tests...
    return 0;
}