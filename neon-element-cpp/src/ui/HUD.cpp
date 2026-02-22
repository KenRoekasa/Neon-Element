#include "HUD.hpp"
#include <cmath>
#include <sstream>
#include <algorithm>

namespace neon {

HUD::HUD()
{
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");
}

void HUD::update(const ClientGameState& state, const GameClock& clock)
{
    auto* player = state.getPlayer();
    if (!player) return;

    // Health
    healthRatio_ = std::max(0.f, player->getHealth() / 100.f);

    // Cooldowns
    long long elapsed = clock.elapsedMs();
    long long lightLast = player->getLastUsed(CooldownItem::Light);
    long long heavyLast = player->getLastUsed(CooldownItem::Heavy);
    long long changeLast = player->getLastUsed(CooldownItem::ChangeState);

    float lightCdMs = cooldown::kLightAttackCD * 1000.f;
    float heavyCdMs = cooldown::kHeavyAttackCD * 1000.f;
    float changeCdMs = cooldown::kChangeStateCD * 1000.f;

    lightCdRatio_ = std::min(1.f, static_cast<float>(elapsed - lightLast) / lightCdMs);
    heavyCdRatio_ = std::min(1.f, static_cast<float>(elapsed - heavyLast) / heavyCdMs);
    changeCdRatio_ = std::min(1.f, static_cast<float>(elapsed - changeLast) / changeCdMs);

    // Stats
    int playerId = player->getId();
    kills_ = static_cast<int>(state.getScoreBoard().getPlayerKills(playerId));
    deaths_ = static_cast<int>(state.getScoreBoard().getPlayerDeaths(playerId));
}

void HUD::draw(sf::RenderWindow& window, const ClientGameState& state)
{
    if (!fontLoaded_) return;

    // Health bar (top-left)
    drawHealthBar(window, 20.f, 20.f, 250.f, 25.f, healthRatio_);

    // Kills/Deaths text (top-left, below health)
    sf::Text statsText;
    statsText.setFont(font_);
    statsText.setCharacterSize(18);
    statsText.setFillColor(sf::Color::White);
    std::ostringstream oss;
    oss << "K: " << kills_ << "  D: " << deaths_;
    statsText.setString(oss.str());
    statsText.setPosition(20.f, 52.f);
    window.draw(statsText);

    // Cooldown indicators (bottom-left)
    float cdY = 660.f;
    drawCooldownIndicator(window, 50.f, cdY, 22.f, lightCdRatio_, "L");
    drawCooldownIndicator(window, 110.f, cdY, 22.f, heavyCdRatio_, "H");
    drawCooldownIndicator(window, 170.f, cdY, 22.f, changeCdRatio_, "E");

    // Leaderboard (top-right, shown when Tab is held)
    if (state.isTab()) {
        drawLeaderboard(window, state);
    }

    // Mode-specific info (bottom-center)
    GameClock dummyClock;  // We need clock for timed mode
    drawModeInfo(window, state, dummyClock);
}

void HUD::drawHealthBar(sf::RenderWindow& window, float x, float y, float w, float h, float ratio)
{
    // Background
    sf::RectangleShape bg({w, h});
    bg.setPosition(x, y);
    bg.setFillColor(sf::Color(40, 0, 40));
    bg.setOutlineColor(sf::Color(100, 0, 150));
    bg.setOutlineThickness(1.f);
    window.draw(bg);

    // Fill
    sf::Color fillColor;
    if (ratio > 0.5f)
        fillColor = sf::Color(0, 200, 100);
    else if (ratio > 0.25f)
        fillColor = sf::Color(255, 200, 0);
    else
        fillColor = sf::Color(255, 50, 50);

    sf::RectangleShape fill({w * ratio, h});
    fill.setPosition(x, y);
    fill.setFillColor(fillColor);
    window.draw(fill);

    // Text
    if (fontLoaded_) {
        sf::Text text;
        text.setFont(font_);
        text.setCharacterSize(14);
        text.setFillColor(sf::Color::White);
        std::ostringstream oss;
        oss << static_cast<int>(ratio * 100.f) << "%";
        text.setString(oss.str());
        sf::FloatRect bounds = text.getLocalBounds();
        text.setPosition(x + (w - bounds.width) / 2.f, y + 2.f);
        window.draw(text);
    }
}

void HUD::drawCooldownIndicator(sf::RenderWindow& window, float x, float y, float radius,
                                  float ratio, const std::string& label)
{
    // Background circle
    sf::CircleShape bg(radius);
    bg.setOrigin(radius, radius);
    bg.setPosition(x, y);
    bg.setFillColor(sf::Color(30, 0, 40));
    bg.setOutlineColor(sf::Color(80, 0, 120));
    bg.setOutlineThickness(2.f);
    window.draw(bg);

    // Progress arc (simplified as a filled portion)
    if (ratio < 1.f) {
        // Draw a partially filled circle using a ConvexShape
        sf::ConvexShape arc;
        int segments = static_cast<int>(ratio * 30.f);
        if (segments < 2) segments = 2;
        arc.setPointCount(static_cast<size_t>(segments) + 1);
        arc.setPoint(0, {x, y});
        for (int i = 0; i < segments; i++) {
            float angle = -90.f + static_cast<float>(i) / 30.f * 360.f;
            float rad = angle * 3.14159265f / 180.f;
            arc.setPoint(static_cast<size_t>(i) + 1,
                         {x + std::cos(rad) * radius, y + std::sin(rad) * radius});
        }
        arc.setFillColor(sf::Color(150, 0, 200, 150));
        window.draw(arc);
    } else {
        // Full — draw bright indicator
        sf::CircleShape ready(radius - 3.f);
        ready.setOrigin(radius - 3.f, radius - 3.f);
        ready.setPosition(x, y);
        ready.setFillColor(sf::Color(200, 0, 255, 100));
        window.draw(ready);
    }

    // Label
    if (fontLoaded_) {
        sf::Text text;
        text.setFont(font_);
        text.setCharacterSize(14);
        text.setFillColor(sf::Color::White);
        text.setString(label);
        sf::FloatRect bounds = text.getLocalBounds();
        text.setOrigin(bounds.left + bounds.width / 2.f, bounds.top + bounds.height / 2.f);
        text.setPosition(x, y);
        window.draw(text);
    }
}

void HUD::drawLeaderboard(sf::RenderWindow& window, const ClientGameState& state)
{
    // Semi-transparent background
    sf::RectangleShape bg({300.f, 200.f});
    bg.setPosition(960.f, 20.f);
    bg.setFillColor(sf::Color(10, 0, 20, 200));
    bg.setOutlineColor(sf::Color(100, 0, 150));
    bg.setOutlineThickness(1.f);
    window.draw(bg);

    // Header
    sf::Text header;
    header.setFont(font_);
    header.setCharacterSize(20);
    header.setFillColor(sf::Color(255, 0, 255));
    header.setString("LEADERBOARD");
    header.setPosition(1010.f, 25.f);
    window.draw(header);

    // Player entries sorted by score
    auto leaderboard = state.getScoreBoard().getLeaderBoard();
    float entryY = 55.f;
    for (size_t i = 0; i < leaderboard.size() && i < 4; i++) {
        int pid = leaderboard[i];
        sf::Text entry;
        entry.setFont(font_);
        entry.setCharacterSize(16);

        // Highlight current player
        if (state.getPlayer() && pid == state.getPlayer()->getId())
            entry.setFillColor(sf::Color(0, 255, 200));
        else
            entry.setFillColor(sf::Color(200, 200, 200));

        std::ostringstream oss;
        oss << "#" << (i + 1) << " Player " << pid
            << " - Score: " << state.getScoreBoard().getPlayerScore(pid)
            << " K:" << state.getScoreBoard().getPlayerKills(pid)
            << " D:" << state.getScoreBoard().getPlayerDeaths(pid);
        entry.setString(oss.str());
        entry.setPosition(970.f, entryY + static_cast<float>(i) * 35.f);
        window.draw(entry);
    }
}

void HUD::drawModeInfo(sf::RenderWindow& window, const ClientGameState& state, const GameClock& /*clock*/)
{
    if (!fontLoaded_) return;

    sf::Text modeText;
    modeText.setFont(font_);
    modeText.setCharacterSize(18);
    modeText.setFillColor(sf::Color(200, 200, 200));

    std::ostringstream oss;
    switch (state.getMode()) {
        case GameType::Type::FirstToXKills: {
            auto* ft = dynamic_cast<const FirstToXKillsGame*>(&state.getGameType());
            if (ft) oss << "First to " << ft->getKillsNeeded() << " Kills";
            break;
        }
        case GameType::Type::Timed: {
            auto* tg = dynamic_cast<const TimedGame*>(&state.getGameType());
            if (tg) oss << "Timed Mode - " << (tg->getDuration() / 1000) << "s";
            break;
        }
        case GameType::Type::Hill:
            oss << "King of the Hill";
            break;
        case GameType::Type::Regicide:
            oss << "Regicide";
            break;
    }

    modeText.setString(oss.str());
    sf::FloatRect bounds = modeText.getLocalBounds();
    modeText.setPosition(640.f - bounds.width / 2.f, 695.f);
    window.draw(modeText);
}

} // namespace neon
