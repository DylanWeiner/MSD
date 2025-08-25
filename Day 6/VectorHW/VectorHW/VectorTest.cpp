/*
  Author: Daniel Kopta and ??
  July 2017
  
  CS 6010 Fall 2019
  Vector util library tests

  Compile this file together with your VectorUtil library with the following command:
  clang++ -std=c++11 VectorTest.cpp VectorUtil.cpp

  Most of the provided tests will fail until you have provided correct
  implementations for the VectorUtil library functions.

  You will need to provide more thorough tests.
*/

#include <iostream>
#include <string>

// Include the VectorUtil library
#include "VectorUtil.h"

/*
 * Helper function for failing a test.
 * Prints a message and exits the program.
 */
void ErrorExit( std::string message )
{
  std::cerr << "Failed test: " << message << std::endl;
  exit(1); // Causes the entire program to exit.
}


int main()
{
    
    // Set up some input vectors for testing purposes.
    
    // We can use list initialization syntax:
    vector<int> v1 = {3, 1, 0, -1, 5};
    
    // Or we can repeatedly push_back items
    vector<int> v2;
    v2.push_back(1);
    v2.push_back(2);
    v2.push_back(3);
    
    vector<int> v3;
    int value = 0;
    
    std::string uVal;
    
    std::cout << "Please input any value to run tests on the numbers you input: " << std::endl;
    
    while(std::getline(std::cin, uVal)) {
        
        if(IsDone(uVal)) {
            break;
        }
        
        //std::cout<< std::stoi(uVal);
        
        else if(IsDone(uVal) == false) {
            value = std::stoi(uVal);
            v3.push_back(value);
        }
    }
        // When testing, be sure to check boundary conditions, such as an empty vector
        vector<int> empty;
        
        
        /*
         * Contains tests
         */
        
        // v1 doesn't contain 4, so this should return false
        if( Contains(v1, 4) ) {
            ErrorExit( "Contains() - test 1" );
        }
        
        // v1 does contain -1, so this should return true
        if(!Contains(v1, -1)) {
            ErrorExit("Contains() - test 2");
        }
        
        /*
         * The vector 'empty' doesn't contain anything, so this should return false
         * The specific value we're looking for here (99) is not important in this test.
         * This test is designed to find any general errors caused by the array being empty.
         * That type of error is unlikely to depend on the value we are looking for.
         */
        if( Contains(empty, 99) ) {
            ErrorExit("Contains() - empty");
        }
        
        else {
            std::cout << "The smallest value in vector 1 is: " << FindMin(v1) << std::endl;
            
            std::cout << "The smallest value in vector 2 is: " << FindMin(v2) << std::endl;
            
            std::cout << "The smallest value in vector 3 is: " << FindMin(v3) << std::endl;
            
            std::cout << "The largest value in vector 1 is: " << FindMax(v1) << std::endl;
            
            std::cout << "The largest value in vector 2 is: " << FindMax(v2) << std::endl;
            
            std::cout << "The largest value in vector 2 is: " << FindMax(v3) << std::endl;
            
            std::cout << "The average value in vector 1 is: " << Average(v1) << std::endl;
            
            std::cout << "The average value in vector 2 is: " << Average(v2) << std::endl;
            
            std::cout << "The average value in vector 2 is: " << Average(v2) << std::endl;
            
            if(IsSorted(v1)) {
                std::cout << "Vector 1 is sorted lowest to highest!" << std::endl;
            }
            else {
                std::cout << "Vector 1 is not sorted lowest to highest!" << std::endl;
            }
            
            if(IsSorted(v2)) {
                std::cout << "Vector 2 is sorted lowest to highest!" << std::endl;
            }
            else {
                std::cout << "Vector 2 is not sorted lowest to highest!" << std::endl;
            }
            
            if(IsSorted(v3)) {
                std::cout << "Vector 3 is sorted lowest to highest!" << std::endl;
            }
            else {
                std::cout << "Vector 3 is not sorted lowest to highest!" << std::endl;
            }
            
        }
        // TODO: Add your own tests that thoroughly exercise your VectorUtil library.
        
        // Since any failed test exits the program, if we made it this far, we passed all tests.
        std::cout << "All tests passed!\n";
        
    }

