// By Dylan Weiner
// 1/13/26
// CS 6015
// Project Phase 1: MSDscript Command Line
#include <iostream>
#include "cmdline.h"
#define CATCH_CONFIG_RUNNER
#include "catch.h"
// #define CATCH_CONFIG_RUNNER


void use_arguments(int argc, char **argv) {
    // Catch::Session() = 1;
    bool testsSeen = false; // Tells the program we haven't run tests.
    for(int i = 1; i < argc; i++) {
        if(argc == 1) {
            std::cout << "ERROR: There was no argument provided on the commandline.\n";
        } // Checks if there is a word on the commandline and states whether or not there are enough arguments.
        
        std::string arg = (std::string) (argv[i]); // casts each argv to a string.
        if(arg == "--help") {
            std::cout << "msdscript has the functions: \n    --help\n    --test\n";
        } // Prints out help statement.
        else if(arg == "--test") {
            if(Catch::Session().run(1, argv) == 1) {
                std::cerr << "Tests failed ::( \n";
                exit(1); // exits with an error message.
            } // fails the test if it is written more than once.
            else { // if(!testsSeen)
                std::cout << "Tests passed! ::)";
                testsSeen = true; // sets test seen to true
            } // passes the test if it has never been run.
        }
        else {
            std::cerr << "This argument is not recognized.";
            exit(1); // exits with an error message if there is an argument not previously included.
        }
    }
    // return 0; // exits the function.
}