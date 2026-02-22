#pragma once

#include "Screen.hpp"
#include "Widget.hpp"
#include "engine/model/GameType.hpp"
#include "engine/ai/enums/AiType.hpp"
#include <memory>
#include <vector>

namespace neon {

class LocalSetupScreen : public Screen {
public:
    LocalSetupScreen();

    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    void update(float dt) override;
    void draw(sf::RenderWindow& window) override;

private:
    void initWidgets();

    sf::Font font_;
    bool fontLoaded_ = false;

    std::unique_ptr<Label> title_;

    // Enemy count selection
    std::unique_ptr<RadioGroup> enemyCountGroup_;

    // Difficulty per enemy
    std::unique_ptr<RadioGroup> diff1Group_;
    std::unique_ptr<RadioGroup> diff2Group_;
    std::unique_ptr<RadioGroup> diff3Group_;

    // Game mode
    std::unique_ptr<RadioGroup> modeGroup_;

    std::unique_ptr<Button> startBtn_;
    std::unique_ptr<Button> backBtn_;

    int enemyCount_ = 1;
};

} // namespace neon
