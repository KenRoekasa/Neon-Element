#pragma once

#include <cstdint>

namespace neon {

enum class ObjectType : uint8_t {
    Enemy   = 0,
    Obstacle = 1,
    Player  = 2,
    PowerUp = 3
};

inline constexpr int objectSize(ObjectType type)
{
    switch (type) {
        case ObjectType::Enemy:    return 60;
        case ObjectType::Obstacle: return 100;
        case ObjectType::Player:   return 60;
        case ObjectType::PowerUp:  return 20;
    }
    return 60;
}

} // namespace neon
