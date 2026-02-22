#pragma once

#include "core/Clock.hpp"

namespace neon {

class GameState;

class PhysicsController {
public:
    explicit PhysicsController(GameState& gameState);

    void clientLoop(const GameClock& clock);
    void serverLoop(const GameClock& clock);
    void dumbClientLoop(const GameClock& clock);

private:
    void doUpdates(const GameClock& clock);
    void doCollisionDetection(const GameClock& clock);
    void doHitDetection();
    void deathHandler(const GameClock& clock);
    void kingOfHillHandler(const GameClock& clock);
    void regicideHandler(const GameClock& clock);

    GameState& gameState_;
    long long lastTimeRegi_ = 0;
};

} // namespace neon
