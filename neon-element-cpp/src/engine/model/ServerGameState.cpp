#include "ServerGameState.hpp"

namespace neon {

ServerGameState::ServerGameState(Map map,
                                 std::vector<std::unique_ptr<PhysicsObject>> objects,
                                 ScoreBoard scoreBoard,
                                 std::unique_ptr<GameType> gameType,
                                 int numPlayers)
    : GameState(std::move(map), std::move(objects), std::move(scoreBoard), std::move(gameType))
    , numPlayers_(numPlayers)
{
}

} // namespace neon
