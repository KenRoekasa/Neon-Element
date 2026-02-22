#pragma once

#include "engine/ai/calculations/AiCalculations.hpp"
#include "engine/ai/enums/AiStates.hpp"
#include "core/Clock.hpp"
#include <random>

namespace neon {

class AiController;

class AiActions {
public:
    AiActions(AiController& aiCon, AiCalculations& calc, const Rect& map, const GameClock& clock);

    void assignRandomElement();
    void assignDifferentRandomElement();
    void moveAwayFromPlayer(Player& player);
    void moveAwayFromEdge();
    void moveTo(int powerupIndex, Vec2 loc);
    void moveTo(Player& player);
    void moveToAndKeepDistance(Player& player);
    void simpleMovement(Vec2 myLoc, Vec2 theirLoc);
    void changeToRandomElementAfter(int seconds);
    void startWandering();
    void changeToBefittingElement();
    void attackIfInDistance(Player& player);
    void attackIfInDistanceWithShield(Player& player);
    void shieldWhenAlone();
    void setWanderingDirection(int dir) { wanderingDirection_ = dir; }

private:
    void up();
    void down();
    void left();
    void right();
    void upcart();
    void downcart();
    void leftcart();
    void rightcart();

    static constexpr int kNumberOfTicks = 60;

    AiController& aiCon_;
    Player* aiPlayer_;
    Rect map_;
    const GameClock& clock_;
    AiCalculations& calc_;
    TimeCalculations& timeCalc_;
    MovementCalculations& moveCalc_;
    PlayersCalculations& playerCalc_;
    int wanderingDirection_ = 0;
    std::mt19937 rng_{std::random_device{}()};
};

} // namespace neon
