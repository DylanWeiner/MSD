//
//  main.cpp
//  Cards
//
//  Created by Dylan Weiner on 8/26/25.
//

#include <iostream>
#include <string>
#include <vector>

struct card {
    std::string name;
    int val;
    std::string suit;
};

std::vector <card> createDeck() {
    std::vector<card> s;
    card deck;
    std::vector<std::string> suits = {"Hearts", "Spades", "Diamonds", "Clubs"};
    int suitNum = 4;
    int cardToSuit = 13;
    
    for(int i = 0; i < suitNum; i++) {
        for(int j = 0; j < cardToSuit; j++) {
            card cards;
            cards.val = j + 1;
            
            if (j == 0) {
                cards.name = "Ace";
            }
            else if(j == 10) {
                cards.name = "Jack";
            }
            else if(j == 11) {
                cards.name = "Queen";
            }
            else if(j == 12) {
                cards.name = "King";
            }
            else {
                cards.name = std::to_string(j + 1);
            }
            
            cards.suit = suits[i];
            
            s.push_back(cards);
        }
    }
    return s;
}

int main(int argc, const char * argv[]) {
    std::vector<card> finalDeck = createDeck();
    
    
    
    for(int i = 0; i < 52; i++){
        std::cout << "This card is a " << finalDeck[i].name << " of " << finalDeck[i].suit << " worth a value of " << finalDeck[i].val << std::endl;
    }
    
    return 0;
}
