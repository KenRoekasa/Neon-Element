#pragma once

#include <cstdint>

namespace neon {

enum class CooldownItem : uint8_t {
    Speed,
    Damage,
    Light,
    Heavy,
    ChangeState
};

inline constexpr int kCooldownItemCount = 5;

namespace cooldown {
    inline constexpr float kSpeedBoostDuration  = 4.0f;   // seconds
    inline constexpr float kLightAttackCD       = 0.30f;  // seconds
    inline constexpr float kHeavyAttackCD       = 7.5f;   // seconds
    inline constexpr float kChangeStateCD       = 1.5f;   // seconds
    inline constexpr float kDamageBoostDuration = 5.0f;   // seconds
} // namespace cooldown

} // namespace neon
