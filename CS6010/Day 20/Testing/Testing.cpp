#include <SFML/Graphics.hpp>

//struct Room{
//    
//    // This example shows the most common use of sf::Texture:
//    // drawing a sprite
//    
//    // Load a texture from a file
//    sf::Texture * tex = new sf::Texture("bathroomfloor.png");
//    
//    // Assign it to a sprite
//    sf::Sprite * spr = new sf::Sprite(*tex);
//    // Draw the textured sprite
////    sf::Window.draw(sprite);
//};



int main()
{
    sf::Texture tex = sf::Texture("bathroomfloor.png");
    
    // Assign it to a sprite
    sf::Sprite spr = sf::Sprite(tex);
    
    // create the window
    sf::RenderWindow window(sf::VideoMode({800, 600}), "My window");

    // run the program as long as the window is open
    while (window.isOpen())
    {
        // check all the window's events that were triggered since the last iteration of the loop
        while (const std::optional event = window.pollEvent())
        {
            // "close requested" event: we close the window
            if (event->is<sf::Event::Closed>())
                window.close();
        }

        // clear the window with black color
        window.clear(sf::Color::Black);

        // draw everything here...
         window.draw(spr);

        // end the current frame
        window.display();
    }
}

