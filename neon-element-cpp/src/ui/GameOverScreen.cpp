#include "GameOverScreen.hpp"
#include <sstream>
#include <algorithm>

namespace neon {

GameOverScreen::GameOverScreen()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
    if (!fontLoaded_) return;

    title_ = std::make_unique<Label>(font_, "GAME OVER", 72);
    title_->setColor(sf::Color(255, 0, 255));
    title_->setPosition(400.f, 40.f);

    playAgainBtn_ = std::make_unique<Button>(font_, "PLAY AGAIN", 28);
    playAgainBtn_->setPosition(500.f, 600.f);
    playAgainBtn_->setSize(250.f, 55.f);
    playAgainBtn_->setColors(sf::Color(0, 100, 50), sf::Color(0, 160, 80), sf::Color(0, 200, 100));

    quitBtn_ = std::make_unique<Button>(font_, "QUIT", 28);
    quitBtn_->setPosition(780.f, 600.f);
    quitBtn_->setSize(250.f, 55.f);
    quitBtn_->setColors(sf::Color(120, 0, 0), sf::Color(180, 0, 0), sf::Color(220, 0, 0));
}

void GameOverScreen::init(const ClientGameState& state)
{
    if (initialized_) return;
    initialized_ = true;

    auto leaderboard = state.getScoreBoard().getLeaderBoard();
    for (int pid : leaderboard) {
        PlayerStats ps;
        ps.id = pid;
        ps.kills = static_cast<int>(state.getScoreBoard().getPlayerKills(pid));
        ps.deaths = static_cast<int>(state.getScoreBoard().getPlayerDeaths(pid));
        ps.score = static_cast<int>(state.getScoreBoard().getPlayerScore(pid));
        stats_.push_back(ps);
    }
}

void GameOverScreen::handleEvent(const sf::Event& event, sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    playAgainBtn_->setOnClick([this]() { if (onPlayAgain_) onPlayAgain_(); });
    quitBtn_->setOnClick([this]() { if (onQuit_) onQuit_(); });

    playAgainBtn_->handleEvent(event, window);
    quitBtn_->handleEvent(event, window);
}

void GameOverScreen::draw(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;

    // Background
    sf::RectangleShape bg({1280.f, 720.f});
    bg.setFillColor(sf::Color(10, 0, 20, 230));
    window.draw(bg);

    title_->draw(window);

    // Leaderboard table
    float tableX = 300.f;
    float tableY = 160.f;

    // Header
    sf::Text header;
    header.setFont(font_);
    header.setCharacterSize(22);
    header.setFillColor(sf::Color(255, 0, 255));

    auto drawCol = [&](const std::string& text, float x) {
        header.setString(text);
        header.setPosition(x, tableY);
        window.draw(header);
    };

    drawCol("Rank", tableX);
    drawCol("Player", tableX + 80.f);
    drawCol("Kills", tableX + 240.f);
    drawCol("Deaths", tableX + 380.f);
    drawCol("Score", tableX + 530.f);

    // Separator
    sf::RectangleShape sep({580.f, 2.f});
    sep.setPosition(tableX, tableY + 32.f);
    sep.setFillColor(sf::Color(100, 0, 150));
    window.draw(sep);

    // Rows
    sf::Text cell;
    cell.setFont(font_);
    cell.setCharacterSize(20);

    for (size_t i = 0; i < stats_.size() && i < 4; i++) {
        float rowY = tableY + 45.f + static_cast<float>(i) * 40.f;

        // Rank 1 gets gold, etc.
        sf::Color rowColor;
        switch (i) {
            case 0: rowColor = sf::Color(255, 215, 0); break;
            case 1: rowColor = sf::Color(192, 192, 192); break;
            case 2: rowColor = sf::Color(205, 127, 50); break;
            default: rowColor = sf::Color(180, 180, 180); break;
        }
        cell.setFillColor(rowColor);

        auto drawCell = [&](const std::string& text, float x) {
            cell.setString(text);
            cell.setPosition(x, rowY);
            window.draw(cell);
        };

        drawCell("#" + std::to_string(i + 1), tableX);
        drawCell("Player " + std::to_string(stats_[i].id), tableX + 80.f);
        drawCell(std::to_string(stats_[i].kills), tableX + 260.f);
        drawCell(std::to_string(stats_[i].deaths), tableX + 410.f);
        drawCell(std::to_string(stats_[i].score), tableX + 550.f);
    }

    playAgainBtn_->draw(window);
    quitBtn_->draw(window);
}

} // namespace neon
