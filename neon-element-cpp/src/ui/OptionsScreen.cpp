#include "OptionsScreen.hpp"
#include "UIManager.hpp"
#include "audio/Sound.hpp"

namespace neon {

OptionsScreen::OptionsScreen()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (fontLoaded_) initWidgets();
}

void OptionsScreen::initWidgets()
{
    title_ = std::make_unique<Label>(font_, "OPTIONS", 48);
    title_->setPosition(500.f, 80.f);

    musicSlider_ = std::make_unique<Slider>(font_, "Music Volume", 0.f, 100.f, 50.f);
    musicSlider_->setPosition(400.f, 220.f);
    musicSlider_->setWidth(350.f);

    effectsSlider_ = std::make_unique<Slider>(font_, "Effects Volume", 0.f, 100.f, 100.f);
    effectsSlider_->setPosition(400.f, 320.f);
    effectsSlider_->setWidth(350.f);

    backBtn_ = std::make_unique<Button>(font_, "BACK", 28);
    backBtn_->setPosition(640.f, 500.f);
    backBtn_->setSize(200.f, 50.f);
}

void OptionsScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    musicSlider_->setOnChange([this](float val) {
        if (manager_) manager_->getAudio().setMusicVolume(val);
    });
    effectsSlider_->setOnChange([this](float val) {
        if (manager_) manager_->getAudio().setSoundVolume(val);
    });

    backBtn_->setOnHover([this]() {
        if (manager_) manager_->getAudio().playSound(SoundEffect::Hover1, 50.f);
    });
    backBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->popScreen();
        }
    });

    musicSlider_->handleEvent(event, window);
    effectsSlider_->handleEvent(event, window);
    backBtn_->handleEvent(event, window);
}

void OptionsScreen::update(float /*dt*/) {}

void OptionsScreen::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;
    window.clear(sf::Color(10, 0, 20));
    title_->draw(window);
    musicSlider_->draw(window);
    effectsSlider_->draw(window);
    backBtn_->draw(window);
}

} // namespace neon
