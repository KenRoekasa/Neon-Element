#pragma once

#include "AiActions.hpp"
#include "engine/ai/enums/AiStates.hpp"
#include "engine/ai/enums/AiType.hpp"
#include <memory>
#include <random>

namespace neon {

class AiController;

class AiStateActions {
public:
    AiStateActions(AiController& aiCon, AiCalculations& calc, AiActions& actions);
    virtual ~AiStateActions() = default;

    static std::unique_ptr<AiStateActions> create(AiCalculations& calc, const Rect& map,
                                                   GameType& gameType, AiController& aiCon,
                                                   const GameClock& clock);

    virtual void executeAction();

protected:
    virtual void idle();
    virtual void wander();
    virtual void findSpeed();
    virtual void findDamage();
    virtual void findHealth();
    virtual void aggressiveAttack();
    virtual void escape();
    virtual void attack();
    virtual void attackWinner();
    void updateElement();
    void updateWandering();

    Player* aiPlayer_;
    AiController& aiCon_;
    AiActions& actions_;
    PowerupCalculations& puCalc_;
    PlayersCalculations& playerCalc_;
    TimeCalculations& timeCalc_;
    bool wandering_ = false;
    std::mt19937 rng_{std::random_device{}()};
};

// Game-mode specific state actions

class AiKillsStateActions : public AiStateActions {
public:
    using AiStateActions::AiStateActions;
    void executeAction() override;

protected:
    void attackLosing();
};

class AiTimedStateActions : public AiStateActions {
public:
    using AiStateActions::AiStateActions;
};

class AiHillStateActions : public AiStateActions {
public:
    AiHillStateActions(AiController& aiCon, AiCalculations& calc, AiActions& actions);
    void executeAction() override;

protected:
    void goToHill();
    void wanderOnHill();
    HillCalculations& hillCalc_;
};

class AiRegicideStateActions : public AiStateActions {
public:
    AiRegicideStateActions(AiController& aiCon, AiCalculations& calc, AiActions& actions);
    void executeAction() override;

protected:
    void attackKing();
    RegicideCalculations& regCalc_;
};

} // namespace neon
