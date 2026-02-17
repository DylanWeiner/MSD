// By Dylan Weiner
// Unix Shell
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
        cout << "$ ";
        vector<string> tokens = tokenize(lineInput); // used to mark each token in the line passed into the command shell.
        allCommands = getCommands(tokens); // Converts tokens into a readable format for parsing a vector of commands.
        runCommands(allCommands);
    }
    return 0;
}



