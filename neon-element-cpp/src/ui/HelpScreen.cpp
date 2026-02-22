#include "HelpScreen.hpp"
#include "UIManager.hpp"
#include "audio/Sound.hpp"

namespace neon {

HelpScreen::HelpScreen()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (fontLoaded_) initWidgets();
}

void HelpScreen::initWidgets()
{
    title_ = std::make_unique<Label>(font_, "HELP", 48);
    title_->setPosition(560.f, 30.f);

    tabGroup_ = std::make_unique<RadioGroup>(
        font_, "Topics", std::vector<std::string>{"Controls", "Game Modes", "Elements"}, 0);
    tabGroup_->setPosition(60.f, 100.f);
    tabGroup_->setOnChange([this](int idx) { currentTab_ = idx; });

    // Controls content
    auto addLabel = [this](const std::string& text, float y) {
        auto l = std::make_unique<Label>(font_, text, 18);
        l->setColor(sf::Color(200, 200, 200));
        l->setPosition(300.f, y);
        contentLabels_.push_back(std::move(l));
    };

    // Tab 0: Controls (indices 0-8)
    addLabel("WASD - Isometric Movement", 110.f);
    addLabel("Arrow Keys - Cartesian Movement", 140.f);
    addLabel("Left Click - Light Attack", 170.f);
    addLabel("Right Click - Charge Heavy Attack", 200.f);
    addLabel("Middle Click - Shield", 230.f);
    addLabel("1/2/3/4 - Switch Element (Fire/Water/Earth/Air)", 260.f);
    addLabel("P - Pause", 290.f);
    addLabel("Tab - Show Leaderboard", 320.f);
    addLabel("Esc - Quit", 350.f);

    // Tab 1: Game Modes (indices 9-12)
    addLabel("First to X Kills: First player to reach kill target wins.", 110.f);
    addLabel("Timed: Highest score when timer expires wins.", 140.f);
    addLabel("Hill: Stand on the hill to gain points. First to target wins.", 170.f);
    addLabel("Regicide: Kill the king for bonus points!", 200.f);

    // Tab 2: Elements (indices 13-17)
    addLabel("Fire > Earth > Air > Water > Fire (damage cycle)", 110.f);
    addLabel("Matching element attacks deal reduced damage.", 140.f);
    addLabel("Shield blocks damage but matching element breaks through.", 170.f);
    addLabel("Switch elements strategically to counter opponents!", 200.f);
    addLabel("Power-ups: Heal (green), Speed (blue), Damage (red)", 230.f);

    backBtn_ = std::make_unique<Button>(font_, "BACK", 28);
    backBtn_->setPosition(640.f, 640.f);
    backBtn_->setSize(200.f, 50.f);
}

void HelpScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    tabGroup_->handleEvent(event, window);

    backBtn_->setOnHover([this]() {
        if (manager_) manager_->getAudio().playSound(SoundEffect::Hover1, 50.f);
    });
    backBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->popScreen();
        }
    });
    backBtn_->handleEvent(event, window);
}

void HelpScreen::update(float /*dt*/) {}

void HelpScreen::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;
    window.clear(sf::Color(10, 0, 20));

    title_->draw(window);
    tabGroup_->draw(window);

    // Draw only content for current tab
    size_t start = 0, end = 0;
    switch (currentTab_) {
        case 0: start = 0; end = 9; break;
        case 1: start = 9; end = 13; break;
        case 2: start = 13; end = 18; break;
    }
    for (size_t i = start; i < end && i < contentLabels_.size(); i++)
        contentLabels_[i]->draw(window);

    backBtn_->draw(window);
}

} // namespace neon
