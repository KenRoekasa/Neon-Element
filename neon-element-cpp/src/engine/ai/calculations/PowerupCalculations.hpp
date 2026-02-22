#pragma once

#include "PlayersCalculations.hpp"
#include "engine/entities/PowerUp.hpp"
#include <vector>

namespace neon {

class AiController;

class PowerupCalculations {
public:
    PowerupCalculations(AiController& aiCon, const Rect& map, PlayersCalculations& playerCalc);

    int getNearestPowerUp();
    int getNearestPowerUp(PowerUpType type);
    bool powerUpExist(PowerUpType type);
    std::vector<PowerUp*>& getPowerups();
    bool powerupCloserThanPlayer();
    bool powerupIsTooClose();

private:
    void updatePowerups();
    bool isTooClose(Vec2 loc) const;

    AiController& aiCon_;
    Player* aiPlayer_;
    Rect map_;
    PlayersCalculations& playerCalc_;
    std::vector<PowerUp*> powerups_;
};

} // namespace neon
