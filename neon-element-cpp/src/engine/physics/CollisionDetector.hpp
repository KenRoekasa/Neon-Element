#pragma once

#include "engine/entities/PhysicsObject.hpp"
#include "core/Rect.hpp"
#include "core/Circle.hpp"
#include "core/Transform2D.hpp"

namespace neon {

namespace CollisionDetector {

inline bool checkCollision(const PhysicsObject& obj1, const PhysicsObject& obj2)
{
    return intersects(obj1.getBounds(), obj2.getBounds());
}

inline bool checkCollision(const Rect& a, const Rect& b)
{
    return intersects(a, b);
}

inline bool checkCollision(const Circle& c, const Rect& r)
{
    return intersects(c, r);
}

inline bool checkCollision(const RotatedRect& rr, const Rect& target)
{
    return intersectsRotated(rr, target);
}

} // namespace CollisionDetector

} // namespace neon
