//
//  main.cpp
//  TripleTemplate
//
//  Created by Dylan Weiner on 9/11/25.
//

#include "TripleTime.h"

int main(int argc, const char * argv[]) {
    Triple<string> t_a = {"Egg ", "Salad ", "Sandwich!"};
    Triple<int> t_b = {16, 42, 5};
    Triple<vector<int>> t_c ({16, 42, 5}, {10, 10, 10}, {5, 5, 5});
    cout << t_a.addVar() << endl;
    cout << t_b.addVar() << endl;
//    t_c.addVar();  //This doesn't work because you can't add vectors without targeting specific values.
    return 0;
}
