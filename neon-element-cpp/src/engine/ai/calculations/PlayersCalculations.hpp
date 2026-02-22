#pragma once

#include "MovementCalculations.hpp"
#include "engine/model/ScoreBoard.hpp"
#include "engine/model/GameType.hpp"
#include <vector>

namespace neon {

class AiController;

class PlayersCalculations {
public:
    PlayersCalculations(AiController& aiCon, const Rect& map,
                        ScoreBoard* scoreboard, MovementCalculations& moveCalc);

    bool playerIsTooClose();
    bool scoreDifferenceIsMoreThan(int score);
    double getScore(Player& player);
    bool someoneCloseIsCharging();
    bool inAttackDistance(Player& player);
    bool isCharging(Player& player) const;
    bool isTooClose(Vec2 loc) const;
    bool isNearestPlayer(Player& player);
    Player* getNearestPlayer();
    std::vector<Player*> getOtherPlayers();
    std::vector<Player*> getAllPlayers();
    std::vector<Player*> getRealPlayers();
    Player* getPlayerWithLowestHealth();
    Player* getWinningPlayer();

private:
    AiController& aiCon_;
    Player* aiPlayer_;
    Rect map_;
    ScoreBoard* scoreboard_;
    MovementCalculations& moveCalc_;
};

} // namespace neon
