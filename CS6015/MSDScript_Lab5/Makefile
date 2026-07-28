CXX      = c++
CFLAGS   = -std=c++17
LIB_DIR  = Library_MSDScript
LIB_NAME = my_library

OBJS     = main.o

LDFLAGS  = -L$(LIB_DIR) -l$(LIB_NAME)

msdscript: $(OBJS)
	$(CXX) $(LDFLAGS) $^ -o $@

%.o: %.cpp
	$(CXX) $(CFLAGS) -c $< -o $@

clean:
	rm -f *.o msdscript

.PHONY: clean
