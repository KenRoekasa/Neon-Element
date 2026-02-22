#include "PowerupCalculations.hpp"
#include "engine/ai/controller/AiController.hpp"
#include <limits>

namespace neon {

PowerupCalculations::PowerupCalculations(AiController& aiCon, const Rect& map, PlayersCalculations& playerCalc)
    : aiCon_(aiCon), aiPlayer_(aiCon.getAiPlayer()), map_(map), playerCalc_(playerCalc)
{
}

int PowerupCalculations::getNearestPowerUp()
{
    auto& pups = getPowerups();
    int index = -1;
    double minDist = std::numeric_limits<double>::max();
    for (int i = 0; i < static_cast<int>(pups.size()); i++) {
        double d = static_cast<double>(distance(pups[static_cast<size_t>(i)]->getLocation(), aiPlayer_->getLocation()));
        if (d < minDist) {
            minDist = d;
            index = i;
        }
    }
    return index;
}

int PowerupCalculations::getNearestPowerUp(PowerUpType type)
{
    auto& pups = getPowerups();
    int index = -1;
    double minDist = std::numeric_limits<double>::max();
    for (int i = 0; i < static_cast<int>(pups.size()); i++) {
        if (pups[static_cast<size_t>(i)]->getType() == type) {
            double d = static_cast<double>(distance(pups[static_cast<size_t>(i)]->getLocation(), aiPlayer_->getLocation()));
            if (d < minDist) {
                minDist = d;
                index = i;
            }
        }
    }
    return index;
}

bool PowerupCalculations::powerUpExist(PowerUpType type)
{
    auto& pups = getPowerups();
    for (auto* p : pups) {
        if (p->getType() == type) return true;
    }
    return false;
}

std::vector<PowerUp*>& PowerupCalculations::getPowerups()
{
    updatePowerups();
    return powerups_;
}

bool PowerupCalculations::powerupCloserThanPlayer()
{
    int puIdx = getNearestPowerUp();
    if (puIdx == -1) return false;
    float distPu = distance(powerups_[static_cast<size_t>(puIdx)]->getLocation(), aiPlayer_->getLocation());
    Player* nearest = playerCalc_.getNearestPlayer();
    if (!nearest) return false;
    float distPlayer = distance(nearest->getLocation(), aiPlayer_->getLocation());
    return distPu < distPlayer;
}

bool PowerupCalculations::powerupIsTooClose()
{
    int idx = getNearestPowerUp();
    if (idx != -1 && isTooClose(powerups_[static_cast<size_t>(idx)]->getLocation()))
        return true;
    return false;
}

void PowerupCalculations::updatePowerups()
{
    powerups_.clear();
    auto& objects = aiCon_.getObjects();
    for (auto& obj : objects) {
        if (obj->getTag() == ObjectType::PowerUp) {
            powerups_.push_back(static_cast<PowerUp*>(obj.get()));
        }
    }
}

bool PowerupCalculations::isTooClose(Vec2 loc) const
{
    return distance(aiPlayer_->getLocation(), loc) < map_.width * 0.2f;
}

} // namespace neon
