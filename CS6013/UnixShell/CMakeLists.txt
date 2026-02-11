cmake_minimum_required(VERSION 3.10.0)
project(UnixShell VERSION 0.1.0 LANGUAGES C CXX)

add_executable(UnixShell main.cpp shelpers.cpp)

include(CTest)
enable_testing()

