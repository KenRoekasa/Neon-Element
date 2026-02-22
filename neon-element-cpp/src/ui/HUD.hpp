#pragma once

#include "engine/model/ClientGameState.hpp"
#include "engine/model/GameType.hpp"
#include "engine/model/gametypes/FirstToXKillsGame.hpp"
#include "engine/model/gametypes/TimedGame.hpp"
#include "engine/model/gametypes/HillGame.hpp"
#include "engine/model/gametypes/Regicide.hpp"
#include "engine/entities/CooldownSystem.hpp"
#include "core/Clock.hpp"
#include <SFML/Graphics.hpp>

namespace neon {

class HUD {
public:
    HUD();

    void update(const ClientGameState& state, const GameClock& clock);
    void draw(sf::RenderWindow& window, const ClientGameState& state);

private:
    void drawHealthBar(sf::RenderWindow& window, float x, float y, float w, float h, float ratio);
    void drawCooldownIndicator(sf::RenderWindow& window, float x, float y, float radius,
                                float ratio, const std::string& label);
    void drawLeaderboard(sf::RenderWindow& window, const ClientGameState& state);
    void drawModeInfo(sf::RenderWindow& window, const ClientGameState& state, const GameClock& clock);

    sf::Font font_;
    bool fontLoaded_ = false;

    // Cached values
    float healthRatio_ = 1.f;
    float lightCdRatio_ = 1.f;
    float heavyCdRatio_ = 1.f;
    float changeCdRatio_ = 1.f;
    int kills_ = 0;
    int deaths_ = 0;
};

} // namespace neon
