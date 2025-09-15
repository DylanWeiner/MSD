cmake_minimum_required(VERSION 3.5)

project(testSFML)

add_executable(testSFML Testing.cpp)

find_package(SFML 3.0.1 COMPONENTS Graphics REQUIRED)

target_link_libraries(testSFML SFML::Graphics)
