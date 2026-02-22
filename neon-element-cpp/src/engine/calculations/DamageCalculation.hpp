#pragma once

#include "engine/entities/Player.hpp"
#include "engine/entities/Character.hpp"

namespace neon {

namespace DamageCalculation {

// Element damage multiplier matrix [attacker][victim]
// Rows: Fire, Water, Earth, Air (attacker)
// Cols: Fire, Water, Earth, Air (victim)
inline constexpr float kElementMatrix[4][4] = {
    // victim: Fire  Water  Earth  Air
    /*Fire*/  {1.0f, 0.5f,  1.0f,  1.5f},
    /*Water*/ {1.5f, 1.0f,  1.0f,  0.5f},
    /*Earth*/ {1.5f, 0.5f,  1.0f,  1.0f},
    /*Air*/   {1.5f, 1.0f,  0.5f,  1.0f}
};

// Shield mitigation matrix [attacker_element][victim_element]
inline constexpr float kShieldMatrix[4][4] = {
    // victim: Fire   Water  Earth  Air
    /*Fire*/  {0.50f, 1.00f, 1.00f, 0.75f},
    /*Water*/ {0.50f, 0.50f, 0.25f, 1.00f},
    /*Earth*/ {0.75f, 0.95f, 0.50f, 0.95f},
    /*Air*/   {1.00f, 0.75f, 0.75f, 0.50f}
};

inline float calculateDamage(float baseDmg, Element attacker, Element victim)
{
    return baseDmg * kElementMatrix[static_cast<int>(attacker)][static_cast<int>(victim)];
}

inline float calculateMitigation(Element attacker, Element victim, bool shielded)
{
    if (shielded)
        return kShieldMatrix[static_cast<int>(attacker)][static_cast<int>(victim)];
    return 1.0f;
}

inline float calculateDealtDamage(const Player& attacker, const Character& victim)
{
    float baseDmg = 0.f;
    float enemyMultiplier = 1.f;

    if (attacker.getCurrentAction() == Action::Light) {
        baseDmg = 3.f;
        if (victim.getTag() == ObjectType::Enemy)
            enemyMultiplier = 5.f;
    } else if (attacker.getCurrentAction() == Action::Heavy) {
        baseDmg = 20.f;
        if (victim.getTag() == ObjectType::Enemy)
            enemyMultiplier = 4.f;
    } else {
        return 0.f;
    }

    float elementDmg = calculateDamage(baseDmg, attacker.getCurrentElement(), victim.getCurrentElement());
    float mitigation = calculateMitigation(attacker.getCurrentElement(), victim.getCurrentElement(), victim.isShielded());

    return elementDmg * mitigation * attacker.getDamageMultiplier() * enemyMultiplier;
}

} // namespace DamageCalculation

} // namespace neon
