#pragma once

#include "Screen.hpp"
#include "Widget.hpp"
#include <memory>

namespace neon {

class MenuScreen : public Screen {
public:
    MenuScreen();

    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    void update(float dt) override;
    void draw(sf::RenderWindow& window) override;

private:
    void initWidgets();

    sf::Font font_;
    sf::Font titleFont_;
    bool fontsLoaded_ = false;

    std::unique_ptr<Label> title_;
    std::unique_ptr<Button> playBtn_;
    std::unique_ptr<Button> helpBtn_;
    std::unique_ptr<Button> optionsBtn_;
    std::unique_ptr<Button> exitBtn_;
};

} // namespace neon
