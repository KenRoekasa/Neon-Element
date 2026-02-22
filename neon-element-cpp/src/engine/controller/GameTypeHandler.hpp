#pragma once

#include "engine/model/GameState.hpp"
#include "engine/model/GameType.hpp"
#include "engine/model/gametypes/FirstToXKillsGame.hpp"
#include "engine/model/gametypes/TimedGame.hpp"
#include "engine/model/gametypes/HillGame.hpp"
#include "engine/model/gametypes/Regicide.hpp"
#include "core/Clock.hpp"

namespace neon {

namespace GameTypeHandler {

inline bool checkRunning(const GameState& currentGame, const GameClock& clock)
{
    auto& gameType = currentGame.getGameType();

    switch (gameType.getType()) {
        case GameType::Type::Timed: {
            auto& t = static_cast<const TimedGame&>(gameType);
            return clock.elapsedMs() < t.getDuration();
        }
        case GameType::Type::FirstToXKills: {
            auto& t = static_cast<const FirstToXKillsGame&>(gameType);
            auto& lb = currentGame.getScoreBoard().getLeaderBoard();
            if (lb.empty()) return true;
            return currentGame.getScoreBoard().getPlayerScore(lb[0]) < t.getKillsNeeded();
        }
        case GameType::Type::Hill: {
            auto& h = static_cast<const HillGame&>(gameType);
            auto& lb = currentGame.getScoreBoard().getLeaderBoard();
            if (lb.empty()) return true;
            return currentGame.getScoreBoard().getPlayerScore(lb[0]) < h.getScoreNeeded();
        }
        case GameType::Type::Regicide: {
            auto& r = static_cast<const Regicide&>(gameType);
            auto& lb = currentGame.getScoreBoard().getLeaderBoard();
            if (lb.empty()) return true;
            return currentGame.getScoreBoard().getPlayerScore(lb[0]) < r.getScoreNeeded();
        }
    }

    return true;
}

} // namespace GameTypeHandler

} // namespace neon
