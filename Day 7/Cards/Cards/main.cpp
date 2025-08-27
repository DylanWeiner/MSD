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

std::vector <card> FullDeck(std::vector<card> s) {
    card deck;
    std::string suits[] = {"Hearts", "Spades", "Diamonds", "Clubs"};
    
    for(int i = 0; i < 4; i++) {
        for(int j = 0; j < 13; j++) {
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
    std::vector<card> decks;
    std::vector<card> names = FullDeck(decks);
    
    
    
    for(int i = 0; i < 52; i++){
        std::cout << "This card is a " << names[i].name << " of " << names[i].suit << " worth a value of " << names[i].val << std::endl;
    }
    
    return 0;
}
