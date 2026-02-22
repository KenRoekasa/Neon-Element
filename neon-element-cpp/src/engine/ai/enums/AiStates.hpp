#pragma once

#include <cstdint>

namespace neon {

enum class AiState : uint8_t {
    Idle,
    Attack,
    AggressiveAttack,
    Escape,
    FindHealth,
    FindDamage,
    FindSpeed,
    Wander,
    AttackWinner,
    // Hill-specific
    GoToHill,
    WanderOnHill,
    // Regicide-specific
    AttackKing,
    // Kills-specific
    AttackLosing
};

} // namespace neon
