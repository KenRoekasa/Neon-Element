#pragma once

#include "Map.hpp"
#include "GameType.hpp"
#include "ScoreBoard.hpp"
#include "engine/entities/PhysicsObject.hpp"
#include "engine/entities/Player.hpp"
#include "gametypes/FirstToXKillsGame.hpp"
#include "gametypes/TimedGame.hpp"
#include "gametypes/HillGame.hpp"
#include "gametypes/Regicide.hpp"
#include <vector>
#include <memory>

namespace neon {

class GameState {
public:
    GameState(Map map,
              std::vector<std::unique_ptr<PhysicsObject>> objects,
              ScoreBoard scoreBoard,
              std::unique_ptr<GameType> gameType);
    virtual ~GameState() = default;

    // Accessors
    Map& getMap() { return map_; }
    const Map& getMap() const { return map_; }
    void setMap(Map map) { map_ = std::move(map); }

    std::vector<std::unique_ptr<PhysicsObject>>& getObjects() { return objects_; }
    const std::vector<std::unique_ptr<PhysicsObject>>& getObjects() const { return objects_; }

    GameType& getGameType() { return *gameType_; }
    const GameType& getGameType() const { return *gameType_; }
    void setGameType(std::unique_ptr<GameType> gt) { gameType_ = std::move(gt); }

    ScoreBoard& getScoreBoard() { return scoreBoard_; }
    const ScoreBoard& getScoreBoard() const { return scoreBoard_; }

    std::vector<Player*>& getAllPlayers() { return allPlayers_; }
    const std::vector<Player*>& getAllPlayers() const { return allPlayers_; }

    std::vector<Player*>& getDeadPlayers() { return deadPlayers_; }

    long long getStartTime() const { return startTime_; }
    void setStartTime(long long t) { startTime_ = t; }

    bool getRunning() const { return isRunning_; }
    void start() { isRunning_ = true; }
    void stop() { isRunning_ = false; }

    void rebuildPlayerList();

protected:
    Map map_;
    std::vector<std::unique_ptr<PhysicsObject>> objects_;
    std::unique_ptr<GameType> gameType_;
    ScoreBoard scoreBoard_;
    std::vector<Player*> allPlayers_;
    std::vector<Player*> deadPlayers_;
    long long startTime_ = 0;
    bool isRunning_ = false;
};

} // namespace neon
