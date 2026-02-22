#pragma once

#include "core/Vec2.hpp"
#include "core/Rect.hpp"
#include "engine/entities/Player.hpp"
#include <cmath>

namespace neon {

class AiController;

class MovementCalculations {
public:
    MovementCalculations(AiController& aiCon, const Rect& map);

    bool reachedAnEdge() const;
    int closestEdgeLocation() const;
    double calcAngle(Vec2 loc1, Vec2 loc2) const;
    double calcDistance(Vec2 a, Vec2 b) const;

private:
    Player* aiPlayer_;
    Rect map_;
};

} // namespace neon
