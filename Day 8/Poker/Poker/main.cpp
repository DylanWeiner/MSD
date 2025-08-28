//Done with Demi, Courtney, and Fatima

#include <iostream>

#include <vector>

#include <string>

#include <cstdlib>

#include "Deck.h"

int main() {
    
    double royfl = 0;
    double strfl = 0;
    double full = 0;
    double fl = 0;
    double str = 0;
//    double shuffles = 100000;
    
    
    
//    for(int i =0; i < shuffles; i++) {
        
        // Create a deck
        
        vector<Card> deck = createDeck();
        
//         Print the deck
        
        cout << "Here is a full deck of cards:\n";
        
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
        }
//    }
//    
//    cout << "\n" << "Whole Numbers: " << "\n" << "Royal Flushes: " << royfl <<endl;
//    cout << "Straight Flushes: " << strfl << endl;
//    cout << "Full Houses: " << full <<endl;
//    cout << "Flushes: " << fl << endl;
//    cout << "Straights: " << str << endl << endl;
//    
//    cout << "\n" << "Fractions: " << "\n" << "Royal Flushes: " << royfl/shuffles <<endl;
//    cout << "Straight Flushes: " << strfl/shuffles << endl;
//    cout << "Full Houses: " << full/shuffles <<endl;
//    cout << "Flushes: " << fl/shuffles << endl;
//    cout << "Straights: " << str/shuffles << endl;
    
    return 0;
    
}
