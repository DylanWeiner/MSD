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

#include "Deck.h"

using namespace std;

struct Card {
    int rank;   // 1 = Ace, 2-10, 11 = Jack, 12 = Queen, 13 = King
    
    string suit; // "Hearts", "Diamonds", "Clubs", "Spades"
};

string rankToString(int rank);

vector<Card> createDeck();

void Shuffle(vector<Card> & deck);

void sortHand(vector<Card> & deck);

void printDeck(vector<Card> & deck);

vector<Card> createHand(vector<Card> & deck);

bool isFlush(const vector<Card> & deck);

bool isStraight(const vector<Card> & hand);

bool isFullHouse(const vector<Card> & hand);

bool isRoyalFlush(const vector<Card> & hand);

bool isTenLowest(const vector<Card> & card);

void RiggedFlush(vector<Card> & deck);

void RiggedRoyalFlush(vector<Card> & deck);

void RiggedStraight(vector<Card> & deck);

void RiggedFullHouse(vector<Card> & deck);

#endif
