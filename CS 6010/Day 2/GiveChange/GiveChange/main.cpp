//
//  main.cpp
//  GiveChange
//
//  Created by Dylan Weiner on 8/19/25.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    int cost; //defining cost of purchase, amount paid, and total change.
    int paid;
    int change; //preparing for int divison to find nearest whole change with fewest coins and dollars
    int quarter;
    int dime;
    int nickel;
    int penny;
    int leftover;
    
    std::cout << "Please input cost of item in cents.\n";
    std::cin >> cost;
    std::cout << "Please input amount you paid in cents.\n";
    std::cin >> paid;
    change = paid - cost;
    quarter = change / 25;
    leftover = change % 25;
    dime = leftover / 10;
    leftover = leftover % 10;
    nickel = leftover / 5;
    leftover = leftover % 5;
    penny = leftover / 1;
    std::cout << "Your change is " << change << " cents. You will receive:\n" << quarter << " Quarter(s)\n" << dime << " Dime(s)\n" << nickel << " Nickel(s)\n" << penny << " Pennies\n";
    return 0;
}
