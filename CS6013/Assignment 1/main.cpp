#include <sys/time.h>
#include <iostream>

extern "C" {
  void sayHello();

  int myPuts(const char* s, int len);

  timeval myGTOD();
}

int main() {
    sayHello(); // Prints the assembly value with "puts" correctly.
    myPuts("hello", 6); // Takes the input and prints it correctly.
    std::cout << "\n" << myGTOD().tv_sec << " seconds \n" << myGTOD().tv_usec << " microseconds.\n";
    // Gave 1769199616 seconds and 395223 microseconds which adds up to the year 2026 and is correct.
    // It also counts up correctly and consistently.
};