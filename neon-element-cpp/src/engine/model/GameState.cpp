#include "GameState.hpp"

namespace neon {

GameState::GameState(Map map,
                     std::vector<std::unique_ptr<PhysicsObject>> objects,
                     ScoreBoard scoreBoard,
                     std::unique_ptr<GameType> gameType)
    : map_(std::move(map))
    , objects_(std::move(objects))
    , gameType_(std::move(gameType))
    , scoreBoard_(std::move(scoreBoard))
{
    rebuildPlayerList();
}

void GameState::rebuildPlayerList()
{
    allPlayers_.clear();
    for (auto& obj : objects_) {
        if (auto* player = dynamic_cast<Player*>(obj.get()))
            allPlayers_.push_back(player);
    }
}

} // namespace neon
