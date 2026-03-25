cmake_minimum_required(VERSION 3.10.0)
project(MSDScript VERSION 0.1.0 LANGUAGES C CXX)

add_executable(MSDScript cmdline.cpp expr.cpp main.cpp parse.cpp)

include(CTest)
enable_testing()

