#include "PauseOverlay.hpp"

namespace neon {

PauseOverlay::PauseOverlay()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (!fontLoaded_) return;

    title_ = std::make_unique<Label>(font_, "PAUSED", 72);
    title_->setColor(sf::Color(255, 0, 255));
    title_->setPosition(490.f, 100.f);

    resumeBtn_ = std::make_unique<Button>(font_, "RESUME", 32);
    resumeBtn_->setPosition(640.f, 300.f);
    resumeBtn_->setSize(300.f, 60.f);

    optionsBtn_ = std::make_unique<Button>(font_, "OPTIONS", 32);
    optionsBtn_->setPosition(640.f, 390.f);
    optionsBtn_->setSize(300.f, 60.f);

    quitBtn_ = std::make_unique<Button>(font_, "QUIT", 32);
    quitBtn_->setPosition(640.f, 480.f);
    quitBtn_->setSize(300.f, 60.f);
    quitBtn_->setColors(sf::Color(120, 0, 0), sf::Color(180, 0, 0), sf::Color(220, 0, 0));
}

void PauseOverlay::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    resumeBtn_->setOnClick([this]() { if (onResume_) onResume_(); });
    optionsBtn_->setOnClick([this]() { if (onOptions_) onOptions_(); });
    quitBtn_->setOnClick([this]() { if (onQuit_) onQuit_(); });

    resumeBtn_->handleEvent(event, window);
    optionsBtn_->handleEvent(event, window);
    quitBtn_->handleEvent(event, window);
}

void PauseOverlay::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    // Semi-transparent overlay
    sf::RectangleShape overlay({1280.f, 720.f});
    overlay.setFillColor(sf::Color(10, 0, 20, 180));
    window.draw(overlay);

    title_->draw(window);
    resumeBtn_->draw(window);
    optionsBtn_->draw(window);
    quitBtn_->draw(window);
}

} // namespace neon
