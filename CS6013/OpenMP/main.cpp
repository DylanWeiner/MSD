#include "parallelReduction.h"
#include <fstream>
#include <cmath>

// template<typename T>
int main(int argc, char* argv[]) {
    std::ofstream file_multi("multithreaded.csv");
    std::ofstream file_custom("custom_openmp.csv");
    std::ofstream file_builtin("builtin_openmp.csv");
    file_multi << "Num Values,Time\n"; // Header
    file_custom << "Num Values,Time\n"; // Header
    file_builtin << "Num Values,Time\n"; // Header

    for(int i = 1; i <= 16; i ++) {
        size_t N = 1000; // Size of the array
        size_t num_threads = i; // Number of threads to use
        double* a = new double[N]; // Allocate array

        // Initialize the array with some values (for example, 1.0)
        for (size_t i = 0; i < N; ++i) {
            a[i] = 1.0;
        }

        // Call the parallel sum function using std::thread
        auto result_std = parallel_sum_std(a, N, num_threads);
        std::cout << "Parallel sum using multithreaded addition: " << num_threads 
                << " threads in " << result_std.second << " ticks." << std::endl;

        file_multi << num_threads << "," << result_std.second << "\n";
        

        // Call the parallel sum function using OpenMP
        auto result_openmp = parallel_sum_openmp(a, N, num_threads);
        std::cout << "Parallel sum using OpenMP custom reduction: " << num_threads
                << " threads in " << result_openmp.second << " ticks." << std::endl;
        file_custom << num_threads << "," << result_openmp.second << "\n";

        auto result_openmp_builtin = parallel_sum_openmp_builtin(a, N, num_threads);
        std::cout << "Parallel sum using OpenMP builtin reduction: " << num_threads
                << " threads in " << result_openmp_builtin.second << " ticks." << std::endl;
        file_builtin << num_threads << "," << result_openmp_builtin.second << "\n";
        std::cout << "---------------------------------------------" << std::endl;
        // Clean up
        delete[] a;
    }
    file_multi.close();
    file_custom.close();
    file_builtin.close();
    return 0;
}