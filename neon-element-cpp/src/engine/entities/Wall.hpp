#pragma once

#include "PhysicsObject.hpp"

namespace neon {

class Wall : public PhysicsObject {
public:
    Wall(Vec2 location, int width, int height);
    Wall(float x, float y, int width, int height);

    void update(const GameClock& /*clock*/) override {}
};

} // namespace neon
