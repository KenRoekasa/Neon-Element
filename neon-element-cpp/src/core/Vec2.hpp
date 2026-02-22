#pragma once

#include <SFML/System/Vector2.hpp>
#include <cmath>

namespace neon {

using Vec2 = sf::Vector2f;

inline float distance(Vec2 a, Vec2 b)
{
    float dx = b.x - a.x;
    float dy = b.y - a.y;
    return std::sqrt(dx * dx + dy * dy);
}

inline float length(Vec2 v)
{
    return std::sqrt(v.x * v.x + v.y * v.y);
}

inline Vec2 normalize(Vec2 v)
{
    float len = length(v);
    if (len < 1e-6f)
        return {0.f, 0.f};
    return {v.x / len, v.y / len};
}

inline float dot(Vec2 a, Vec2 b)
{
    return a.x * b.x + a.y * b.y;
}

} // namespace neon
