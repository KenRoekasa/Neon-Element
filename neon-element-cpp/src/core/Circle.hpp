#pragma once

#include "Vec2.hpp"
#include "Rect.hpp"
#include <cmath>

namespace neon {

struct Circle {
    float x;
    float y;
    float radius;

    constexpr Circle() : x(0), y(0), radius(0) {}
    constexpr Circle(float x, float y, float r) : x(x), y(y), radius(r) {}

    constexpr Vec2 center() const { return {x, y}; }
};

inline bool intersects(const Circle& c, const Rect& r)
{
    // Find closest point on rect to circle center
    float closestX = std::max(r.left(), std::min(c.x, r.right()));
    float closestY = std::max(r.top(), std::min(c.y, r.bottom()));

    float dx = c.x - closestX;
    float dy = c.y - closestY;

    return (dx * dx + dy * dy) <= (c.radius * c.radius);
}

inline bool intersects(const Rect& r, const Circle& c)
{
    return intersects(c, r);
}

inline bool intersects(const Circle& a, const Circle& b)
{
    float dx = a.x - b.x;
    float dy = a.y - b.y;
    float rSum = a.radius + b.radius;
    return (dx * dx + dy * dy) <= (rSum * rSum);
}

} // namespace neon
