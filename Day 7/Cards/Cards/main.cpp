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

std::vector <card> Numbers(std::vector<card> s) {
    card deck;
    for(int i = 0; i < 4; i++) {
        for(int j = 0; j < 13; j++) {
            deck.val = j + 1;
            s.push_back(deck);
        }
    }
    return s;
}

std::vector <card> Name(std::vector<card> s) {
    card deck;
    for(int i = 0; i < 4; i++) {
        for(int j = 0; j < 13; j++) {
            if(j < 10) {
                int full = j + 1;
                deck.name = std::to_string(full);
            }
            else if(j == 10) {
                deck.name = "Jack";
            }
            else if(j == 11) {
                deck.name = "Queen";
            }
            else if(j == 12) {
                deck.name = "King";
            }
            s.push_back(deck);
        }
    }
    for(int j = 0; j < 13; j++) {
        deck.suit = "Hearts";
        s.push_back(deck);
    }
    for(int j = 0; j < 13; j++) {
        deck.suit = "Spades";
        s.push_back(deck);
    }
    for(int j = 0; j < 13; j++) {
        deck.suit = "Diamonds";
        s.push_back(deck);
    }
    for(int j = 0; j < 13; j++) {
        deck.suit = "Clubs";
        s.push_back(deck);
    }
    for(int i = 0; i < 4; i++) {
        for(int j = 0; j < 13; j++) {
            deck.val = j + 1;
            s.push_back(deck);
        }
    }
    return s;
}

std::vector <card> Suit(std::vector<card> s) {
    card deck;
    for(int j = 0; j < 13; j++) {
        deck.suit = "Hearts";
        s.push_back(deck);
    }
    for(int j = 0; j < 13; j++) {
        deck.suit = "Spades";
        s.push_back(deck);
    }
    for(int j = 0; j < 13; j++) {
        deck.suit = "Diamonds";
        s.push_back(deck);
    }
    for(int j = 0; j < 13; j++) {
        deck.suit = "Clubs";
        s.push_back(deck);
    }
    return s;
}

int main(int argc, const char * argv[]) {
    std::vector<card> decks;
    std::vector<card> names = Name(decks);
    std::vector<card> values = Numbers(decks);
    std::vector<card> suits = Suit(decks);
    
    
    
    for(int i = 0; i < 52; i++){
        std::cout << "This card is a " << names[i].name << " of " << suits[i].suit << " worth a value of " << values[i].val << std::endl;
    }
    
    return 0;
}
