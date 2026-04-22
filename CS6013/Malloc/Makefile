CXX = clang++
CXXFLAGS = -std=c++17 -Wall -Wextra -O2
DEBUG_FLAGS = -g -O0

# Target files
SOURCES = MyMalloc.cpp hashTable.cpp
HEADERS = MyMalloc.h hashTable.h

# Main program
MAIN_PROGRAM = Malloc
MAIN_SOURCE = main.cpp

# Test program
TEST_PROGRAM = test_malloc
TEST_SOURCE = test_malloc.cpp

.PHONY: all clean test debug

all: $(MAIN_PROGRAM) $(TEST_PROGRAM)

# Build main program
$(MAIN_PROGRAM): $(MAIN_SOURCE) $(SOURCES) $(HEADERS)
	$(CXX) $(CXXFLAGS) -o $(MAIN_PROGRAM) $(MAIN_SOURCE) $(SOURCES)

# Build test program
$(TEST_PROGRAM): $(TEST_SOURCE) $(SOURCES) $(HEADERS)
	$(CXX) $(CXXFLAGS) -o $(TEST_PROGRAM) $(TEST_SOURCE) $(SOURCES)

# Run tests
test: $(TEST_PROGRAM)
	./$(TEST_PROGRAM)

# Debug build
debug: CXXFLAGS = $(DEBUG_FLAGS)
debug: clean all

# Clean build artifacts
clean:
	rm -f $(MAIN_PROGRAM) $(TEST_PROGRAM) *.o *.dSYM

# Run main program
run: $(MAIN_PROGRAM)
	./$(MAIN_PROGRAM)
