#include <iostream>
#include <vector>
#include <chrono>
#include <cstring>
#include <cassert>
#include <algorithm>
#include <set>
#include "MyMalloc.h"

// Use system malloc/free for comparison
#define SYSTEM_MALLOC(size) ::malloc(size)
#define SYSTEM_FREE(ptr) ::free(ptr)

// Test results tracking
struct TestResult {
    std::string name;
    bool passed;
    std::string message;
};

std::vector<TestResult> results;

void logTest(const std::string& name, bool passed, const std::string& message = "") {
    results.push_back({name, passed, message}); // Stores the description for the test summary
    std::string status = passed ? "PASS" : "FAIL"; // Simple pass/fail status
    std::cout << status << ": " << name; // Log the test result
    if (!message.empty()) {
        std::cout << " (" << message << ")";
    } // Logs additional test message if provided
    std::cout << std::endl; // newline
}

// UNIT TESTS

void testBasicAllocationDeallocation() {
    std::cout << "\n--- Basic Allocation/Deallocation Tests ---\n";
    
    // Test single allocation
    void* ptr1 = malloc(64);
    bool test1 = ptr1 != nullptr;
    logTest("Single allocation returns non-null", test1);
    
    // Test deallocation
    free(ptr1); // Frees the allocated memory
    bool test2 = true; // If we get here without crashing, it passed
    logTest("Single deallocation doesn't crash", test2); 
    
    // Test multiple allocations
    std::vector<void*> ptrs; // Creates vector of pointers to store allocated addresses
    for (int i = 0; i < 10; i++) {
        ptrs.push_back(malloc(256));
    } // Allocates 10 blocks of memory and stores their addresses
    bool test3 = std::all_of(ptrs.begin(), ptrs.end(), [](void* p) { return p != nullptr; }); // Checks all allocations returned non-null
    logTest("Multiple allocations return non-null", test3);
    
    // Test all addresses are different
    std::set<void*> uniquePtrs(ptrs.begin(), ptrs.end()); // Creates a set of unique addresses from the allocated pointers
    bool test4 = uniquePtrs.size() == ptrs.size();
    logTest("All allocated addresses are unique", test4, 
            test4 ? "" : "Got " + std::to_string(uniquePtrs.size()) + 
                         " unique out of " + std::to_string(ptrs.size())); // Ensures hashed addresses are unique
    
    // Deallocate all
    for (void* p : ptrs) {
        free(p); // Frees each allocated block
    }
    logTest("Multiple deallocations don't crash", true);
}

void testNoOverlappingAllocations() {
    std::cout << "\n--- No Overlapping Allocations Test ---\n";
    
    const int numAllocations = 100;
    const size_t allocSize = 4096; // Size of each allocation (1 page)
    std::vector<std::pair<void*, size_t>> allocations; // Vector to store allocated addresses and their sizes
    
    // Allocate blocks
    for (int i = 0; i < numAllocations; i++) {
        void* ptr = malloc(allocSize); // Allocates a block of memory of size allocSize
        allocations.push_back({ptr, allocSize}); // Adds the allocated address and its size to the vector
    }
    
    // Check for overlaps
    bool noOverlaps = true;
    for (size_t i = 0; i < allocations.size() && noOverlaps; i++) {
        for (size_t j = i + 1; j < allocations.size(); j++) {
            uintptr_t addr1 = reinterpret_cast<uintptr_t>(allocations[i].first);
            uintptr_t addr2 = reinterpret_cast<uintptr_t>(allocations[j].first);  // Converts the pointers to integers for easier comparison
            size_t size1 = allocations[i].second;
            size_t size2 = allocations[j].second; // Retrieves the sizes of the allocated blocks
            
            // Check if blocks overlap
            bool overlap = (addr1 < addr2 + size2) && (addr2 < addr1 + size1);
            if (overlap) {
                noOverlaps = false;
                std::cout << "  Overlap detected: [" << (void*)addr1 << ", " 
                          << (void*)(addr1 + size1) << ") with ["
                          << (void*)addr2 << ", " << (void*)(addr2 + size2) << ")\n"; // Logs the overlapping addresses and their ranges
                break;
            } // This logs the exact overlaps
        }
    }
    
    logTest("No overlapping allocations", noOverlaps, 
            "Checked " + std::to_string(numAllocations) + " allocations"); // Logs the number of allocations checked for overlaps
    
    // Cleanup
    for (auto& alloc : allocations) {
        free(alloc.first);
    }
}

void testDataIntegrity() {
    std::cout << "\n--- Data Integrity Test ---\n";
    
    const int numBlocks = 50;
    std::vector<void*> ptrs;
    
    // Allocate and write data
    for (int i = 0; i < numBlocks; i++) {
        size_t size = 1024 * (i + 1); // Each block is a multiple of 1KB, increasing in size
        void* ptr = malloc(size); // Allocates a block of memory of size 'size'
        ptrs.push_back(ptr); // Stores the allocated pointer for later verification
        
        // Write pattern
        unsigned char* data = static_cast<unsigned char*>(ptr); // Casts the allocated pointer to a byte array for writing
        for (size_t j = 0; j < size; j++) {
            data[j] = static_cast<unsigned char>(i % 256);
        } // Writes a pattern to the allocated block (the value is based on the block index to create distinct patterns for each block)
    }
    
    // Verify data integrity
    bool dataIntact = true;
    for (int i = 0; i < numBlocks; i++) {
        size_t size = 1024 * (i + 1); // Retrieves the size of the block based on the index
        unsigned char* data = static_cast<unsigned char*>(ptrs[i]); // Casts the allocated pointer to a byte array for reading
        
        for (size_t j = 0; j < size; j++) {
            if (data[j] != static_cast<unsigned char>(i % 256)) {
                dataIntact = false;
                std::cout << "  Data corruption at block " << i << ", offset " << j << "\n";
                break;
            } // Logs the first instance of data corruption if found
        } // Data corruption is where the data read back does not match the pattern that was written, indicating that the memory may have been overwritten or corrupted
        if (!dataIntact) break;
    }
    
    logTest("Data integrity maintained across allocations", dataIntact);
    
    // Cleanup
    for (void* ptr : ptrs) {
        free(ptr);
    }
}

void testHashTableMetadata() {
    std::cout << "\n--- Hash Table Metadata Test ---\n";
    
    MyMalloc testMal;
    
    // Test initial state
    bool test1 = testMal.allocatedBlocks.getSize() == 0;
    logTest("Hash table initially empty", test1);
    
    // Test size increases
    std::vector<void*> ptrs;
    for (int i = 0; i < 20; i++) {
        ptrs.push_back(testMal.allocate(1024));
    }
    
    bool test2 = testMal.allocatedBlocks.getSize() == 20;
    logTest("Hash table size increases with allocations", test2,
            "Expected 20, got " + std::to_string(testMal.allocatedBlocks.getSize()));
    
    // Test size decreases
    testMal.deallocate(ptrs[0]);
    bool test3 = testMal.allocatedBlocks.getSize() == 19;
    logTest("Hash table size decreases with deallocations", test3,
            "Expected 19, got " + std::to_string(testMal.allocatedBlocks.getSize()));
    
    // Cleanup
    for (size_t i = 1; i < ptrs.size(); i++) {
        testMal.deallocate(ptrs[i]);
    }
}

void testRepeatedAllocDealloc() {
    std::cout << "\n--- Repeated Alloc/Dealloc Test (Fragmentation) ---\n";
    
    const int iterations = 100;
    bool success = true;
    
    for (int iter = 0; iter < iterations; iter++) {
        std::vector<void*> ptrs;
        
        // Allocate various sizes
        for (int i = 0; i < 20; i++) {
            size_t size = (i + 1) * 256;
            void* ptr = malloc(size);
            if (ptr == nullptr) {
                success = false;
                std::cout << "  Allocation failed at iteration " << iter << "\n";
                break;
            } // This test ensures that removed value's space is reused and doesn't cause fragmentation that leads to allocation failures
            ptrs.push_back(ptr);
        }
        
        // Free in different order
        for (int i = ptrs.size() - 1; i >= 0; i--) {
            free(ptrs[i]);
        }
        
        if (!success) break;
    }
    
    logTest("Repeated alloc/dealloc cycles", success,
            std::to_string(iterations) + " iterations completed");
}

// ============================================================================
// MICROBENCHMARKS
// ============================================================================

struct BenchmarkResult {
    std::string name;
    double customTime;
    double systemTime;
    double ratio;
};

std::vector<BenchmarkResult> benchmarks;

void benchmarkAllocations(int numAllocations, size_t allocationSize) {
    std::cout << "\n--- Benchmark: " << numAllocations << " allocations of " 
              << allocationSize << " bytes ---\n";
    
    // Benchmark custom malloc
    auto start = std::chrono::high_resolution_clock::now(); // Start timing
    std::vector<void*> customPtrs(numAllocations); // Vector to store allocated pointers for later deallocation
    for (int i = 0; i < numAllocations; i++) {
        customPtrs[i] = malloc(allocationSize); // Allocates memory using the custom malloc and stores the pointer for later deallocation
    }
    auto end = std::chrono::high_resolution_clock::now(); // End timing
    double customAllocTime = std::chrono::duration<double, std::milli>(end - start).count(); // Takes the time for custom malloc in milliseconds
    
    // Deallocate
    for (void* ptr : customPtrs) {
        free(ptr);
    }
    
    // Benchmark system malloc
    start = std::chrono::high_resolution_clock::now(); // Start timing
    std::vector<void*> systemPtrs(numAllocations);
    for (int i = 0; i < numAllocations; i++) {
        systemPtrs[i] = SYSTEM_MALLOC(allocationSize);
    }
    end = std::chrono::high_resolution_clock::now(); // End timing
    double systemAllocTime = std::chrono::duration<double, std::milli>(end - start).count(); // Takes the time for system malloc in milliseconds
    
    // Deallocate
    for (void* ptr : systemPtrs) {
        SYSTEM_FREE(ptr);
    }
    
    double ratio = customAllocTime / systemAllocTime; // Calculates the ratio of custom malloc time to system malloc time
    benchmarks.push_back({
        std::to_string(numAllocations) + "x" + std::to_string(allocationSize) + "B", // Name of the benchmark based on the number of allocations and their size
        customAllocTime,
        systemAllocTime,
        ratio
    });
    
    std::cout << "  Custom malloc: " << customAllocTime << " ms\n";
    std::cout << "  System malloc: " << systemAllocTime << " ms\n";
    std::cout << "  Difference in Malloc Times: " << (customAllocTime - systemAllocTime) << " ms\n";
    std::cout << "  Ratio (custom/system): " << ratio << "x\n";
}

void benchmarkDeallocation(int numAllocations, size_t allocationSize) {
    std::cout << "\n--- Benchmark Deallocation: " << numAllocations << " deallocations ---\n";
    
    // Allocate first (custom)
    std::vector<void*> customPtrs(numAllocations); // Vector to store allocated pointers for later deallocation
    for (int i = 0; i < numAllocations; i++) {
        customPtrs[i] = malloc(allocationSize);
    }
    
    // Benchmark custom free
    auto start = std::chrono::high_resolution_clock::now(); // Start timing
    for (void* ptr : customPtrs) {
        free(ptr);
    }
    auto end = std::chrono::high_resolution_clock::now(); // End timing
    double customFreeTime = std::chrono::duration<double, std::milli>(end - start).count(); // Takes the time for custom free in milliseconds
    
    // Allocate first (system)
    std::vector<void*> systemPtrs(numAllocations);
    for (int i = 0; i < numAllocations; i++) {
        systemPtrs[i] = SYSTEM_MALLOC(allocationSize);
    }
    
    // Benchmark system free
    start = std::chrono::high_resolution_clock::now(); // Start timing
    for (void* ptr : systemPtrs) {
        SYSTEM_FREE(ptr);
    }
    end = std::chrono::high_resolution_clock::now(); // End timing
    double systemFreeTime = std::chrono::duration<double, std::milli>(end - start).count(); // Takes the time for system free in milliseconds
    
    double ratio = customFreeTime / systemFreeTime; // Calculates the ratio of custom free time to system free time
    
    std::cout << "  Custom free: " << customFreeTime << " ms\n";
    std::cout << "  System free: " << systemFreeTime << " ms\n";
    std::cout << "  Difference in Free Times: " << (customFreeTime - systemFreeTime) << " ms\n";
    std::cout << "  Ratio (custom/system): " << ratio << "x\n";
}

void benchmarkMixedOperations(int numIterations) {
    std::cout << "\n--- Benchmark Mixed Operations: " << numIterations << " iterations ---\n";
    
    // Custom malloc/free cycle
    auto start = std::chrono::high_resolution_clock::now(); // Start timing
    for (int i = 0; i < numIterations; i++) {
        void* ptr = malloc(4096);
        free(ptr);
    }
    auto end = std::chrono::high_resolution_clock::now(); // End timing
    double customTime = std::chrono::duration<double, std::milli>(end - start).count(); // Takes the time for custom malloc/free cycle in milliseconds
    
    // System malloc/free cycle
    start = std::chrono::high_resolution_clock::now(); // Start timing
    for (int i = 0; i < numIterations; i++) {
        void* ptr = SYSTEM_MALLOC(4096);
        SYSTEM_FREE(ptr);
    }
    end = std::chrono::high_resolution_clock::now(); // End timing
    double systemTime = std::chrono::duration<double, std::milli>(end - start).count(); // Takes the time for system malloc/free cycle in milliseconds
    
    double ratio = customTime / systemTime; // Calculates the ratio of custom malloc/free cycle time to system malloc/free cycle time
    
    std::cout << "  Custom malloc/free cycle: " << customTime << " ms\n";
    std::cout << "  System malloc/free cycle: " << systemTime << " ms\n";
    std::cout << "  Difference in Times: " << (customTime - systemTime) << " ms\n";
    std::cout << "  Ratio (custom/system): " << ratio << "x\n";
}

// MAIN TEST RUNNER

void printSummary() {
    std::cout << "\n" << std::string(60, '=') << "\n";
    std::cout << "TEST SUMMARY\n";
    std::cout << std::string(60, '=') << "\n";
    
    int passed = 0;
    int failed = 0;
    
    for (const auto& result : results) {
        if (result.passed) {
            passed++;
        } else {
            failed++;
        }
    }
    
    std::cout << "Total Tests: " << results.size() << "\n";
    std::cout << "Passed: " << passed << "\n";
    std::cout << "Failed: " << failed << "\n";
    std::cout << "Pass Rate: " << (100.0 * passed / results.size()) << "%\n";
    
    if (failed > 0) {
        std::cout << "\nFailed Tests:\n";
        for (const auto& result : results) {
            if (!result.passed) {
                std::cout << "  - " << result.name << "\n";
            }
        }
    }
}

int main() {
    std::cout << "========================================\n";
    std::cout << "    Custom Malloc Test Suite\n";
    std::cout << "========================================\n";
    
    // Unit Tests
    testBasicAllocationDeallocation();
    testNoOverlappingAllocations();
    testDataIntegrity();
    testHashTableMetadata();
    testRepeatedAllocDealloc();
    
    // Benchmarks
    std::cout << "\n" << std::string(60, '=') << "\n";
    std::cout << "PERFORMANCE BENCHMARKS\n";
    std::cout << std::string(60, '=') << "\n";
    
    benchmarkAllocations(100, 4096);
    benchmarkAllocations(1000, 4096);
    benchmarkAllocations(10000, 4096);
    benchmarkAllocations(100, 1048576);  // 1MB allocations
    
    benchmarkDeallocation(100, 4096);
    benchmarkDeallocation(1000, 4096);
    
    benchmarkMixedOperations(1000);
    benchmarkMixedOperations(10000);
    
    // Summary
    printSummary();
    
    return 0;
}
