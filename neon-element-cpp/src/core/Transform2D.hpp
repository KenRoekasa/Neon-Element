#pragma once

#include "Vec2.hpp"
#include "Rect.hpp"
#include <cmath>

namespace neon {

struct RotatedRect {
    Rect rect;
    float angle; // degrees
    Vec2 pivot;  // rotation center
};

inline bool intersectsRotated(const RotatedRect& rr, const Rect& target)
{
    // Rotate the target center into the rotated rect's local space
    float rad = -rr.angle * 3.14159265f / 180.f;
    float cosA = std::cos(rad);
    float sinA = std::sin(rad);

    // Translate target center relative to pivot
    float dx = target.x - rr.pivot.x;
    float dy = target.y - rr.pivot.y;

    // Rotate into local space
    float localX = dx * cosA - dy * sinA + rr.pivot.x;
    float localY = dx * sinA + dy * cosA + rr.pivot.y;

    // Now test AABB intersection with unrotated rect
    Rect localTarget{localX, localY, target.width, target.height};
    return intersects(rr.rect, localTarget);
}

} // namespace neon
