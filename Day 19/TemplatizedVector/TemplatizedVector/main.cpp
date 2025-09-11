//
//  main.cpp
//  TemplatizedVector
//
//  Created by Dylan Weiner on 9/11/25.
//

#include "Vector.h"

int main(int argc, const char * argv[]) {
    vector vec (20);
    
    assert(vec.viewCapacity()==20);
    
    vec.pushBack(10);
    vec.pushBack(28);
    vec.pushBack(93);
    vec.pushBack(14);
    vec.pushBack(6);
    vec.pushBack(9);
    
    assert(vec.get(0)==10);
    
    assert(vec.get(5)==9 && "Incorrect");
    
    assert(vec.get(2)==93 && "Incorrect");
    
    vec.popBack();
    
    assert(vec.viewSize() == 5);
    
    assert(vec.viewSize() != 6);
    
    assert(vec.get(4) == 6);
    
    vec.set(1, 99);
    
    assert(vec.get(1) == 99);
    
    return 0;
}
