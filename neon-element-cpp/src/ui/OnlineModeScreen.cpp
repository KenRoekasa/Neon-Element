#include "OnlineModeScreen.hpp"
#include "UIManager.hpp"
#include "LobbyScreen.hpp"
#include "audio/Sound.hpp"

namespace neon {

OnlineModeScreen::OnlineModeScreen()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (fontLoaded_) initWidgets();
}

void OnlineModeScreen::initWidgets()
{
    title_ = std::make_unique<Label>(font_, "ONLINE MODE", 48);
    title_->setPosition(430.f, 80.f);

    hostBtn_ = std::make_unique<Button>(font_, "HOST GAME", 32);
    hostBtn_->setPosition(640.f, 280.f);
    hostBtn_->setSize(300.f, 60.f);

    joinBtn_ = std::make_unique<Button>(font_, "JOIN GAME", 32);
    joinBtn_->setPosition(640.f, 380.f);
    joinBtn_->setSize(300.f, 60.f);

    backBtn_ = std::make_unique<Button>(font_, "BACK", 28);
    backBtn_->setPosition(640.f, 520.f);
    backBtn_->setSize(200.f, 50.f);
}

void OnlineModeScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    auto hoverSound = [this]() {
        if (manager_) manager_->getAudio().playSound(SoundEffect::Hover1, 50.f);
    };

    hostBtn_->setOnHover(hoverSound);
    joinBtn_->setOnHover(hoverSound);
    backBtn_->setOnHover(hoverSound);

    hostBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->pushScreen(std::make_unique<LobbyScreen>(true));
        }
    });
    joinBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->pushScreen(std::make_unique<LobbyScreen>(false));
        }
    });
    backBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->popScreen();
        }
    });

    hostBtn_->handleEvent(event, window);
    joinBtn_->handleEvent(event, window);
    backBtn_->handleEvent(event, window);
}

void OnlineModeScreen::update(float /*dt*/) {}

void OnlineModeScreen::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;
    window.clear(sf::Color(10, 0, 20));
    title_->draw(window);
    hostBtn_->draw(window);
    joinBtn_->draw(window);
    backBtn_->draw(window);
}

} // namespace neon
