#include "parallelReduction.h"

template<typename T>
std::pair<T, double> parallel_sum_std(T a[], size_t N, size_t num_threads) {
    if (num_threads == 0) {
        num_threads = 1; // Fallback to single thread if num_threads is zero
    }

    if (N == 0) {
        return std::make_pair(0, 0.0); // Return zero sum and zero duration for empty array
    }

    if (num_threads > N) {
        num_threads = N; // Limit the number of threads to the size of the array
    }

    std::vector<std::thread> threads;
    std::vector<T> partial_sums(num_threads, T{});
    size_t chunk_size = N / num_threads;
    std::atomic<T> total_sum = T{};
    auto start_time = std::chrono::high_resolution_clock::now();

    for (size_t i = 0; i < num_threads; ++i) {
        threads.emplace_back([&, i]() {
            size_t start = i * chunk_size;
            size_t end = (i == num_threads - 1) ? N : start + chunk_size;
            std::atomic<T> local_sum = T{};
            for (size_t j = start; j < end; ++j) {
                local_sum += a[j];
            }
            partial_sums[i] = local_sum;
        });
    }

    for (auto& t : threads) {
        t.join();
    }
    
    for (const auto& s : partial_sums) {
        total_sum += s;
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration = end_time - start_time;

    return std::make_pair(total_sum, duration.count());
}

template<typename T>
std::pair<T, double> parallel_sum_openmp(T a[], size_t N, size_t num_threads) {
    if (num_threads == 0) {
        num_threads = 1; // Fallback to single thread if num_threads is zero
    }

    if (N == 0) {
        return std::make_pair(0, 0.0); // Return zero sum and zero duration for empty array
    }

    if (num_threads > N) {
        num_threads = N; // Limit the number of threads to the size of the array
    }

    std::vector<T> partial_sums(num_threads, T{});
    auto start_time = std::chrono::high_resolution_clock::now();

    #pragma omp parallel num_threads(num_threads)
    {
        int id = omp_get_thread_num();
        size_t chunk_size = N / num_threads;
        size_t start = id * chunk_size;
        size_t end = (id == num_threads - 1) ? N : start + chunk_size;
        T local_sum = T{};
        for (size_t j = start; j < end; ++j) {
            local_sum += a[j];
        }
        partial_sums[id] = local_sum;
    }

    std::atomic<T> total_sum = T{};
    for (const auto& s : partial_sums) {
        total_sum += s;
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration = end_time - start_time;

    return std::make_pair(total_sum, duration.count());
}

template<typename T>
std::pair<T, double> parallel_sum_openmp_builtin(T a[], size_t N, size_t num_threads) {
    if (num_threads == 0) {
        num_threads = 1; // Fallback to single thread if num_threads is zero
    }

    if (N == 0) {
        return std::make_pair(0, 0.0); // Return zero sum and zero duration for empty array
    }

    auto start_time = std::chrono::high_resolution_clock::now();
    T total_sum = T{};

    #pragma omp parallel for reduction(+:total_sum) num_threads(num_threads)
    for (size_t i = 0; i < N; ++i) {
        total_sum += a[i];
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration = end_time - start_time;

    return std::make_pair(total_sum, duration.count());
}