#pragma once

#include "Widget.hpp"
#include "engine/model/ClientGameState.hpp"
#include <SFML/Graphics.hpp>
#include <memory>
#include <functional>

namespace neon {

class GameOverScreen {
public:
    GameOverScreen();

    void init(const ClientGameState& state);
    void handleEvent(const sf::Event& event, sf::RenderWindow& window);
    void draw(sf::RenderWindow& window);

    void setOnPlayAgain(std::function<void()> cb) { onPlayAgain_ = std::move(cb); }
    void setOnQuit(std::function<void()> cb) { onQuit_ = std::move(cb); }

private:
    sf::Font font_;
    bool fontLoaded_ = false;
    bool initialized_ = false;

    std::unique_ptr<Label> title_;
    std::unique_ptr<Button> playAgainBtn_;
    std::unique_ptr<Button> quitBtn_;

    // Leaderboard data
    struct PlayerStats {
        int id;
        int kills;
        int deaths;
        int score;
    };
    std::vector<PlayerStats> stats_;

    std::function<void()> onPlayAgain_;
    std::function<void()> onQuit_;
};

} // namespace neon
