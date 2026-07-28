//
//  Deck.h
//  Poker2
//
//  Created by Dylan Weiner on 8/28/25.
//

#ifndef DECK_H
#define DECK_H

#include <iostream>

#include <string>

#include <vector>

#include <cstdlib>

#include <ctime>

#include <algorithm>

#include <utility>

#include <SFML/Graphics.hpp>

using namespace std;

struct Card {
    int actualRank;   // 1 = Ace, 2-10, 11 = Jack, 12 = Queen, 13 = King
    
    string actualSuit; // "Hearts", "Diamonds", "Clubs", "Spades"
};

class Deck {
public:
    vector<Card> deck;
    
    Deck();
    Deck(const Deck& rhs);
    
    string rankToString(int rank);
    
    void Shuffle();
    void printDeck(Deck & deck);
    
    
    
    Deck operator=(const Deck& rhs);
    

    
//    bool operator==(const vector<Card> & rhs)const;
//    bool operator!=(const vector<Card> & rhs)const;
//    bool operator<(const vector<Card> & rhs)const;
//    bool operator<=(const vector<Card> & rhs)const;
//    bool operator>(const vector<Card> & rhs)const;
//    bool operator>=(const vector<Card> & rhs)const; //Use these overloads to compare hand values and create a new struct for player hands.
private:
    vector<Card> actualDeck;
};

struct Hand {
    vector<Card> hand;
    Hand createHand(Deck & deck);
    
    void sortHand(Deck & deck);
    
    Hand operator=(const Hand& rhs);
    
    bool isFlush(const Hand & rhs);
    bool isStraight(const Hand & rhs);
    bool isFullHouse(const Hand & rhs);
    bool isRoyalFlush(const Hand & rhs);
    bool isTenLowest(const Hand & rhs);
};

string Deck::rankToString(int rank) {
    
    if (rank == 1) return "A";
    
    else if (rank == 11) return "J";
    
    else if (rank == 12) return "Q";
    
    else if (rank == 13) return "K";
    
    else return to_string(rank);
    
}

// Function to create a deck of 52 cards

Deck::Deck() {
    Deck deck;
    string suits[4] = {"Hearts", "Diamonds", "Clubs", "Spades"};
    for (int s = 0; s < 4; s++) {        // loop through suits
        for (int r = 1; r <= 13; r++) {  // loop through ranks
            Card c;
            c.actualRank = r;
            c.actualSuit = suits[s];
            actualDeck.push_back(c);
        }
    }
    deck.deck = deck.actualDeck;
}


Deck Deck::operator=(const Deck& rhs) {
    this->actualDeck.assign(rhs.actualDeck.begin(), rhs.actualDeck.end());
    
    return (*this);
}

Deck::Deck(const Deck& rhs) {
    Card c;
    
    c.actualRank = c.actualRank;
    c.actualSuit = c.actualSuit;
    
    this->actualDeck = rhs.actualDeck;
}

// Function to shuffle all cards.

void Deck::Shuffle() {
    Card c;
    for(int i = 0; i < 52; i++) {
        int cap = 0 + std::rand() % 51;
        Card temp = deck[i];
        deck[i] = deck[cap];
        deck[cap] = temp;
    }
}

Hand Hand::createHand(Deck & rhs) {
    string suits[4] = {"Hearts", "Diamonds", "Clubs", "Spades"};
    for (int s = 0; s < 5; s++) {
        this->hand[s] = rhs.deck[s];
    }
    return (*this);
}


Hand Hand::operator=(const Hand& rhs) {
    this->hand.assign(rhs.hand.begin(), rhs.hand.end());
    
    return (*this);
}

// Function to print all cards in the deck

void Hand::sortHand(Deck & rhs) {
    Card c;
    for(int i = 0; i < hand.size(); i++) {
        for(int j = i + 1; j < hand.size(); j++) { //The i prevents excessive iteration and the lowest values will be swapped by the if statement.
            if(hand[j].actualRank < hand[i].actualRank) {
                Card temp = hand[i];
                hand[i] = hand[j];
                hand[j] = temp;
            }
        }
    }
}

void Deck::printDeck(Deck & rhs) {
    
    for (int i = 0; i < deck.size(); i++) {
        
        cout << rankToString(deck[i].actualRank) << " of " << deck[i].actualSuit << endl;
        
    }
    
}



bool Hand::isFlush(const Hand & rhs) {
    for(int i = 0; i < hand.size()-1; i++) {
        Card c1 = hand[i];
        Card c2 = hand[i+1];
        if(c1.actualSuit != c2.actualSuit) {
            return false;
        }
    }
    return true;
}

bool Hand::isStraight(const Hand & rhs) {
    vector<int> r;
    for(int i = 0; i < hand.size(); i++) {
        Card c = hand[i];
        r.push_back(c.actualRank);
    }
        std::sort(r.begin(), r.end());
    for(int i = 0; i < r.size() - 1; i++) {
        if(r[i] + 1 != (r[i+1])) {
            //cout << r[i];
            return false;
        }
    }
    return true;
}

bool Hand::isFullHouse(const Hand & rhs) {
    vector<int> r;
    for(int i = 0; i < hand.size(); i++) {
        Card c = hand[i];
        r.push_back(c.actualRank);
    }
    std::sort(r.begin(), r.end());
    for(int i = 0; i < r.size() - 1; i++) {
        if(r[0] == (r[1]) && r[2] == r[3] && r[3] == r[4]) {
            //cout << r[i];
            return true;
        }
        else if(r[0] == (r[1]) && r[1] == r[2] && r[3] == r[4]) {
            //cout << r[i];
            return true;
        }
        
    }
    return false;
}

bool Hand::isTenLowest(const Hand & rhs) {
    vector<int> low;
    for(int i = 0; i < hand.size(); i++) {
        Card c = hand[i];
        low.push_back(c.actualRank);
    }
        sort(low.begin(), low.end());
    if(low[0] == 1 && low[1] == 10) {
        return true;
    }
    cout << low[0] << low[1] << low[2] << low[3] << low[4];
    return false;
}

bool Hand::isRoyalFlush(const Hand & rhs) {
    vector<int> r;
    for(int i = 0; i < hand.size(); i++) {
        Card c = hand[i];
        r.push_back(c.actualRank);
    }
    std::sort(r.begin(), r.end());
    for(int i = 1; i < r.size() - 1; i++) {
        if(r[i] + 1 != (r[i+1])) {
            //cout << r[i];
            return false;
        }
    }
    return true;
}

#endif
