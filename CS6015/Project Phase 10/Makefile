CXX=c++
CFLAGS = -std=c++17 -g
HEADERS = cmdline.h catch.h expr.h parse.hpp val.h
CXXSOURCE = cmdline.cpp expr.cpp main.cpp parse.cpp val.cpp
OBJS = cmdline.o expr.o main.o parse.o val.o
TESTOBJS = run_msdscript.o exec.o

msdscript: $(OBJS) #This makes the executable out of the .o files
	$(CXX) $(OBJS) -o msdscript

test_msdscript: test_msdscript.cpp exec.cpp
	$(CXX) test_msdscript.cpp exec.cpp -o test_msdscript

compare_msdscript: compare_msdscripts.cpp exec.cpp
	$(CXX) compare_msdscripts.cpp exec.cpp -o compare_msdscript

all: msdscript test_msdscript compare_msdscript

*.o: *.cpp
	$(CXX) $(CFLAGS) $< -o $@

clean: #This removes all of the .o files
	rm -f *.o msdscript test_msdscript compare_msdscript

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

