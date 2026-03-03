CXX=c++
CFLAGS = -std=c++17
HEADERS = cmdline.h catch.h expr.h parse.hpp
CXXSOURCE = cmdline.cpp expr.cpp main.cpp parse.cpp
OBJS = cmdline.o expr.o main.o parse.o
TESTOBJS = run_msdscript.o exec.o

msdscript: $(OBJS) #This makes the executable out of the .o files
	$(CXX) $(OBJS) -o msdscript

test_msdscript: run_msdscript.cpp exec.cpp
	$(CXX) run_msdscript.cpp exec.cpp -o test_msdscript

compare_msdscript: run_msdscript2.cpp exec.cpp
	$(CXX) run_msdscript2.cpp exec.cpp -o compare_msdscript

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

