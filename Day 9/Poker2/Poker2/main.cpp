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
    double royalFlush = 0;
    double straightFlush = 0;
    double fullHouse = 0;
    double flush = 0;
    double straight = 0;
    double highCard = 0;
    double shuffles = 1000000;
    
    
    
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
        
        sortHand(myHand);
        
//        sortHand(deck);
        
        cout << "\n\n";
        
        printDeck(myHand);
        
        if(isRoyalFlush(myHand) && isFlush(myHand) && isTenLowest(myHand)) {
            std::cout << "You have a royal flush!" << std::endl;
            royalFlush += 1;
        }
        else if(isStraight(myHand) && isFlush(myHand)) {
            std::cout << "You have a straight flush!" << std::endl;
            straightFlush += 1;
        }
        else if(isFullHouse(myHand)) {
            cout << "You have a full house!" << std::endl;
            fullHouse += 1;
        }
        else if(isFlush(myHand)) {
            std::cout << "You have a flush!" <<std::endl;
            flush += 1;
        }
        else if(isStraight(myHand)) {
            std::cout << "You have a straight!" << std::endl;
            straight += 1;
        }
        else if (!isStraight(myHand) && !isFlush(myHand) && !isFullHouse(myHand)) {
            cout << "You have a high card." << endl;
            highCard += 1;
        }
    }
    
    cout << "\n" << "Whole Numbers: " << "\n" << "Royal Flushes: " << royalFlush <<endl;
    cout << "Straight Flushes: " << straightFlush << endl;
    cout << "Full Houses: " << fullHouse <<endl;
    cout << "Flushes: " << flush << endl;
    cout << "Straights: " << straight << endl;
    cout << "High Cards: " << highCard << endl << endl;
    
    cout << "\n" << "Fractions: " << "\n" << "Royal Flushes: " << royalFlush/shuffles <<endl;
    cout << "Straight Flushes: " << straightFlush/shuffles << endl;
    cout << "Full Houses: " << fullHouse/shuffles <<endl;
    cout << "Flushes: " << flush/shuffles << endl;
    cout << "Straights: " << straight/shuffles << endl;
    cout << "High Cards: " << highCard/shuffles << endl;
    
    return 0;
    
}
