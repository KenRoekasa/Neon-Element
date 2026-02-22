#pragma once

#include "GameState.hpp"

namespace neon {

class ServerGameState : public GameState {
public:
    ServerGameState(Map map,
                    std::vector<std::unique_ptr<PhysicsObject>> objects,
                    ScoreBoard scoreBoard,
                    std::unique_ptr<GameType> gameType,
                    int numPlayers);

    int getNumPlayers() const { return numPlayers_; }
    bool isStarted() const { return isStarted_; }
    void setStarted(bool started) { isStarted_ = started; }

private:
    int numPlayers_;
    bool isStarted_ = false;
};

} // namespace neon
