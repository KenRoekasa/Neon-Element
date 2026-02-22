#include "AiStateActions.hpp"
#include "engine/ai/controller/AiController.hpp"
#include "engine/ai/calculations/AiCalculations.hpp"
#include "engine/model/gametypes/HillGame.hpp"

namespace neon {

AiStateActions::AiStateActions(AiController& aiCon, AiCalculations& calc, AiActions& actions)
    : aiPlayer_(aiCon.getAiPlayer()), aiCon_(aiCon), actions_(actions),
      puCalc_(calc.getPowerupCalc()), playerCalc_(calc.getPlayerCalc()),
      timeCalc_(calc.getTimeCalc())
{
}

std::unique_ptr<AiStateActions> AiStateActions::create(AiCalculations& calc, const Rect& map,
                                                        GameType& gameType, AiController& aiCon,
                                                        const GameClock& clock)
{
    auto actions = std::make_unique<AiActions>(aiCon, calc, map, clock);
    AiActions& actionsRef = *actions;
    // Store actions in the controller - the AiController will own it
    aiCon.setActions(std::move(actions));

    switch (gameType.getType()) {
        case GameType::Type::FirstToXKills:
            return std::make_unique<AiKillsStateActions>(aiCon, calc, actionsRef);
        case GameType::Type::Hill:
            return std::make_unique<AiHillStateActions>(aiCon, calc, actionsRef);
        case GameType::Type::Regicide:
            return std::make_unique<AiRegicideStateActions>(aiCon, calc, actionsRef);
        case GameType::Type::Timed:
            return std::make_unique<AiTimedStateActions>(aiCon, calc, actionsRef);
    }
    return std::make_unique<AiTimedStateActions>(aiCon, calc, actionsRef);
}

void AiStateActions::executeAction()
{
    updateElement();
    updateWandering();

    switch (aiCon_.getActiveState()) {
        case AiState::Attack:           attack(); break;
        case AiState::AggressiveAttack: aggressiveAttack(); break;
        case AiState::FindHealth:       findHealth(); break;
        case AiState::FindDamage:       findDamage(); break;
        case AiState::FindSpeed:        findSpeed(); break;
        case AiState::Escape:           escape(); break;
        case AiState::Wander:           wander(); break;
        case AiState::AttackWinner:     attackWinner(); break;
        case AiState::Idle:             idle(); break;
        default: break;
    }
}

void AiStateActions::idle()
{
    Player* player = playerCalc_.getNearestPlayer();
    if (player) actions_.attackIfInDistanceWithShield(*player);
}

void AiStateActions::wander()
{
    aiPlayer_->unShield();
    updateWandering();
    actions_.startWandering();
}

void AiStateActions::findSpeed()
{
    actions_.shieldWhenAlone();
    int idx = puCalc_.getNearestPowerUp(PowerUpType::Speed);
    if (idx != -1) {
        auto& pups = puCalc_.getPowerups();
        actions_.moveTo(idx, pups[static_cast<size_t>(idx)]->getLocation());
    }
}

void AiStateActions::findDamage()
{
    actions_.shieldWhenAlone();
    int idx = puCalc_.getNearestPowerUp(PowerUpType::Damage);
    if (idx != -1) {
        auto& pups = puCalc_.getPowerups();
        actions_.moveTo(idx, pups[static_cast<size_t>(idx)]->getLocation());
    }
}

void AiStateActions::findHealth()
{
    actions_.shieldWhenAlone();
    int idx = puCalc_.getNearestPowerUp(PowerUpType::Heal);
    if (idx != -1) {
        auto& pups = puCalc_.getPowerups();
        actions_.moveTo(idx, pups[static_cast<size_t>(idx)]->getLocation());
    }
}

void AiStateActions::aggressiveAttack()
{
    aiPlayer_->unShield();
    Player* player = playerCalc_.getNearestPlayer();
    if (!player) return;
    aiPlayer_->chargeHeavyAttack(aiCon_.getClock());
    if (aiCon_.getAiType() == AiType::Hard)
        actions_.moveToAndKeepDistance(*player);
    else
        actions_.moveTo(*player);
    actions_.attackIfInDistance(*player);
}

void AiStateActions::escape()
{
    actions_.shieldWhenAlone();
    Player* player = playerCalc_.getNearestPlayer();
    if (!player) return;
    if (playerCalc_.isTooClose(player->getLocation()))
        actions_.moveAwayFromPlayer(*player);
    else
        wander();
}

void AiStateActions::attack()
{
    aiPlayer_->unShield();
    Player* player = playerCalc_.getNearestPlayer();
    if (!player) return;
    actions_.moveTo(*player);
    actions_.attackIfInDistance(*player);
}

void AiStateActions::attackWinner()
{
    aiPlayer_->unShield();
    Player* player = playerCalc_.getWinningPlayer();
    if (player && player != aiPlayer_) {
        aiPlayer_->chargeHeavyAttack(aiCon_.getClock());
        actions_.moveTo(*player);
        actions_.attackIfInDistance(*player);
    }
}

void AiStateActions::updateElement()
{
    if (aiCon_.getAiType() == AiType::Easy)
        actions_.changeToRandomElementAfter(15);
    else
        actions_.changeToBefittingElement();
}

void AiStateActions::updateWandering()
{
    if (wandering_ && aiCon_.getActiveState() != AiState::Wander)
        wandering_ = false;
    else if ((!wandering_ && aiCon_.getActiveState() == AiState::Wander) || timeCalc_.hasBeenWanderingFor(5)) {
        wandering_ = true;
        std::uniform_int_distribution<int> dist(0, 7);
        actions_.setWanderingDirection(dist(rng_));
    }
}

// --- AiKillsStateActions ---

void AiKillsStateActions::executeAction()
{
    updateElement();
    updateWandering();

    AiState state = aiCon_.getActiveState();
    if (state == AiState::AttackLosing) {
        attackLosing();
        return;
    }
    AiStateActions::executeAction();
}

void AiKillsStateActions::attackLosing()
{
    aiPlayer_->unShield();
    Player* player = playerCalc_.getPlayerWithLowestHealth();
    if (!player) return;
    actions_.moveTo(*player);
    actions_.attackIfInDistance(*player);
}

// --- AiHillStateActions ---

AiHillStateActions::AiHillStateActions(AiController& aiCon, AiCalculations& calc, AiActions& actions)
    : AiStateActions(aiCon, calc, actions),
      hillCalc_(static_cast<HillCalculations&>(calc))
{
}

void AiHillStateActions::executeAction()
{
    updateElement();
    updateWandering();

    AiState state = aiCon_.getActiveState();
    if (state == AiState::GoToHill) { goToHill(); return; }
    if (state == AiState::WanderOnHill) { wanderOnHill(); return; }
    AiStateActions::executeAction();
}

void AiHillStateActions::goToHill()
{
    auto& hillGame = static_cast<HillGame&>(aiCon_.getGameType());
    Circle hill = hillGame.getHill();
    actions_.moveTo(-1, Vec2{hill.x, hill.y});
}

void AiHillStateActions::wanderOnHill()
{
    actions_.startWandering();
    Player* player = playerCalc_.getNearestPlayer();
    if (player) actions_.attackIfInDistance(*player);
}

// --- AiRegicideStateActions ---

AiRegicideStateActions::AiRegicideStateActions(AiController& aiCon, AiCalculations& calc, AiActions& actions)
    : AiStateActions(aiCon, calc, actions),
      regCalc_(static_cast<RegicideCalculations&>(calc))
{
}

void AiRegicideStateActions::executeAction()
{
    updateElement();
    updateWandering();

    AiState state = aiCon_.getActiveState();
    if (state == AiState::AttackKing) { attackKing(); return; }
    AiStateActions::executeAction();
}

void AiRegicideStateActions::attackKing()
{
    aiPlayer_->unShield();
    Player* king = regCalc_.getKing();
    if (!king || king == aiPlayer_) return;
    actions_.moveTo(*king);
    actions_.attackIfInDistance(*king);
}

} // namespace neon
