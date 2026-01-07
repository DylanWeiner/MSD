HEADERS = Caps.h Clothing.h Pants.h #This ties in the .h files
CXX=c++
CFLAGS = -std=c++17
CXXSOURCE = main.cpp Caps.cpp Clothing.cpp Pants.cpp
OBJS = main.o Caps.o Clothing.o Pants.o

all: $(OBJS) #This makes the executable out of the .o files
	$(CXX) $(OBJS) -o all

*.o: *.cpp
	$(CXX) $(CFLAGS) $< -o $@

clean: #This removes all of the .o files
	rm -f *.o all

run: all #This is used to run the executable that would be made
	./all

printM:
	@echo "CXX = $(CXX)"
	@echo "CFLAGS = $(CFLAGS)"
	@echo "OBJECTS = $(OBJS)"
	@echo "HEADERS = $(HEADERS)"

.PHONY: clean printM run
