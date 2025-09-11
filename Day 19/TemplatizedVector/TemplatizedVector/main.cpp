//
//  main.cpp
//  TemplatizedVector
//
//  Created by Dylan Weiner on 9/11/25.
//

#include "Vector.h"


int main(int argc, const char * argv[]) {
    vector<int> vec (20);
    
    vector<std::string> vec2 (20);
    
    vector<int> vec3 (20);
    
    vector<int> vec4 (20);
    
    vector<int> vec5 (19);
    
    vector<std::string> vec6 (20);
    
    vector<std::string> vec7 (20);
    
    assert(vec.viewCapacity()==20);
    
    vec.pushBack(10);
    vec.pushBack(28);
    vec.pushBack(93);
    vec.pushBack(14);
    vec.pushBack(6);
    vec.pushBack(9);
    
    vec3.pushBack(10);
    vec3.pushBack(28);
    vec3.pushBack(93);
    vec3.pushBack(14);
    vec3.pushBack(6);
    vec3.pushBack(9);
    
    vec2.pushBack("Egg ");
    vec2.pushBack("Salad ");
    vec2.pushBack("Sandwich ");
    vec2.pushBack("Yum!");
    
    vec4.pushBack(10);
    vec4.pushBack(28);
    vec4.pushBack(93);
    vec4.pushBack(14);
    vec4.pushBack(6);
    vec4.pushBack(9);
    
    vec5.pushBack(10);
    vec5.pushBack(28);
    vec5.pushBack(93);
    vec5.pushBack(14);
    vec5.pushBack(6);
    
    vec6.pushBack("attack");
    vec6.pushBack("on");
    vec6.pushBack("titan");
    
    vec7.pushBack("full");
    vec7.pushBack("metal");
    vec7.pushBack("alchemist");
    
    assert(vec4 >= vec5);
    
    assert(vec5 <= vec4);
    
    assert(vec5 != vec4);
    
    assert(vec == vec3);
    
    assert(vec7 < vec6);
    
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
