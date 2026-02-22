#pragma once

#include "core/Vec2.hpp"
#include "core/Rect.hpp"
#include "engine/model/enums/ObjectType.hpp"

namespace neon {

class GameClock;

class PhysicsObject {
public:
    virtual ~PhysicsObject() = default;

    virtual void update(const GameClock& clock) = 0;

    Vec2 getLocation() const { return location_; }
    int getWidth() const { return width_; }
    int getHeight() const { return height_; }
    ObjectType getTag() const { return tag_; }

    Rect getBounds() const
    {
        return {location_.x, location_.y,
                static_cast<float>(width_), static_cast<float>(height_)};
    }

protected:
    int width_ = 0;
    int height_ = 0;
    Vec2 location_{0.f, 0.f};
    ObjectType tag_ = ObjectType::Player;
};

} // namespace neon
