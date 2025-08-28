//
//  Deck.h
//  Poker
//
//  Created by Dylan Weiner on 8/27/25.
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

void printDeck(vector<Card> deck);

vector<Card> createHand(vector<Card> & deck);

bool isFlush(const vector<Card> & deck);

bool IsStraight(const vector<Card> & hand);

bool IsFullHouse(const vector<Card> & hand);

bool IsRoyalFlush(const vector<Card> & hand);

bool TenLowest(const vector<Card> & card);

void RiggedFlush(vector<Card> & deck);

void RiggedRoyalFlush(vector<Card> & deck);

void RiggedStraight(vector<Card> & deck);

void RiggedFullHouse(vector<Card> & deck);

#endif
