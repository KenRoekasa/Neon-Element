#include "LobbyScreen.hpp"
#include "UIManager.hpp"
#include "audio/Sound.hpp"

namespace neon {

LobbyScreen::LobbyScreen(bool isHost)
    : isHost_(isHost)
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (fontLoaded_) initWidgets();
}

void LobbyScreen::initWidgets()
{
    title_ = std::make_unique<Label>(font_, isHost_ ? "HOST LOBBY" : "JOIN LOBBY", 48);
    title_->setPosition(430.f, 40.f);

    statusLabel_ = std::make_unique<Label>(font_, "Waiting for players...", 24);
    statusLabel_->setColor(sf::Color(200, 200, 200));
    statusLabel_->setPosition(430.f, 130.f);

    // Show local IP for the host
    std::string ipText = "IP: " + sf::IpAddress::getLocalAddress().toString();
    ipLabel_ = std::make_unique<Label>(font_, ipText, 20);
    ipLabel_->setColor(sf::Color(150, 150, 150));
    ipLabel_->setPosition(430.f, 170.f);

    if (isHost_) {
        startBtn_ = std::make_unique<Button>(font_, "START GAME", 32);
        startBtn_->setPosition(640.f, 550.f);
        startBtn_->setSize(300.f, 60.f);
        startBtn_->setColors(sf::Color(0, 100, 50), sf::Color(0, 160, 80), sf::Color(0, 200, 100));
    }

    backBtn_ = std::make_unique<Button>(font_, "BACK", 28);
    backBtn_->setPosition(640.f, 640.f);
    backBtn_->setSize(200.f, 50.f);
}

void LobbyScreen::addPlayer(const std::string& name)
{
    playerNames_.push_back(name);
    auto label = std::make_unique<Label>(font_, name, 26);
    label->setColor(sf::Color(200, 200, 200));
    label->setPosition(480.f, 220.f + static_cast<float>(playerLabels_.size()) * 40.f);
    playerLabels_.push_back(std::move(label));

    statusLabel_->setText("Players: " + std::to_string(playerNames_.size()) + "/4");
}

void LobbyScreen::startNetworking()
{
    if (networkStarted_) return;
    networkStarted_ = true;

    if (manager_) {
        auto& cb = manager_->getOnStartOnlineGame();
        if (cb) {
            // Launch online game through UIManager callback
            cb(isHost_, "localhost", GameType::Type::FirstToXKills, 2);
        }
    }
}

void LobbyScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    auto hoverSound = [this]() {
        if (manager_) manager_->getAudio().playSound(SoundEffect::Hover1, 50.f);
    };

    if (startBtn_) {
        startBtn_->setOnHover(hoverSound);
        startBtn_->setOnClick([this]() {
            if (manager_) manager_->getAudio().playSound(SoundEffect::Button);
            startNetworking();
        });
        startBtn_->handleEvent(event, window);
    }

    backBtn_->setOnHover(hoverSound);
    backBtn_->setOnClick([this]() {
        if (manager_) {
            manager_->getAudio().playSound(SoundEffect::Button);
            manager_->popScreen();
        }
    });
    backBtn_->handleEvent(event, window);
}

void LobbyScreen::update(float /*dt*/) {}

void LobbyScreen::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;
    window.clear(sf::Color(10, 0, 20));

    title_->draw(window);
    statusLabel_->draw(window);
    ipLabel_->draw(window);

    for (auto& label : playerLabels_)
        label->draw(window);

    if (startBtn_) startBtn_->draw(window);
    backBtn_->draw(window);
}

} // namespace neon
