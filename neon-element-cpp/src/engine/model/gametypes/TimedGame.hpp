#pragma once

#include "engine/model/GameType.hpp"

namespace neon {

class TimedGame : public GameType {
public:
    explicit TimedGame(long long durationMs)
        : GameType(Type::Timed), duration_(durationMs) {}

    long long getDuration() const { return duration_; }

private:
    long long duration_;
};

} // namespace neon
