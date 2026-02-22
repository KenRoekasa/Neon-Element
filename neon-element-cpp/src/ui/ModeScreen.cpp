#include "ModeScreen.hpp"
#include "UIManager.hpp"
#include "LocalSetupScreen.hpp"
#include "OnlineModeScreen.hpp"
#include "audio/Sound.hpp"

namespace neon {

ModeScreen::ModeScreen()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (fontLoaded_) initWidgets();
}

void ModeScreen::initWidgets()
{
    title_ = std::make_unique<Label>(font_, "SELECT MODE", 48);
    title_->setPosition(450.f, 80.f);

    localBtn_ = std::make_unique<Button>(font_, "LOCAL", 32);
    localBtn_->setPosition(640.f, 280.f);
    localBtn_->setSize(300.f, 60.f);

    onlineBtn_ = std::make_unique<Button>(font_, "ONLINE", 32);
    onlineBtn_->setPosition(640.f, 380.f);
    onlineBtn_->setSize(300.f, 60.f);

    backBtn_ = std::make_unique<Button>(font_, "BACK", 28);
    backBtn_->setPosition(640.f, 520.f);
    backBtn_->setSize(200.f, 50.f);
}

void ModeScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    auto hoverSound = [this]() {
        if (manager_) manager_->getAudio().playSound(SoundEffect::Hover1, 50.f);
    };

    localBtn_->setOnHover(hoverSound);
    onlineBtn_->setOnHover(hoverSound);
    backBtn_->setOnHover(hoverSound);

    localBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->pushScreen(std::make_unique<LocalSetupScreen>());
        }
    });
    onlineBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->pushScreen(std::make_unique<OnlineModeScreen>());
        }
    });
    backBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->popScreen();
        }
    });

    localBtn_->handleEvent(event, window);
    onlineBtn_->handleEvent(event, window);
    backBtn_->handleEvent(event, window);
}

void ModeScreen::update(float /*dt*/) {}

void ModeScreen::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;
    window.clear(sf::Color(10, 0, 20));
    title_->draw(window);
    localBtn_->draw(window);
    onlineBtn_->draw(window);
    backBtn_->draw(window);
}

} // namespace neon
