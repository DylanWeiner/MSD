#include <iostream>
#include <unistd.h>

using namespace std;

void usage(char * argv[]) {
    cout << argv[0] <<": 'message'\n";
}

void childCode(int readFd) {
    cout << "Child:\n";

    // Get length
    int length;
    read(readFd, &length, 4);
    cout << "Child thinks message has a length of " << length << "\n";

    // Get actual message.
    char msg[1024]; // Creates a buffer up to 1024 bytes.
    read(readFd, msg, length);

    cout << "Child: " << msg << "\n";
    
    exit(0);
}

void parentCode(int writeFd, const string & message) { // We want to wait for the child code to finish.
    cout << "Parent:\n";

    int len = message.length() + 1;
    write(writeFd, &len, 4); // 4 bytes is an int.
    write(writeFd, message.c_str(), message.length()+1); // Add 1 to include the null terminator.

    cout << "Parent: Done printing message.\n";

    bool done = false;
    while(!done) {
        string newMsg;
        getline(cin, newMsg);
        
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
        childCode(readFd);
    }
    else { // This is the parent process.
        string msg = argv[1];
        parentCode(writeFd, msg);
    }
}

// compiles w ./lab3 'this is my message'