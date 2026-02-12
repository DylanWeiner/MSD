CXX=c++
CFLAGS = -std=c++17
HEADERS = headers/cmdline.h headers/catch.h headers/expr.h headers/namespace.hpp
CXXSOURCE = cmdline.cpp expr.cpp main.cpp namespace.cpp
OBJS = cmdline.o expr.o main.o

msdscript: $(OBJS) #This makes the executable out of the .o files
	$(CXX) $(OBJS) -o msdscript

# main.o

# cmdline.o

# expr.o

*.o: *.cpp
	$(CXX) $(CFLAGS) -I./headers $< -o $@

clean: #This removes all of the .o files
	rm -f *.o msdscript

test: msdscript #This is used to run the executable that would be made
	./msdscript --test
	
help: msdscript
	./msdscript --help
	
doc: msdscript
	doxygen

.PHONY: clean msdscript test help doc

