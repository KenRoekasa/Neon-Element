#include "ClientGameState.hpp"

namespace neon {

ClientGameState::ClientGameState(Player* player,
                                 Map map,
                                 std::vector<std::unique_ptr<PhysicsObject>> objects,
                                 ScoreBoard scoreBoard,
                                 std::unique_ptr<GameType> gameType,
                                 GameType::Type mode)
    : GameState(std::move(map), std::move(objects), std::move(scoreBoard), std::move(gameType))
    , player_(player)
    , mode_(mode)
{
}

} // namespace neon
