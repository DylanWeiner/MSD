CXX=c++
CFLAGS = -std=c++17
HEADERS = cmdline.h catch.h expr.h parse.hpp
CXXSOURCE = cmdline.cpp expr.cpp main.cpp parse.cpp
OBJS = cmdline.o expr.o main.o parse.o

msdscript: $(OBJS) #This makes the executable out of the .o files
	$(CXX) $(OBJS) -o msdscript

# main.o

# cmdline.o

# expr.o

*.o: *.cpp
	$(CXX) $(CFLAGS) $< -o $@

clean: #This removes all of the .o files
	rm -f *.o msdscript

test: msdscript #This is used to run the executable that would be made
	./msdscript --test

interp: msdscript
	./msdscript --interp

print: msdscript
	./msdscript --print

pretty-print: msdscript
	./msdscript --pretty-print
	
help: msdscript
	./msdscript --help
	
doc: msdscript
	doxygen

.PHONY: clean msdscript test help

