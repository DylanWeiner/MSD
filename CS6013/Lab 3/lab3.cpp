// By Dylan Weiner
// This program demonstrates inter-process communication using pipes and signals in C++.
// 2/3/26
// CS 6013

#include <iostream>
#include <unistd.h>
#include <signal.h>

using namespace std;

volatile bool childAcknowledged = false;

void signalHandler(int sig) {
    if(sig == SIGUSR1) {
        childAcknowledged = true;
    }
}

void usage(char * argv[]) {
    cout << argv[0] <<": 'message'\n";
}

void childCode(int readFd, pid_t parentPid) {
    cout << "Child:\n";

    bool done = false;
    while(!done) {
        // Get length
        int length;
        read(readFd, &length, 4);
        
        if(length == -1) { // Special signal to quit
            done = true;
            cout << "Child: Received quit signal\n";
            break;
        }
        
        cout << "Child thinks message has a length of " << length << "\n";

        // Get actual message.
        char msg[1024]; // Creates a buffer up to 1024 bytes.
        read(readFd, msg, length);

        cout << "Child: " << msg << "\n";
        
        // Send acknowledgement signal to parent
        kill(parentPid, SIGUSR1);
    }
    
    exit(0);
}

void parentCode(int writeFd, const string & message, pid_t childPid) { // We want to wait for the child code to finish.
    cout << "Parent:\n";

    int len = message.length() + 1;
    write(writeFd, &len, 4); // 4 bytes is an int.
    write(writeFd, message.c_str(), message.length()+1); // Add 1 to include the null terminator.

    cout << "Parent: Done printing message.\n";

    bool done = false;
    while(!done) {
        string newMsg;
        getline(cin, newMsg);
        
        if(newMsg == "quit") {
            // Send quit signal to child
            int quitSignal = -1;
            write(writeFd, &quitSignal, 4);
            done = true;
        } else {
            // Write the new message to child
            int len = newMsg.length() + 1;
            write(writeFd, &len, 4);
            write(writeFd, newMsg.c_str(), newMsg.length() + 1);
            
            // Wait for acknowledgement from child
            childAcknowledged = false;
            while(!childAcknowledged) {
                pause(); // Wait for signal
            }
            cout << "Parent: Child acknowledged.\n";
        }
    }

    int status;
    wait(&status); // Waits for child to finish
    // Passes 'status' by reference to see if child is finished. Only works bc there's one child.
    // Can use waitpid w/ child process otherwise.
    cout << "Parent: child process is done.";
    exit(0);
}

int main(int argc, char * argv[]) {
    if(argc != 2) {
        usage(argv);
        exit(1);
    }

    //Pipe before we fork so both the child and parent have access.
    int fds[2]; // Has an input and an output. <- fds is actually an int * so it is already a pointer.
    int rx = pipe(fds); // Don't need an & to point to fds.

    int readFd = fds[0]; // Renamed fds for readability.
    int writeFd = fds[1];

    int rc = fork();

    if(rc < 0) {
        // Fork returns a bad exit code and failed
        cout << "Fork failed! Reason: \n";
        perror("Fork failed: "); // If this fails it sets the global variable named "errno" to the correct error.
        exit(1);
    }
    else if(rc == 0) { // 0 Marks the child process.
        childCode(readFd, getppid());
    }
    else { // This is the parent process.
        // Set up signal handler for acknowledgements
        signal(SIGUSR1, signalHandler);
        
        string msg = argv[1];
        parentCode(writeFd, msg, rc);
    }
}

// compiles w ./lab3 'this is my message'