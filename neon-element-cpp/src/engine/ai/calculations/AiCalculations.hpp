#pragma once

#include "MovementCalculations.hpp"
#include "PlayersCalculations.hpp"
#include "PowerupCalculations.hpp"
#include "TimeCalculations.hpp"
#include "engine/model/GameType.hpp"
#include "engine/model/ScoreBoard.hpp"
#include "core/Rect.hpp"
#include <memory>

namespace neon {

class AiController;

class AiCalculations {
public:
    AiCalculations(AiController& aiCon, const Rect& map, ScoreBoard* scoreboard, GameType& gameType);
    virtual ~AiCalculations() = default;

    static std::unique_ptr<AiCalculations> create(const Rect& map, ScoreBoard* scoreboard,
                                                   GameType& gameType, AiController& aiCon);

    PlayersCalculations& getPlayerCalc() { return playerCalc_; }
    MovementCalculations& getMoveCalc() { return moveCalc_; }
    PowerupCalculations& getPowerupCalc() { return puCalc_; }
    TimeCalculations& getTimeCalc() { return timeCalc_; }

protected:
    GameType& gameType_;
    ScoreBoard* scoreboard_;
    Player* aiPlayer_;
    Rect map_;
    MovementCalculations moveCalc_;
    PlayersCalculations playerCalc_;
    PowerupCalculations puCalc_;
    TimeCalculations timeCalc_;
};

// State-specific subclasses (thin wrappers for factory pattern)
class KillsCalculations : public AiCalculations {
public:
    using AiCalculations::AiCalculations;
};

class TimedCalculations : public AiCalculations {
public:
    using AiCalculations::AiCalculations;
};

class HillCalculations : public AiCalculations {
public:
    HillCalculations(AiController& aiCon, const Rect& map, ScoreBoard* scoreboard, GameType& gameType);

    bool onHill() const;
    bool closeToHill() const;
    Player* getOnHillPlayer();
};

class RegicideCalculations : public AiCalculations {
public:
    RegicideCalculations(AiController& aiCon, const Rect& map, ScoreBoard* scoreboard, GameType& gameType);

    Player* getKing();
    bool kingIsClose();
};

} // namespace neon
