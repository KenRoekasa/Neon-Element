#pragma once

#include "AiController.hpp"
#include "engine/ai/enums/AiType.hpp"
#include <vector>
#include <memory>

namespace neon {

class AiControllersManager {
public:
    AiControllersManager(std::vector<std::unique_ptr<PhysicsObject>>& objects,
                         const Rect& map,
                         ScoreBoard* scoreboard,
                         GameType& gameType,
                         const GameClock& clock);

    Player* addAi(AiType type);
    void registerExistingAi(Player* player, AiType type);
    void updateAllAi();
    void pauseAllAi();

private:
    std::vector<std::unique_ptr<AiController>> controllers_;
    std::vector<std::unique_ptr<PhysicsObject>>& objects_;
    Rect map_;
    ScoreBoard* scoreboard_;
    GameType& gameType_;
    const GameClock& clock_;
};

} // namespace neon
