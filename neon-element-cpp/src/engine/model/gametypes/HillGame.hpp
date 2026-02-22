#pragma once

#include "engine/model/GameType.hpp"
#include "core/Circle.hpp"

namespace neon {

class HillGame : public GameType {
public:
    HillGame(Circle hill, int scoreNeeded)
        : GameType(Type::Hill), hill_(hill), scoreNeeded_(scoreNeeded) {}

    HillGame(float centerX, float centerY, float radius, int scoreNeeded)
        : HillGame(Circle{centerX, centerY, radius}, scoreNeeded) {}

    int getScoreNeeded() const { return scoreNeeded_; }
    Circle getHill() const { return hill_; }

private:
    Circle hill_;
    int scoreNeeded_;
};

} // namespace neon
