#include "AiController.hpp"

namespace neon {

AiController::AiController(Player* aiPlayer,
                           std::vector<std::unique_ptr<PhysicsObject>>& objects,
                           const Rect& map,
                           AiType aiType,
                           ScoreBoard* scoreboard,
                           GameType& gameType,
                           const GameClock& clock)
    : objects_(objects), aiPlayer_(aiPlayer), aiType_(aiType),
      clock_(clock), gameType_(gameType)
{
    calc_ = AiCalculations::create(map, scoreboard, gameType, *this);
    stateActions_ = AiStateActions::create(*calc_, map, gameType, *this, clock);
    fsm_ = FSM::create(aiPlayer, *calc_, gameType, *this);
}

void AiController::pause()
{
    calc_->getTimeCalc().setPaused(true);
}

void AiController::update()
{
    calc_->getTimeCalc().tick();
    fsm_->fetchAction();
    stateActions_->executeAction();
}

} // namespace neon
