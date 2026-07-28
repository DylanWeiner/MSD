// By Dylan Weiner
// Unix Shell from Scratch
// CS 6013
// Handles user input and passes it to the shelpers functions to be parsed and executed as desired.
// 2/13/26

#include <iostream>
#include <unistd.h>
#include <sys/wait.h>
#include <cstring>
#include "shelpers.hpp"

// Compile with "g++ -std=c++17 shelpers.cpp main.cpp -o dshell"

using namespace std;

int main() {
    vector<string> allLineInputs;
    string lineInput;
    vector<Command> allCommands;
    cout << "$ ";
    
    while(getline(cin, lineInput)) { // Collects the line passed into the shell
        int status;
        pid_t done;
        cout << "$ ";
        vector<string> tokens = tokenize(lineInput); // used to mark each token in the line passed into the command shell.
        allCommands = getCommands(tokens); // Converts tokens into a readable format for parsing a vector of commands.
        runCommands(allCommands);
        while((done = waitpid(-1, &status, WNOHANG)) > 0) {
            cout << "\n" << done << " done";
        }
    }
    return 0;
}



