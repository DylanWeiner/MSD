#include "Deck.h"

int main() {
    srand(time(0)); //seed needs to be here or it will reset to the exact same seed every time if it's nested in the shuffle function.
    
    sf::RenderWindow window (sf::VideoMode({800,600}),"SFML Window");
    
    sf::CircleShape circle;
    circle.setRadius(150);
    circle.setOutlineColor(sf::Color::Green);
    circle.setOutlineThickness(7);
    circle.setPosition({15, 35});
    
    sf::Clock clock;
    
    while(window.isOpen()){
        while(std::optional event = window.pollEvent()){
            if(event->is<sf::Event::Closed>()){
                window.close();
            }
    
            Deck();
        
        Shuffle(deck);
        
        
        vector<Deck> theirHand = createHand(deck);
        
        vector<Deck> myHand = createHand(deck);
        
        sortHand(theirHand);
        
        sortHand(myHand);
        
        cout << "\n\n";
        
        printDeck(myHand);
        
        if(isRoyalFlush(myHand) && isFlush(myHand) && isTenLowest(myHand)) {
            std::cout << "You have a royal flush!" << std::endl;
        }
        else if(isStraight(myHand) && isFlush(myHand)) {
            std::cout << "You have a straight flush!" << std::endl;
        }
        else if(isFullHouse(myHand)) {
            cout << "You have a full house!" << std::endl;
        }
        else if(isFlush(myHand)) {
            std::cout << "You have a flush!" <<std::endl;
        }
        else if(isStraight(myHand)) {
            std::cout << "You have a straight!" << std::endl;
        }
        else if (!isStraight(myHand) && !isFlush(myHand) && !isFullHouse(myHand)) {
            cout << "You have a high card." << endl;
        }
        
        }
        window.clear();
        sf::Time timeElapsed = clock.restart();
        window.draw(circle);
        window.display();
    }
    return 0;
}
