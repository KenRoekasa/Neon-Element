#pragma once

#include "engine/ai/enums/AiStates.hpp"
#include "engine/ai/enums/AiType.hpp"
#include "engine/ai/calculations/AiCalculations.hpp"
#include "engine/ai/fsm/FSM.hpp"
#include "engine/ai/actions/AiStateActions.hpp"
#include "engine/ai/actions/AiActions.hpp"
#include "engine/entities/Player.hpp"
#include "engine/model/GameType.hpp"
#include "engine/model/ScoreBoard.hpp"
#include "core/Clock.hpp"
#include <vector>
#include <memory>

namespace neon {

class AiController {
public:
    AiController(Player* aiPlayer,
                 std::vector<std::unique_ptr<PhysicsObject>>& objects,
                 const Rect& map,
                 AiType aiType,
                 ScoreBoard* scoreboard,
                 GameType& gameType,
                 const GameClock& clock);

    void pause();
    void update();

    void setState(AiState s) { activeState_ = s; }
    AiState getActiveState() const { return activeState_; }
    Player* getAiPlayer() { return aiPlayer_; }
    AiType getAiType() const { return aiType_; }
    std::vector<std::unique_ptr<PhysicsObject>>& getObjects() { return objects_; }
    GameType& getGameType() { return gameType_; }
    const GameClock& getClock() const { return clock_; }

    void setActions(std::unique_ptr<AiActions> actions) { actions_ = std::move(actions); }

private:
    AiState activeState_ = AiState::Idle;
    std::vector<std::unique_ptr<PhysicsObject>>& objects_;
    Player* aiPlayer_;
    AiType aiType_;
    const GameClock& clock_;
    GameType& gameType_;

    std::unique_ptr<AiCalculations> calc_;
    std::unique_ptr<FSM> fsm_;
    std::unique_ptr<AiStateActions> stateActions_;
    std::unique_ptr<AiActions> actions_; // owned here
};

} // namespace neon
