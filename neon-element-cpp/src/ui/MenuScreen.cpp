#include "MenuScreen.hpp"
#include "UIManager.hpp"
#include "ModeScreen.hpp"
#include "OptionsScreen.hpp"
#include "HelpScreen.hpp"
#include "audio/Sound.hpp"

namespace neon {

MenuScreen::MenuScreen()
{
    fontsLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (fontsLoaded_) {
        titleFont_ = font_;
        initWidgets();
    }
}

void MenuScreen::initWidgets()
{
    title_ = std::make_unique<Label>(titleFont_, "NEON ELEMENT", 72);
    title_->setColor(sf::Color(255, 0, 255));
    title_->centerHorizontally(1280.f);
    title_->setPosition(title_->isVisible() ? 390.f : 0.f, 80.f); // Will re-center in draw

    playBtn_ = std::make_unique<Button>(font_, "PLAY", 32);
    playBtn_->setPosition(640.f, 280.f);
    playBtn_->setSize(300.f, 60.f);

    helpBtn_ = std::make_unique<Button>(font_, "HELP", 32);
    helpBtn_->setPosition(640.f, 370.f);
    helpBtn_->setSize(300.f, 60.f);

    optionsBtn_ = std::make_unique<Button>(font_, "OPTIONS", 32);
    optionsBtn_->setPosition(640.f, 460.f);
    optionsBtn_->setSize(300.f, 60.f);

    exitBtn_ = std::make_unique<Button>(font_, "EXIT", 32);
    exitBtn_->setPosition(640.f, 550.f);
    exitBtn_->setSize(300.f, 60.f);
}

void MenuScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontsLoaded_) return;

    auto hoverSound = [this]() {
        if (manager_) manager_->getAudio().playSound(SoundEffect::Hover1, 50.f);
    };

    playBtn_->setOnHover(hoverSound);
    helpBtn_->setOnHover(hoverSound);
    optionsBtn_->setOnHover(hoverSound);
    exitBtn_->setOnHover(hoverSound);

    playBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->pushScreen(std::make_unique<ModeScreen>());
        }
    });
    helpBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->pushScreen(std::make_unique<HelpScreen>());
        }
    });
    optionsBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->pushScreen(std::make_unique<OptionsScreen>());
        }
    });
    exitBtn_->setOnClick([&window]() {
        window.close();
    });

    playBtn_->handleEvent(event, window);
    helpBtn_->handleEvent(event, window);
    optionsBtn_->handleEvent(event, window);
    exitBtn_->handleEvent(event, window);
}

void MenuScreen::update(float /*dt*/)
{
}

void MenuScreen::draw(sf::RenderWindow& window)
{
    if (!fontsLoaded_) return;

    // Background
    window.clear(sf::Color(10, 0, 20));

    // Title
    title_->centerHorizontally(1280.f);
    title_->setPosition(title_->isVisible() ? 390.f : 0.f, 80.f);
    title_->draw(window);

    playBtn_->draw(window);
    helpBtn_->draw(window);
    optionsBtn_->draw(window);
    exitBtn_->draw(window);
}

} // namespace neon
