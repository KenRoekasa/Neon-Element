#pragma once

#include "Screen.hpp"
#include "Widget.hpp"
#include <memory>

namespace neon {

class HelpScreen : public Screen {
public:
    HelpScreen();

    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    void update(float dt) override;
    void draw(sf::RenderWindow& window) override;

private:
    void initWidgets();

    sf::Font font_;
    bool fontLoaded_ = false;

    std::unique_ptr<Label> title_;
    std::unique_ptr<RadioGroup> tabGroup_;
    std::vector<std::unique_ptr<Label>> contentLabels_;
    std::unique_ptr<Button> backBtn_;

    int currentTab_ = 0;
};

} // namespace neon
