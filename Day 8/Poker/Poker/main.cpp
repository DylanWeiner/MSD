#include <iostream>

#include <vector>

#include <string>

#include <cstdlib>

using namespace std;







// A simple struct to hold a card

struct Card {
    
    int rank;   // 1 = Ace, 2-10, 11 = Jack, 12 = Queen, 13 = King
    
    string suit; // "Hearts", "Diamonds", "Clubs", "Spades"
    
};

// Function to turn rank number into a printable string

string rankToString(int rank) {
    
    if (rank == 1) return "A";
    
    else if (rank == 11) return "J";
    
    else if (rank == 12) return "Q";
    
    else if (rank == 13) return "K";
    
    else return to_string(rank);
    
}

// Function to create a deck of 52 cards

vector<Card> createDeck() {
    
    vector<Card> deck;
    
    string suits[4] = {"Hearts", "Diamonds", "Clubs", "Spades"};
    
    
    
    for (int s = 0; s < 4; s++) {        // loop through suits
        
        for (int r = 1; r <= 13; r++) {  // loop through ranks
            
            Card c;
            
            c.rank = r;
            
            c.suit = suits[s];
            
            deck.push_back(c);
            
        }
        
    }
    
    return deck;
    
}

// Function to shuffle all cards.

void Shuffle(vector<Card> & deck) {
    srand(13);
    //std::srand(std::time(0));
    for(int i = 0; i < 52; i++) {
        int cap = 0 + std::rand() % 51;
        Card temp = deck[i];
        deck[i] = deck[cap];
        deck[cap] = temp;
    }
}

// Function to print all cards in the deck

void printDeck(vector<Card> deck) {
    
    for (int i = 0; i < deck.size(); i++) {
        
        cout << rankToString(deck[i].rank) << " of " << deck[i].suit << endl;
        
    }
    
}

vector<Card> createHand(vector<Card> & deck) {
    vector<Card> cardHand;
    for(int i = 0; i < 5; i++) {
        cardHand.push_back({deck[i].rank, deck[i].suit});
    }
    
    return cardHand;
}

bool isFlush(const vector<Card> & deck) {
    for(int i = 0; i < deck.size(); i++) {
        if(deck[i].suit != deck[i+1].suit) {
            return false;
        }
    }
    return true;
}

bool IsStraight(const vector<Card> & hand) {
    vector<int> r;
    for(int i = 0; i < hand.size(); i++) {
        r.push_back(hand[i].rank);
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

bool IsFullHouse(const vector<Card> & hand) {
    vector<int> r;
    for(int i = 0; i < hand.size(); i++) {
        r.push_back(hand[i].rank);
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

bool TenLowest(const vector<Card> & card) {
    vector<int> low;
    for(int i = 0; i < card.size(); i++) {
        low.push_back(card[i].rank);
        if(low[0] == 10) {
            return true;
        }
    }
    return false;
}

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
