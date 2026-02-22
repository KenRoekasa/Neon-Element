#include "LocalSetupScreen.hpp"
#include "UIManager.hpp"
#include "audio/Sound.hpp"

namespace neon {

LocalSetupScreen::LocalSetupScreen()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (fontLoaded_) initWidgets();
}

void LocalSetupScreen::initWidgets()
{
    title_ = std::make_unique<Label>(font_, "LOCAL GAME SETUP", 42);
    title_->setPosition(380.f, 30.f);

    // Enemy count (1-3)
    enemyCountGroup_ = std::make_unique<RadioGroup>(
        font_, "Enemies", std::vector<std::string>{"1", "2", "3"}, 0);
    enemyCountGroup_->setPosition(80.f, 100.f);
    enemyCountGroup_->setOnChange([this](int idx) { enemyCount_ = idx + 1; });

    // Difficulty for each enemy
    std::vector<std::string> diffs = {"Easy", "Normal", "Hard"};
    diff1Group_ = std::make_unique<RadioGroup>(font_, "Enemy 1 Difficulty", diffs, 1);
    diff1Group_->setPosition(280.f, 100.f);

    diff2Group_ = std::make_unique<RadioGroup>(font_, "Enemy 2 Difficulty", diffs, 1);
    diff2Group_->setPosition(560.f, 100.f);

    diff3Group_ = std::make_unique<RadioGroup>(font_, "Enemy 3 Difficulty", diffs, 1);
    diff3Group_->setPosition(840.f, 100.f);

    // Game mode
    std::vector<std::string> modes = {"First to X Kills", "Timed", "Hill", "Regicide"};
    modeGroup_ = std::make_unique<RadioGroup>(font_, "Game Mode", modes, 0);
    modeGroup_->setPosition(80.f, 340.f);

    // Start button
    startBtn_ = std::make_unique<Button>(font_, "START", 36);
    startBtn_->setPosition(640.f, 580.f);
    startBtn_->setSize(300.f, 65.f);
    startBtn_->setColors(sf::Color(0, 100, 50), sf::Color(0, 160, 80), sf::Color(0, 200, 100));

    // Back button
    backBtn_ = std::make_unique<Button>(font_, "BACK", 28);
    backBtn_->setPosition(640.f, 660.f);
    backBtn_->setSize(200.f, 50.f);
}

void LocalSetupScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    auto hoverSound = [this]() {
        if (manager_) manager_->getAudio().playSound(SoundEffect::Hover1, 50.f);
    };
    startBtn_->setOnHover(hoverSound);
    backBtn_->setOnHover(hoverSound);

    startBtn_->setOnClick([this]() {
        if (!manager_) return;
        manager_->getAudio().playSound(SoundEffect::Button);

        // Determine game mode
        GameType::Type mode;
        switch (modeGroup_->getSelectedIndex()) {
            case 0: mode = GameType::Type::FirstToXKills; break;
            case 1: mode = GameType::Type::Timed; break;
            case 2: mode = GameType::Type::Hill; break;
            case 3: mode = GameType::Type::Regicide; break;
            default: mode = GameType::Type::FirstToXKills; break;
        }

        // Determine difficulty (encoded as int: 0=Easy, 1=Normal, 2=Hard)
        int avgDiff = diff1Group_->getSelectedIndex();

        if (manager_->getOnStartGame()) {
            manager_->getOnStartGame()(enemyCount_, avgDiff, mode);
        }
    });

    backBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->popScreen();
        }
    });

    enemyCountGroup_->handleEvent(event, window);
    diff1Group_->handleEvent(event, window);
    if (enemyCount_ >= 2) diff2Group_->handleEvent(event, window);
    if (enemyCount_ >= 3) diff3Group_->handleEvent(event, window);
    modeGroup_->handleEvent(event, window);
    startBtn_->handleEvent(event, window);
    backBtn_->handleEvent(event, window);
}

void LocalSetupScreen::update(float /*dt*/)
{
    // Show/hide difficulty groups based on enemy count
    diff2Group_->setVisible(enemyCount_ >= 2);
    diff3Group_->setVisible(enemyCount_ >= 3);
}

void LocalSetupScreen::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;
    window.clear(sf::Color(10, 0, 20));

    title_->draw(window);
    enemyCountGroup_->draw(window);
    diff1Group_->draw(window);
    if (enemyCount_ >= 2) diff2Group_->draw(window);
    if (enemyCount_ >= 3) diff3Group_->draw(window);
    modeGroup_->draw(window);
    startBtn_->draw(window);
    backBtn_->draw(window);
}

} // namespace neon
