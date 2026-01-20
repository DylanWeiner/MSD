
/*********************************************************************
* Dylan Weiner
* 1/12/26
* CS 6013
* Lab 1: C Warmup
* Completed Byte Sort, Nibble Sort, Name List, and Name Sort Features.
*
*/
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h> // For strlen()

/*********************************************************************
*
* The C functions in this lab use patterns and functionality often
* found in operating system code. Your job is to implement them.
* Additionally, write some test cases for each function (stick them in
* functions [called xyzTests(), etc or similar]) Call your abcTests(),
* etc functions in main().
*
* Write your own tests for each function you are testing, then
* share/combine tests with a classmate. Try to come up with tests that
* will break people's code! Easy tests don't catch many bugs! [Note
* this is one specific case where you may share code directly with
* another student. The test function(s) from other students must be
* clearly marked (add '_initials' to the function name) and commented
* with the other student's name.
*
* Note: you may NOT use any global variables in your solution.
*
* Errata:
* - You can use global vars in your testing functions (if necessary).
* - Don't worry about testing the free_list(), or the draw_me() functions.
*
* You must compile in C mode (not C++). If you compile from the
* commandline run clang, not clang++. Submit your solution files on
* Canvas.
*
*********************************************************************/

/*********************************************************************
*
* byte_sort()
*
* specification: byte_sort() treats its argument as a sequence of
* 8 bytes, and returns a new unsigned long integer containing the
* same bytes, sorted numerically, with the smaller-valued bytes in
* the lower-order byte positions of the return value
*
* EXAMPLE: byte_sort (0x0403deadbeef0201) returns 0xefdebead04030201
* Ah, the joys of using bitwise operators!
*
* Hint: you may want to write helper functions for these two functions
*
*********************************************************************/



uint8_t* arg_position( unsigned long arg, int byte_size) {
    uint8_t val;
    unsigned long mask = 0xFF;
    uint8_t *allVals = malloc(byte_size);
    for(int i = 0; i < byte_size; i++) {
        val = mask&(arg >> i * 8);
        allVals[i] = val;
    }
    return allVals;
} 

uint8_t* list_sort(uint8_t allVals[], int byte_size) {
    for(int i = 0; i < byte_size-1; i++) {
        // printf("0x%X", allVals[i]);
        // printf("\n");
        int min = i;
        for(int j = i+1; j < byte_size; j++) {
            if(allVals[j] < allVals[min]) {
                min = j;
            }
        }
            if(min != i) {
                uint8_t temp = allVals[min];
                allVals[min] = allVals[i];
                allVals[i] = temp;
            }
    }
    // printf("\n");
    return allVals;
}

unsigned long byte_sort( unsigned long arg ) {
    unsigned long sorted = 0x0;

    uint8_t *allVals = list_sort(arg_position(arg, sizeof(arg)), sizeof(arg));

    for(int i = 0; i < sizeof(arg); i++) {
        // printf("0x%X", allVals[i]);
        // printf("\n");
        sorted |= (((unsigned long) allVals[i]) << i * 8);
    }
    return sorted;
}

/*********************************************************************
*
* nibble_sort()
*
* specification: nibble_sort() treats its argument as a sequence of 16 4-bit
* numbers, and returns a new unsigned long integer containing the same nibbles,
* sorted numerically, with smaller-valued nibbles towards the "small end" of
* the unsigned long value that you return
*
* the fact that nibbles and hex digits correspond should make it easy to
* verify that your code is working correctly
*
* EXAMPLE: nibble_sort( 0x0403deadbeef0201 ) returns 0xfeeeddba43210000
*
*********************************************************************/

uint8_t* nibble_position( unsigned long arg, int byte_size) {
    uint8_t val;
    unsigned long mask = 0xF;
    uint8_t *smallVals = malloc(byte_size);
    for(int i = 0; i < byte_size; i++) {
        val = mask&(arg >> i * 4);
        smallVals[i] = val;
    }
    return smallVals;
} 

uint8_t* nibble_list_sort(uint8_t smallVals[], int byte_size) {
    for(int i = 0; i < byte_size-1; i++) {
        // printf("0x%X", smallVals[i]);
        // printf("\n");
        int min = i;
        for(int j = i+1; j < byte_size; j++) {
            if(smallVals[j] < smallVals[min]) {
                min = j;
            }
        }
            if(min != i) {
                uint8_t temp = smallVals[min];
                smallVals[min] = smallVals[i];
                smallVals[i] = temp;
            }
    }
    // printf("\n");
    return smallVals;
}

unsigned long nibble_sort( unsigned long arg ) {
    unsigned long sorted = 0x0;

    uint8_t *smallVals = nibble_list_sort(nibble_position(arg, sizeof(arg)*2), sizeof(arg)*2);

    for(int i = 0; i < sizeof(arg)*2; i++) {
        // printf("0x%X", smallVals[i]);
        // printf("\n");
        sorted |= (((unsigned long) smallVals[i]) << i * 4);
    }
    return sorted;
}

/*********************************************************************/

typedef struct elt {
char val;
struct elt *link;
} Elt;

/*********************************************************************/
/* Forward declaration of "free_list()"...
* This allows you to call free_list() in name_list() [if you'd like].
*/

void free_list( Elt* head ); // [No code goes here!]
/*********************************************************************
*
* name_list( name )
*
* specification: allocate and return a pointer to a linked list of
* struct elts
*
* - the first element in the list should contain in its "val" field the
* first letter of the passed in 'name'; the second element the second
* letter, etc.;
*
* - the last element of the linked list should contain in its "val" field
* the last letter of the passed in name and its "link" field should be a
* null pointer.
*
* - each element must be dynamically allocated using a malloc() call
*
* - you must use the "name" variable (change it to be your name).
*
* Note, since we're using C, not C++ you can't use new/delete!
* The analog to delete is the free() function
*
* - if any call to malloc() fails, your function must return NULL and must
* also free any heap memory that has been allocated so far; that is, it
* must not leak memory when allocation fails.
*
* Implement print_list and free_list which should do what you expect.
* Printing or freeing a nullptr should do nothing.
*
* Note: free_list() might be useful for error handling for name_list()...
*
*********************************************************************/

Elt *name_list( char * name ) {
    // if(name == NULL || name == ' ' || isdigit(name)) {
    //     return 1;
    // }

    Elt * head = malloc( sizeof( Elt ) );
    head->val = name[0];
    head->link = NULL;

    Elt * realHead = head;

    for( int i = 1; i < strlen(name); i++ ) {
        Elt * next = malloc(sizeof(Elt));
        next->val = name[i];
        next->link = NULL;

        head->link = next;
        head = next;
    }
    return realHead;
}

/*********************************************************************/

void print_list( Elt* head ) {
    while( head != NULL ) {
        printf("%c", head->val);
        head = head->link;
    }
}

/*********************************************************************/

void free_list( Elt* head ) {
    while(head != NULL) {
        Elt * prevLink = head->link;
        free(head);
        head = prevLink->link;
    }
}

/*********************************************************************
*
* draw_me()
*
* This function creates a file called 'me.txt' which contains an ASCII-art
* picture of you (it does not need to be very big).
*
* Use the C stdlib functions: fopen, fclose, fprintf, etc which live in stdio.h
* - Don't use C++ iostreams
*
*********************************************************************/

void draw_me() {
    FILE *newFile;
    newFile = fopen("me.txt", "w");

    if (newFile == NULL) {
        printf("Error: Could not open or create file.\n");
    } // returns an error if file can't be written.

    fprintf(newFile, "::)");
    fclose(newFile);
}

/*********************************************************************
*
* Test Code - Place your test functions in this section:
*
* Remember, when testing name_list(), you should create a 'myName'
* variable and pass it in.
*/
// bool testByteSort() { ... }
// ...
// ...
/*********************************************************************
*
* main()
*
* The main driver program. You can place your main() method in this
* file to make compilation easier, or have it in a separate file.
*
*********************************************************************/

void test_byte_sort(void){
   
//Basic test
  printf("\n\nProceeding with byte sort tests...");
  unsigned long input =0x0403deadbeef0201UL;
  unsigned long expectation = 0xefdebead04030201UL;
  unsigned long output = byte_sort(input);
  printf("Basic test: %s\n", output == expectation ? "PASS" : "FAIL");
   
//testing what it does if the input is already sorted
  input =0x0807060504030201UL;
  expectation = 0x0807060504030201UL;
  output = byte_sort(input);
//   printf("%lx", output);
  printf("\nAlready sorted test: %s\n", output == expectation ? "PASS" : "FAIL");
   
//testing what it does if the input is already sorted
   input = 0x0102030405060708UL;
   expectation = 0x0807060504030201UL;
   output = byte_sort(input);
//    printf("%lx", output);
   printf("\nReverse order test: %s\n", output == expectation ? "PASS" : "FAIL");
} // Provided by Demi

void test_nibble_sort(void){
  
//Basic test
  printf("\n\nProceeding with nibble sort tests...");
  unsigned long input =0x0403deadbeef0201;
  unsigned long expectation = 0xfeeeddba43210000;
  unsigned long output = nibble_sort(input);
  printf("\nBasic test: %s\n", output == expectation ? "PASS" : "FAIL");
   
//testing what it does if the input is already sorted
  input =0xfeeeddba43210000;
  output = nibble_sort(input);
  printf("Already sorted test: %s\n", output == expectation ? "PASS" : "FAIL");
} // Provided by Demi

void test_draw_me_created(void) {
    FILE *file;
    int output = 0;
    int expectation = 1;
    if ((file = fopen("me.txt", "r"))) { // Opens file in read only
        fclose(file);
        output = 1; // if file exists, sets output to 1.
    }
    printf("\nFile successfully created: %s\n", output == expectation ? "PASS" : "FAIL"); // States whether the file has been created or not.
}

void test_draw_me_correct(void) {
    char buffer[256];
    FILE *file;

    if ((file = fopen("me.txt", "r"))) { // Opens file
        if (fgets(buffer, 256, file) != NULL) { // Grabs file contents off the first line.
            if(strcmp(buffer, "::)") == 0) { // Compares file contents to expected ASCII.
                printf("\nFile has correct ASCII Art!\n");
            }
            else {
                printf("\nFile has incorrect ASCII Art.\n");
            }
        }
    fclose(file); // Closes file
    }
}

int main() {
    unsigned long bytes = 0x0403deadbeef0201;
    // unsigned long bytes = 0xffffffffffffffff;
    printf("%lx", byte_sort(bytes));
    printf("%lx", nibble_sort(bytes));

    test_byte_sort();
    test_nibble_sort();

    char *myName = "Dylan";
    print_list(name_list(myName));
    draw_me();

    test_draw_me_created();
    test_draw_me_correct();
}

