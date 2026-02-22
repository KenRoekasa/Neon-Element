#pragma once

#include "Vec2.hpp"
#include <SFML/Graphics/Rect.hpp>

namespace neon {

struct Rect {
    float x;      // center x
    float y;      // center y
    float width;
    float height;

    constexpr Rect() : x(0), y(0), width(0), height(0) {}
    constexpr Rect(float x, float y, float w, float h) : x(x), y(y), width(w), height(h) {}

    constexpr float left()   const { return x - width / 2.f; }
    constexpr float right()  const { return x + width / 2.f; }
    constexpr float top()    const { return y - height / 2.f; }
    constexpr float bottom() const { return y + height / 2.f; }

    constexpr Vec2 center() const { return {x, y}; }

    sf::FloatRect toSFML() const
    {
        return {left(), top(), width, height};
    }

    static Rect fromTopLeft(float x, float y, float w, float h)
    {
        return {x + w / 2.f, y + h / 2.f, w, h};
    }
};

inline bool intersects(const Rect& a, const Rect& b)
{
    return a.left() < b.right() && a.right() > b.left() &&
           a.top() < b.bottom() && a.bottom() > b.top();
}

} // namespace neon
