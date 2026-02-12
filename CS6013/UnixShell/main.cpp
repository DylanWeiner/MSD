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
        // for(Command com : allCommands) { // Separates out commands from vector.
        //     for(int i = 0; i < com.argv.size(); i++) { // Allows for parsing arguments within the command.
        //         const char * token = com.argv[i];
        //         // if(com.argv[i][0] == '|' ) { // This does the same thing as the line below.
        //         if( strcmp( token, "|" ) ) {
        //             if (pipe(fds) < 0) { // This means something is wrong when trying to pipe.
        //                 perror("pipe error");
        //                 exit(EXIT_FAILURE);
        //             }
        //         }
        //     }
        // }
        // for(Command com : allCommands) {
        //     pid_t pID = fork();
        //     if(pID < 0) { // This means something is wrong when trying to fork.
        //         perror("fork error");
        //         exit(EXIT_FAILURE);
        //     }
        //     if(pID == 0) { // This marks the child process.
        //         execvp(com.argv[0], const_cast<char *const*>(com.argv.data()));
        //         perror("execvp failed!");
        //         // close(fds[1]);
        //     }
        //     else {
        //         int status;
        //         waitpid(pID, &status, 0);

        //         if (WIFEXITED(status)) {
        //             printf("Parent: Child exited with status %d\n", WEXITSTATUS(status));
        //         } else {
        //             printf("Parent: Child terminated abnormally\n");
        //         }
        //     }
        // }
    }
    return 0;
}



