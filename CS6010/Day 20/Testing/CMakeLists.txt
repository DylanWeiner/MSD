cmake_minimum_required(VERSION 3.5)

project(testSFML)

add_executable(testSFML src/Deck.cpp src/Deck.h src/CardTests.cpp)

find_package(SFML 3.0.1 COMPONENTS Graphics REQUIRED)

target_link_libraries(testSFML SFML::Graphics)

file(GLOB ASSETS "*.png" "*.jpg" "*.jpeg")
foreach(ASSET ${ASSETS})
	configure_file(${ASSET}) $
{CMAKE_BINARY_DIR} / COPYONLY)
endforeach()