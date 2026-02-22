#pragma once

#include "PhysicsObject.hpp"
#include "CooldownSystem.hpp"
#include "engine/model/enums/Action.hpp"
#include "engine/model/enums/Elements.hpp"
#include "engine/model/AttackTimes.hpp"
#include "core/Circle.hpp"
#include "core/Transform2D.hpp"
#include "core/Clock.hpp"
#include <array>

namespace neon {

class Player; // forward declaration

class Character : public PhysicsObject {
public:
    static constexpr float kDefaultMovementSpeed = 0.35f;
    static constexpr float kMaxHealth = 100.f;

    Character();
    ~Character() override = default;

    // Movement - isometric
    void moveUp();
    void moveDown(float boardWidth, float boardHeight);
    void moveLeft(float boardWidth);
    void moveRight(float boardWidth, float boardHeight);

    // Movement - cartesian
    void moveUpCartesian();
    void moveDownCartesian(float boardHeight);
    void moveLeftCartesian();
    void moveRightCartesian(float boardWidth);

    // Combat
    void lightAttack(const GameClock& clock);
    void chargeHeavyAttack(const GameClock& clock);
    void shield();
    void unShield();
    void takeDamage(float damage, Player* attacker);
    void removeHealth(float damage);

    // Element switching
    void changeToFire(const GameClock& clock);
    void changeToWater(const GameClock& clock);
    void changeToEarth(const GameClock& clock);
    void changeToAir(const GameClock& clock);

    // Power-ups
    void speedBoost(const GameClock& clock);
    void damageBoost(const GameClock& clock);
    void addHealth(int amount);

    // Respawn
    void respawn();

    // Hitbox accessors
    RotatedRect getAttackHitbox() const;
    Circle getHeavyAttackHitbox() const;

    // Getters
    float getHealth() const { return health_; }
    float getMaxHealth() const { return kMaxHealth; }
    Element getCurrentElement() const { return currentElement_; }
    Action getCurrentAction() const { return currentAction_; }
    long long getCurrentActionStart() const { return currentActionStart_; }
    float getMovementSpeed() const { return movementSpeed_; }
    bool isShielded() const { return isShielded_; }
    bool isAlive() const { return isAlive_; }
    float getAngle() const { return angle_; }
    Player* getLastAttacker() const { return lastAttacker_; }
    float getDamageMultiplier() const { return damageMultiplier_; }
    bool activeDamagePowerup() const { return damagePowerup_; }
    int getIframes() const { return iframes_; }
    float getHorizontalMove() const { return horizontalMove_; }
    float getVerticalMove() const { return verticalMove_; }
    long long getDeathTime() const { return deathTime_; }
    double getLightAttackRange() const { return lightAttackRange_; }
    bool hasActionSounded() const { return actionHasSounded_; }

    // Setters
    void setLocation(Vec2 loc) { location_ = loc; }
    void setAngle(float angle) { angle_ = angle; }
    void setCurrentAction(Action action) { currentAction_ = action; }
    void setCurrentElement(Element element) { currentElement_ = element; }
    void setHorizontalMove(float h) { horizontalMove_ = h; }
    void setVerticalMove(float v) { verticalMove_ = v; }
    void setActionHasSounded(bool v) { actionHasSounded_ = v; }

    long long getLastUsed(CooldownItem item) const
    {
        return cooldownTimes_[static_cast<int>(item)];
    }

protected:
    bool checkCD(CooldownItem item, float cooldown, const GameClock& clock);

    float health_ = kMaxHealth;
    Element currentElement_ = Element::Fire;
    float angle_ = 0.f;
    bool isShielded_ = false;
    float movementSpeed_ = kDefaultMovementSpeed;
    bool isAlive_ = true;
    Action currentAction_ = Action::Idle;
    long long deathTime_ = 0;
    bool damagePowerup_ = false;
    int iframes_ = 0;
    float damageMultiplier_ = 1.f;
    bool actionHasSounded_ = false;
    float verticalMove_ = 0.f;
    float horizontalMove_ = 0.f;
    Player* lastAttacker_ = nullptr;
    double lightAttackRange_ = 300.0;

    long long currentActionStart_ = 0;
    std::array<long long, kCooldownItemCount> cooldownTimes_{};

    static constexpr int kHeavyAttackRange = 500;
};

} // namespace neon
