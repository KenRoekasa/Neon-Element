#pragma once

#include "engine/model/ClientGameState.hpp"
#include "core/Clock.hpp"
#include <SFML/Graphics/RenderWindow.hpp>
#include <SFML/Window.hpp>
#include <cmath>

namespace neon {

class InputHandler {
public:
    void handleInput(sf::RenderWindow& window, ClientGameState& gameState, const GameClock& clock)
    {
        auto* player = gameState.getPlayer();
        if (!player || !player->isAlive()) return;

        float mapW = gameState.getMap().getWidth();
        float mapH = gameState.getMap().getHeight();

        // Movement - WASD (isometric)
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::W))
            player->moveUp();
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::S))
            player->moveDown(mapW, mapH);
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::A))
            player->moveLeft(mapW);
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::D))
            player->moveRight(mapW, mapH);

        // Arrow keys (cartesian)
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Up))
            player->moveUpCartesian();
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Down))
            player->moveDownCartesian(mapH);
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Left))
            player->moveLeftCartesian();
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Right))
            player->moveRightCartesian(mapW);

        // Mouse angle
        sf::Vector2i mousePos = sf::Mouse::getPosition(window);
        float centerX = static_cast<float>(window.getSize().x) / 2.f;
        float centerY = static_cast<float>(window.getSize().y) / 2.f;
        float dx = static_cast<float>(mousePos.x) - centerX;
        float dy = static_cast<float>(mousePos.y) - centerY;
        float angle = std::atan2(dy, dx) * 180.f / 3.14159265f;
        player->setAngle(angle);

        // Combat - mouse buttons
        if (sf::Mouse::isButtonPressed(sf::Mouse::Left))
            player->lightAttack(clock);
        if (sf::Mouse::isButtonPressed(sf::Mouse::Right))
            player->chargeHeavyAttack(clock);
        if (sf::Mouse::isButtonPressed(sf::Mouse::Middle))
            player->shield();
        else if (player->getCurrentAction() == Action::Block)
            player->unShield();

        // Element switching - number keys
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Num1))
            player->changeToFire(clock);
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Num2))
            player->changeToWater(clock);
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Num3))
            player->changeToEarth(clock);
        if (sf::Keyboard::isKeyPressed(sf::Keyboard::Num4))
            player->changeToAir(clock);

        // Leaderboard toggle
        gameState.setTab(sf::Keyboard::isKeyPressed(sf::Keyboard::Tab));
    }

    bool handleEvents(sf::RenderWindow& window, ClientGameState& gameState)
    {
        sf::Event event{};
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
                return false;
            }
            if (event.type == sf::Event::KeyPressed) {
                if (event.key.code == sf::Keyboard::Escape) {
                    window.close();
                    return false;
                }
                if (event.key.code == sf::Keyboard::P) {
                    if (gameState.isPaused())
                        gameState.resume();
                    else
                        gameState.pause();
                }
            }
        }
        return true;
    }
};

} // namespace neon
