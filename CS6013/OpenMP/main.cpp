#include "parallelReduction.h"

template<typename T>
int main(int argc, char* argv[]) {
    for(int i = 1; i < 16; i ++) {
        size_t N = 1000; // Size of the array
        size_t num_threads = i; // Number of threads to use
        T* a = new T[N]; // Allocate array

        // Initialize the array with some values (for example, 1.0)
        for (size_t i = 0; i < N; ++i) {
            a[i] = static_cast<T>(1.0);
        }

        // Call the parallel sum function using std::thread
        auto result_std = parallel_sum_std(a, N, num_threads);
        std::cout << "Parallel sum using std::thread: " << result_std.first 
                << " computed in " << result_std.second << " seconds." << std::endl;

        // Call the parallel sum function using OpenMP
        auto result_openmp = parallel_sum_openmp(a, N, num_threads);
        std::cout << "Parallel sum using OpenMP: " << result_openmp.first 
                << " computed in " << result_openmp.second << " seconds." << std::endl;

        auto result_openmp_builtin = parallel_sum_openmp_builtin(a, N, num_threads);
        std::cout << "Parallel sum using OpenMP builtin reduction: " << result_openmp_builtin.first 
                << " computed in " << result_openmp_builtin.second << " seconds." << std::endl;

        // Clean up
        delete[] a;
    }
}