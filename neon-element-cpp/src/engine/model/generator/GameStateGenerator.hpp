#pragma once

#include "engine/model/ClientGameState.hpp"
#include "engine/model/ServerGameState.hpp"
#include "engine/model/GameType.hpp"
#include "MapGenerator.hpp"
#include "engine/model/gametypes/FirstToXKillsGame.hpp"
#include "engine/model/gametypes/TimedGame.hpp"
#include "engine/model/gametypes/HillGame.hpp"
#include "engine/model/gametypes/Regicide.hpp"
#include <string>
#include <vector>
#include <memory>

namespace neon {

namespace GameStateGenerator {

inline std::unique_ptr<ClientGameState> createEmptyState()
{
    auto map = MapGenerator::createEmptyMap();
    std::vector<std::unique_ptr<PhysicsObject>> objects;
    ScoreBoard scoreBoard;
    auto gameType = std::make_unique<FirstToXKillsGame>(3);

    return std::make_unique<ClientGameState>(
        nullptr, std::move(map), std::move(objects),
        std::move(scoreBoard), std::move(gameType),
        GameType::Type::FirstToXKills);
}

inline std::unique_ptr<ClientGameState> createDemoGameState(
    int numEnemies, GameType::Type mode)
{
    auto map = MapGenerator::createEmptyMap();

    // Create the player
    auto playerPtr = std::make_unique<Player>(ObjectType::Player);
    playerPtr->setId(4);
    Player* player = playerPtr.get();

    std::vector<std::unique_ptr<PhysicsObject>> objects;
    ScoreBoard scoreBoard;
    std::unique_ptr<GameType> gameType;

    switch (mode) {
        case GameType::Type::FirstToXKills:
            gameType = std::make_unique<FirstToXKillsGame>(10);
            break;
        case GameType::Type::Timed:
            gameType = std::make_unique<TimedGame>(90000LL);
            break;
        case GameType::Type::Hill:
            gameType = std::make_unique<HillGame>(1000.f, 1000.f, 500.f, 100000);
            break;
        case GameType::Type::Regicide:
            gameType = std::make_unique<Regicide>(player, 5000);
            break;
    }

    // Create enemies
    std::vector<Player*> enemies;
    for (int i = 0; i < numEnemies; i++) {
        auto enemy = std::make_unique<Player>(ObjectType::Enemy);
        enemy->setId(i);
        enemies.push_back(enemy.get());
        objects.push_back(std::move(enemy));
    }

    // Set spawn positions
    auto& respawnPoints = map.getRespawnPoints();
    player->setLocation(respawnPoints[0].x, respawnPoints[0].y);
    for (int i = 0; i < numEnemies && i + 1 < static_cast<int>(respawnPoints.size()); i++) {
        enemies[static_cast<size_t>(i)]->setLocation(
            respawnPoints[static_cast<size_t>(i) + 1].x,
            respawnPoints[static_cast<size_t>(i) + 1].y);
    }

    // Add player to objects
    objects.push_back(std::move(playerPtr));

    // Add walls to objects
    for (auto& wall : map.getWalls()) {
        objects.push_back(std::make_unique<Wall>(wall));
    }

    // Set king for Regicide mode
    if (mode == GameType::Type::Regicide && !enemies.empty()) {
        static_cast<Regicide*>(gameType.get())->setKing(enemies[0]);
    }

    auto gameState = std::make_unique<ClientGameState>(
        player, std::move(map), std::move(objects),
        std::move(scoreBoard), std::move(gameType), mode);

    gameState->getScoreBoard().initialise(gameState->getAllPlayers());

    return gameState;
}

// Create a client game state for online mode (empty — populated via network)
inline std::unique_ptr<ClientGameState> createEmptyClientGameState(GameType::Type mode)
{
    auto map = MapGenerator::createEmptyMap();
    std::vector<std::unique_ptr<PhysicsObject>> objects;
    ScoreBoard scoreBoard;
    std::unique_ptr<GameType> gameType;

    switch (mode) {
        case GameType::Type::FirstToXKills:
            gameType = std::make_unique<FirstToXKillsGame>(10);
            break;
        case GameType::Type::Timed:
            gameType = std::make_unique<TimedGame>(90000LL);
            break;
        case GameType::Type::Hill:
            gameType = std::make_unique<HillGame>(1000.f, 1000.f, 500.f, 100000);
            break;
        case GameType::Type::Regicide:
            gameType = std::make_unique<Regicide>(0, 5000);
            break;
    }

    // Add walls to objects
    for (auto& wall : map.getWalls()) {
        objects.push_back(std::make_unique<Wall>(wall));
    }

    return std::make_unique<ClientGameState>(
        nullptr, std::move(map), std::move(objects),
        std::move(scoreBoard), std::move(gameType), mode);
}

// Create a server game state for hosting
inline std::unique_ptr<ServerGameState> createServerGameState(
    int numPlayers, GameType::Type mode)
{
    auto map = MapGenerator::createEmptyMap();
    std::vector<std::unique_ptr<PhysicsObject>> objects;
    ScoreBoard scoreBoard;
    std::unique_ptr<GameType> gameType;

    switch (mode) {
        case GameType::Type::FirstToXKills:
            gameType = std::make_unique<FirstToXKillsGame>(10);
            break;
        case GameType::Type::Timed:
            gameType = std::make_unique<TimedGame>(90000LL);
            break;
        case GameType::Type::Hill:
            gameType = std::make_unique<HillGame>(1000.f, 1000.f, 500.f, 100000);
            break;
        case GameType::Type::Regicide:
            gameType = std::make_unique<Regicide>(0, 5000);
            break;
    }

    // Add walls to objects
    for (auto& wall : map.getWalls()) {
        objects.push_back(std::make_unique<Wall>(wall));
    }

    return std::make_unique<ServerGameState>(
        std::move(map), std::move(objects),
        std::move(scoreBoard), std::move(gameType), numPlayers);
}

} // namespace GameStateGenerator

} // namespace neon
