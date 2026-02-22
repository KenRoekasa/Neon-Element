#pragma once

#include "Screen.hpp"
#include "Widget.hpp"
#include <memory>

namespace neon {

class OptionsScreen : public Screen {
public:
    OptionsScreen();

    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    void update(float dt) override;
    void draw(sf::RenderWindow& window) override;

private:
    void initWidgets();

    sf::Font font_;
    bool fontLoaded_ = false;

    std::unique_ptr<Label> title_;
    std::unique_ptr<Slider> musicSlider_;
    std::unique_ptr<Slider> effectsSlider_;
    std::unique_ptr<Button> backBtn_;
};

} // namespace neon
