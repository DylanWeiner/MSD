CXX=c++
CFLAGS = -std=c++17
HEADERS = cmdline.h
CXXSOURCE = msdscript.cpp cmdline.cpp
OBJS = msdscript.o cmdline.o

msdscript: $(OBJS) #This makes the executable out of the .o files
	$(CXX) $(OBJS) -o msdscript

*.o: *.cpp
	$(CXX) $(CFLAGS) $< -o $@

clean: #This removes all of the .o files
	rm -f *.o msdscript

test: msdscript #This is used to run the executable that would be made
	./msdscript --test
	
help: msdscript
	./msdscript --help

.PHONY: clean msdscript test help

