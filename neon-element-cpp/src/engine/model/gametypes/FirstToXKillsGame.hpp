#pragma once

#include "engine/model/GameType.hpp"

namespace neon {

class FirstToXKillsGame : public GameType {
public:
    explicit FirstToXKillsGame(int killsNeeded)
        : GameType(Type::FirstToXKills), killsNeeded_(killsNeeded) {}

    int getKillsNeeded() const { return killsNeeded_; }

private:
    int killsNeeded_;
};

} // namespace neon
