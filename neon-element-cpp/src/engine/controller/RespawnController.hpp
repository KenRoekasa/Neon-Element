#pragma once

#include "engine/model/GameState.hpp"
#include "engine/entities/Player.hpp"
#include "core/Clock.hpp"
#include <random>

namespace neon {

class RespawnController {
public:
    explicit RespawnController(GameState& gameState)
        : gameState_(gameState), rng_(std::random_device{}()) {}

    void update(const GameClock& clock)
    {
        auto type = gameState_.getGameType().getType();
        switch (type) {
            case GameType::Type::FirstToXKills:
                normalRespawn(5000, clock);
                break;
            case GameType::Type::Timed:
            case GameType::Type::Hill:
                normalRespawn(2500, clock);
                break;
            case GameType::Type::Regicide:
                normalRespawn(1000, clock);
                break;
        }
    }

private:
    void normalRespawn(long long respawnTime, const GameClock& clock)
    {
        auto& deadPlayers = gameState_.getDeadPlayers();
        if (deadPlayers.empty())
            return;

        Player* player = deadPlayers.front();
        long long currentTime = clock.elapsedMs();
        long long playerDeathTime = player->getDeathTime();

        if (currentTime - playerDeathTime >= respawnTime) {
            auto& respawnPoints = gameState_.getMap().getRespawnPoints();
            if (!respawnPoints.empty()) {
                std::uniform_int_distribution<size_t> dist(0, respawnPoints.size() - 1);
                auto& point = respawnPoints[dist(rng_)];
                player->setLocation(point.x, point.y);
            }
            player->respawn();
            deadPlayers.erase(deadPlayers.begin());
        }
    }

    GameState& gameState_;
    std::mt19937 rng_;
};

} // namespace neon
