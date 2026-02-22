#pragma once

#include "Screen.hpp"
#include "audio/AudioManager.hpp"
#include "engine/model/GameType.hpp"
#include <SFML/Graphics.hpp>
#include <vector>
#include <memory>
#include <functional>
#include <string>

namespace neon {

// Forward declarations for screen types
class GameClient;

// UIManager manages a stack of UI screens
class UIManager {
public:
    UIManager(sf::RenderWindow& window, AudioManager& audio);

    void pushScreen(std::unique_ptr<Screen> screen);
    void popScreen();
    void replaceScreen(std::unique_ptr<Screen> screen);
    void clearScreens();

    void handleEvent(const sf::Event& event);
    void update(float dt);
    void draw();

    bool hasScreens() const { return !screens_.empty(); }
    Screen* currentScreen() { return screens_.empty() ? nullptr : screens_.back().get(); }

    sf::RenderWindow& getWindow() { return window_; }
    AudioManager& getAudio() { return audio_; }

    // Callback for when the UI wants to launch a local game
    void setOnStartGame(std::function<void(int, int, GameType::Type)> cb) { onStartGame_ = std::move(cb); }
    std::function<void(int, int, GameType::Type)>& getOnStartGame() { return onStartGame_; }

    // Callback for when the UI wants to launch an online game
    // Parameters: isHost, serverAddress, gameMode, numPlayers
    using OnlineGameCallback = std::function<void(bool, const std::string&, GameType::Type, int)>;
    void setOnStartOnlineGame(OnlineGameCallback cb) { onStartOnlineGame_ = std::move(cb); }
    OnlineGameCallback& getOnStartOnlineGame() { return onStartOnlineGame_; }

private:
    sf::RenderWindow& window_;
    AudioManager& audio_;
    std::vector<std::unique_ptr<Screen>> screens_;
    std::function<void(int, int, GameType::Type)> onStartGame_;
    OnlineGameCallback onStartOnlineGame_;
};

} // namespace neon
