#include <iostream>
#include <SFML/Graphics.hpp>

int main(int argc, const char * argv[]) {

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
        }
        window.clear();
        sf::Time timeElapsed = clock.restart();
        window.draw(circle);
        window.display();
    }
return 0;
}
