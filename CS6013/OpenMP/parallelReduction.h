#include <thread>
#include <vector>
#include <iostream>
#include <utility>
#include <atomic>
#include <chrono>

#pragma once

template<typename T>
std::pair<T, double> parallel_sum_std(T a[], size_t N, size_t num_threads);

template<typename T>
std::pair<T, double> parallel_sum_openmp(T a[], size_t N, size_t num_threads);

template<typename T>
std::pair<T, double> parallel_sum_openmp_builtin(T a[], size_t N, size_t num_threads);