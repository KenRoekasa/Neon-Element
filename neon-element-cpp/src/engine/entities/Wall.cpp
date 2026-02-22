#include "Wall.hpp"

namespace neon {

Wall::Wall(Vec2 location, int w, int h)
{
    tag_ = ObjectType::Obstacle;
    width_ = w;
    height_ = h;
    // Store center position (Java passes top-left, shifts to center)
    location_ = {location.x + static_cast<float>(w) / 2.f,
                 location.y + static_cast<float>(h) / 2.f};
}

Wall::Wall(float x, float y, int w, int h)
    : Wall(Vec2{x, y}, w, h)
{
}

} // namespace neon
