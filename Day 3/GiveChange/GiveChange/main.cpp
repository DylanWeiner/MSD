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
    int change = 0; //preparing for int divison to find nearest whole change with fewest coins and dollars
    
    int quarter;
    int dime;
    int nickel;
    int penny;
    
    int leftover = 0;
    
    std::cout << "Please input cost of item in cents. ";
    std::cin >> cost;
    
    std::cout << "Please input amount you paid in cents. ";
    std::cin >> paid;
    
    change = paid - cost;
    
    if(change >= 0) {
        
        quarter = change / 25;
        if(quarter > 2) {
            leftover = change - 50;
            quarter = 2;
        }
        
        else{
            leftover = change % 25;
        }
        
        dime = leftover / 10;
        
        if(dime > 2) {
            leftover = leftover - 20;
            dime = 2;
        }

        else {
            leftover = leftover % 10;
        }
        
        nickel = leftover / 5;
        
        if(nickel > 2) {
            leftover = leftover - 10;
            nickel = 2;
        }
        
        else {
            leftover = leftover % 5;
        }
        
        penny = leftover / 1;
        
        if(penny > 2) {
            std::cout << "Insufficient funds for change in Vending Machine, returning money.\n";
        }
        else {
            std::cout << "Your change is " << change << " cents. You will receive:\n" << quarter << " Quarter(s)\n" << dime << " Dime(s)\n" << nickel << " Nickel(s)\n" << penny << " Pennies\n";
        }
        return 0;
    }
    else {
        std::cout << change << " cents?! \nThis is insufficient payment, your purchase has been terminated.\n";
    }
    return 1;
}
