#pragma once

#include "ISOConverter.hpp"
#include "ColorSwitch.hpp"
#include "textures/TextureLoader.hpp"
#include "engine/model/ClientGameState.hpp"
#include "engine/entities/PowerUp.hpp"
#include "core/Clock.hpp"
#include <SFML/Graphics.hpp>
#include <vector>
#include <random>

namespace neon {

class Renderer {
public:
    Renderer(float width, float height);

    void render(sf::RenderWindow& window, ClientGameState& gameState, const GameClock& clock);

    TextureLoader& getTextures() { return textures_; }

    static long long mapInRange(long long x, long long fromLow, long long fromHigh, long long toLow, long long toHigh);

private:
    void calculateOffset(ClientGameState& gameState);
    void drawBackground(sf::RenderWindow& window);
    void drawMap(sf::RenderWindow& window, ClientGameState& gameState);
    void drawWall(sf::RenderWindow& window, const PhysicsObject& wall, const Player& player);
    void drawPowerUp(sf::RenderWindow& window, const PowerUp& powerUp, const Player& player);
    void drawPlayer(sf::RenderWindow& window, const Character& character, bool isClient);
    void drawPlayerRect(sf::RenderWindow& window, Vec2 screenPos, const Character& character);
    void drawHill(sf::RenderWindow& window, const Player& player, const Circle& hill);
    void drawLightAttack(sf::RenderWindow& window, const Character& character, Vec2 screenPos,
                         long long remainAnim, long long animDuration);
    void drawHeavyAttack(sf::RenderWindow& window, const Character& character, Vec2 screenPos,
                         long long remainAnim, long long animDuration);
    void drawHeavyCharge(sf::RenderWindow& window, const Character& character, Vec2 screenPos,
                         long long remainAnim, long long animDuration);
    void drawShield(sf::RenderWindow& window, const Character& character, Vec2 screenPos);
    void drawActionState(sf::RenderWindow& window, const Character& character, Vec2 screenPos,
                         const GameClock& clock);
    void drawDeathScreen(sf::RenderWindow& window);

    Vec2 getScreenPos(Vec2 worldPos, const Player& player) const;
    Vec2 getPlayerScreenPos() const;

    float width_, height_;
    Vec2 stageCenter_;
    Vec2 offset_{0.f, 0.f};

    TextureLoader textures_;
    sf::Font font_;
    bool fontLoaded_ = false;

    std::vector<Vec2> stars_;
};

} // namespace neon
