//Done with Demi, Courtney, and Fatima

//Percentages off of 100,000 Shuffles:
//Royal Flushes: 0%
//Straight Flushes: 0.003 %
//Full Houses: 0.141 %
//Flushes: 0.235 %
//Straights: 0.373 %
//High Cards: 99.248 %
//Program ended with exit code: 0

#include <iostream>

#include <vector>

#include <string>

#include <cstdlib>

#include "Deck.h"

int main() {
    srand(time(0)); //seed needs to be here or it will reset to the exact same seed every time if it's nested in the shuffle function.
    double royfl = 0;
    double strfl = 0;
    double full = 0;
    double fl = 0;
    double str = 0;
    double hc = 0;
    double shuffles = 100000;
    
    
    
    for(int i =0; i < shuffles; i++) {
        
//        Create a deck
        
        vector<Card> deck = createDeck();
        
//        Print the deck
        
//        cout << "Here is a full deck of cards:\n";
        
        Shuffle(deck);
        
        //printDeck(deck);
        
//        RiggedRoyalFlush(deck);
//        RiggedFlush(deck);
//        RiggedStraight(deck);
//        RiggedFullHouse(deck);
    
        vector<Card> myHand = createHand(deck);
        
        cout << "\n\n";
        
        printDeck(myHand);
        
        if(IsRoyalFlush(myHand) && isFlush(myHand) && TenLowest(myHand)) {
            std::cout << "You have a royal flush!" << std::endl;
            royfl += 1;
        }
        else if(IsStraight(myHand) && isFlush(myHand)) {
            std::cout << "You have a straight flush!" << std::endl;
            strfl += 1;
        }
        else if(IsFullHouse(myHand)) {
            cout << "You have a full house!" << std::endl;
            full += 1;
        }
        else if(isFlush(myHand)) {
            std::cout << "You have a flush!" <<std::endl;
            fl += 1;
        }
        else if(IsStraight(myHand)) {
            std::cout << "You have a straight!" << std::endl;
            str += 1;
        }
        else {
            cout << "You have a high card." << endl;
            hc += 1;
        }
    }
    
    cout << "\n" << "Whole Numbers: " << "\n" << "Royal Flushes: " << royfl <<endl;
    cout << "Straight Flushes: " << strfl << endl;
    cout << "Full Houses: " << full <<endl;
    cout << "Flushes: " << fl << endl;
    cout << "Straights: " << str << endl;
    cout << "High Cards: " << shuffles << endl << endl;
    
    cout << "\n" << "Fractions: " << "\n" << "Royal Flushes: " << royfl/shuffles <<endl;
    cout << "Straight Flushes: " << strfl/shuffles << endl;
    cout << "Full Houses: " << full/shuffles <<endl;
    cout << "Flushes: " << fl/shuffles << endl;
    cout << "Straights: " << str/shuffles << endl;
    cout << "High Cards: " << hc/shuffles << endl;
    
    return 0;
    
}
