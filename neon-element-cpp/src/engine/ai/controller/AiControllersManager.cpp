#include "AiControllersManager.hpp"

namespace neon {

AiControllersManager::AiControllersManager(std::vector<std::unique_ptr<PhysicsObject>>& objects,
                                           const Rect& map,
                                           ScoreBoard* scoreboard,
                                           GameType& gameType,
                                           const GameClock& clock)
    : objects_(objects), map_(map), scoreboard_(scoreboard),
      gameType_(gameType), clock_(clock)
{
}

Player* AiControllersManager::addAi(AiType type)
{
    auto player = std::make_unique<Player>(ObjectType::Enemy);
    Player* playerPtr = player.get();
    objects_.push_back(std::move(player));

    controllers_.push_back(std::make_unique<AiController>(
        playerPtr, objects_, map_, type, scoreboard_, gameType_, clock_));

    return playerPtr;
}

void AiControllersManager::registerExistingAi(Player* player, AiType type)
{
    controllers_.push_back(std::make_unique<AiController>(
        player, objects_, map_, type, scoreboard_, gameType_, clock_));
}

void AiControllersManager::updateAllAi()
{
    for (auto& con : controllers_)
        con->update();
}

void AiControllersManager::pauseAllAi()
{
    for (auto& con : controllers_)
        con->pause();
}

} // namespace neon
