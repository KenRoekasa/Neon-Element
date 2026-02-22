#pragma once

#include <cstdint>
#include <optional>

namespace neon {

enum class PowerUpType : uint8_t {
    Heal   = 1,
    Speed  = 2,
    Damage = 3
};

inline std::optional<PowerUpType> powerUpTypeFromId(uint8_t id)
{
    switch (id) {
        case 1: return PowerUpType::Heal;
        case 2: return PowerUpType::Speed;
        case 3: return PowerUpType::Damage;
        default: return std::nullopt;
    }
}

} // namespace neon
