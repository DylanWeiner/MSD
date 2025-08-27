#include <iostream>

#include <vector>

#include <string>

#include <cstdlib>

#include "Deck.h"

int main() {
    
    // Create a deck
    
    vector<Card> deck = createDeck();
    
    // Print the deck
    
    cout << "Here is a full deck of cards:\n";
    
    Shuffle(deck);
    
    printDeck(deck);
    
    vector<Card> myHand = createHand(deck);
    
    cout << "\n\n";
    
    printDeck(myHand);
    
    if(IsStraight(myHand) && isFlush(myHand) && TenLowest(myHand)) {
       std::cout << "You have a royal flush!" << std::endl;
   }
    else if(IsStraight(myHand) && isFlush(myHand)) {
       std::cout << "You have a straight flush!" << std::endl;
   }
    else if(IsFullHouse(myHand)) {
        cout << "You have a full house!" << std::endl;
    }
    else if(isFlush(myHand)) {
        std::cout << "You have a flush!" <<std::endl;
    }
     else if(IsStraight(myHand)) {
        std::cout << "You have a straight!" << std::endl;
    }
     else {
         cout << "You have a high card." << endl;
     }
    
    
    
    return 0;
    
}
