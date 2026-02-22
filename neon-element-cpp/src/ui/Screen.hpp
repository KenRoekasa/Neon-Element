#pragma once

#include <SFML/Graphics.hpp>

namespace neon {

class UIManager;

// Abstract base for all UI screens
class Screen {
public:
    virtual ~Screen() = default;

    virtual void handleEvent(const sf::Event& event, sf::RenderWindow& window) = 0;
    virtual void update(float dt) = 0;
    virtual void draw(sf::RenderWindow& window) = 0;

    void setManager(UIManager* mgr) { manager_ = mgr; }

protected:
    UIManager* manager_ = nullptr;
};

} // namespace neon
