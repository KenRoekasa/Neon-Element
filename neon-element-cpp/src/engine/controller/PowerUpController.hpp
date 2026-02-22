#pragma once

#include "engine/model/GameState.hpp"
#include "engine/entities/PowerUp.hpp"
#include "core/Clock.hpp"

namespace neon {

class PowerUpController {
public:
    explicit PowerUpController(GameState& gameState)
        : gameState_(gameState) {}

    void update(const GameClock& clock)
    {
        long long currentTime = clock.elapsedMs();
        if (currentTime - lastTime_ >= kSpawnRate) {
            auto powerUp = std::make_unique<PowerUp>();
            gameState_.getObjects().push_back(std::move(powerUp));
            lastTime_ = currentTime;
        }
    }

private:
    static constexpr long long kSpawnRate = 15000; // 15 seconds
    GameState& gameState_;
    long long lastTime_ = 0;
};

} // namespace neon
