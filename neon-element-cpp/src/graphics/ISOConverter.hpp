#pragma once

#include "core/Vec2.hpp"
#include <SFML/Graphics/RenderTarget.hpp>
#include <SFML/Graphics/Transform.hpp>
#include <cmath>

namespace neon {

namespace ISOConverter {

// Creates isometric transformation: 45 degree rotation + Y scale by 0.5
inline sf::Transform getIsoTransform(Vec2 center)
{
    sf::Transform t;
    t.translate(center);
    t.rotate(45.f);
    t.scale(1.f, 0.5f);
    t.translate(-center);
    return t;
}

// Get screen position of an object after isometric conversion
inline Vec2 toScreen(Vec2 worldPos, Vec2 playerPos, Vec2 stageCenter, Vec2 offset)
{
    float relX = stageCenter.x - playerPos.x + worldPos.x + offset.x;
    float relY = stageCenter.y - playerPos.y + worldPos.y + offset.y;
    return {relX, relY};
}

inline Vec2 playerScreenPos(Vec2 stageCenter, Vec2 offset)
{
    return {stageCenter.x + offset.x, stageCenter.y + offset.y};
}

// Apply rotation to a transform at a pivot point
inline sf::Transform rotationTransform(float angleDeg, Vec2 pivot)
{
    sf::Transform t;
    t.translate(pivot);
    t.rotate(angleDeg);
    t.translate(-pivot);
    return t;
}

} // namespace ISOConverter

} // namespace neon
