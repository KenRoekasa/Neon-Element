#include "Character.hpp"
#include <cmath>
#include <algorithm>

namespace neon {

Character::Character()
{
    // Initialize all cooldown times to far in the past so abilities are available
    cooldownTimes_.fill(-100000LL);
}

// --- Isometric movement ---

void Character::moveUp()
{
    float yCheck = location_.y - movementSpeed_ - static_cast<float>(width_) / 2.f;
    float xCheck = location_.x - movementSpeed_ - static_cast<float>(width_) / 2.f;

    if (yCheck >= 0.f && xCheck >= 0.f) {
        horizontalMove_ = -movementSpeed_;
        verticalMove_ = -movementSpeed_;
    }
}

void Character::moveDown(float boardWidth, float boardHeight)
{
    float yCheck = location_.y + movementSpeed_ + static_cast<float>(width_) / 2.f;
    float xCheck = location_.x + movementSpeed_ + static_cast<float>(width_) / 2.f;

    if (yCheck <= boardHeight && xCheck <= boardWidth) {
        horizontalMove_ = movementSpeed_;
        verticalMove_ = movementSpeed_;
    }
}

void Character::moveLeft(float boardWidth)
{
    float xCheck = location_.x - movementSpeed_ - static_cast<float>(width_) / 2.f;
    float yCheck = location_.y + movementSpeed_ + static_cast<float>(width_) / 2.f;

    if (xCheck >= 0.f && yCheck <= boardWidth) {
        horizontalMove_ = -movementSpeed_;
        verticalMove_ = movementSpeed_;
    }
}

void Character::moveRight(float boardWidth, float boardHeight)
{
    float xCheck = location_.x + movementSpeed_ + static_cast<float>(width_) / 2.f;
    float yCheck = location_.y - movementSpeed_ - static_cast<float>(width_) / 2.f;

    if (xCheck <= boardWidth && yCheck >= 0.f) {
        horizontalMove_ = movementSpeed_;
        verticalMove_ = -movementSpeed_;
    }
}

// --- Cartesian movement ---

void Character::moveUpCartesian()
{
    if ((location_.y - movementSpeed_ - static_cast<float>(width_) / 2.f) >= 0.f) {
        horizontalMove_ = 0.f;
        verticalMove_ = -std::sqrt(2.f * movementSpeed_ * movementSpeed_);
    } else {
        location_.y = static_cast<float>(width_) / 2.f;
    }
}

void Character::moveDownCartesian(float boardHeight)
{
    if ((location_.y + movementSpeed_ + static_cast<float>(width_) / 2.f) <= boardHeight) {
        horizontalMove_ = 0.f;
        verticalMove_ = std::sqrt(2.f * movementSpeed_ * movementSpeed_);
    } else {
        location_.y = boardHeight - static_cast<float>(width_) / 2.f;
    }
}

void Character::moveLeftCartesian()
{
    if ((location_.x - movementSpeed_ - static_cast<float>(width_) / 2.f) >= 0.f) {
        horizontalMove_ = -std::sqrt(2.f * movementSpeed_ * movementSpeed_);
        verticalMove_ = 0.f;
    } else {
        location_.x = static_cast<float>(width_) / 2.f;
    }
}

void Character::moveRightCartesian(float boardWidth)
{
    if ((location_.x + movementSpeed_ + static_cast<float>(width_) / 2.f) <= boardWidth) {
        horizontalMove_ = std::sqrt(2.f * movementSpeed_ * movementSpeed_);
        verticalMove_ = 0.f;
    } else {
        location_.x = boardWidth - static_cast<float>(width_) / 2.f;
    }
}

// --- Combat ---

void Character::lightAttack(const GameClock& clock)
{
    if (checkCD(CooldownItem::Light, cooldown::kLightAttackCD, clock)) {
        if (currentAction_ == Action::Idle) {
            actionHasSounded_ = false;
            currentAction_ = Action::Light;
            currentActionStart_ = clock.elapsedMs();
        }
    }
}

void Character::chargeHeavyAttack(const GameClock& clock)
{
    if (checkCD(CooldownItem::Heavy, cooldown::kHeavyAttackCD, clock)) {
        if (currentAction_ == Action::Idle) {
            actionHasSounded_ = false;
            currentAction_ = Action::Charge;
            currentActionStart_ = clock.elapsedMs();
        }
    }
}

void Character::shield()
{
    if (currentAction_ == Action::Idle) {
        actionHasSounded_ = false;
        currentAction_ = Action::Block;
        isShielded_ = true;
        movementSpeed_ = kDefaultMovementSpeed / 2.f;
    }
}

void Character::unShield()
{
    if (currentAction_ == Action::Block) {
        currentAction_ = Action::Idle;
        movementSpeed_ = kDefaultMovementSpeed;
        isShielded_ = false;
    }
}

void Character::takeDamage(float damage, Player* attacker)
{
    health_ -= damage;
    lastAttacker_ = attacker;
    iframes_ = 10;
}

void Character::removeHealth(float damage)
{
    health_ -= damage;
}

// --- Element switching ---

void Character::changeToFire(const GameClock& clock)
{
    if (currentAction_ == Action::Idle) {
        if (checkCD(CooldownItem::ChangeState, cooldown::kChangeStateCD, clock))
            currentElement_ = Element::Fire;
    }
}

void Character::changeToWater(const GameClock& clock)
{
    if (currentAction_ == Action::Idle) {
        if (checkCD(CooldownItem::ChangeState, cooldown::kChangeStateCD, clock))
            currentElement_ = Element::Water;
    }
}

void Character::changeToEarth(const GameClock& clock)
{
    if (currentAction_ == Action::Idle) {
        if (checkCD(CooldownItem::ChangeState, cooldown::kChangeStateCD, clock))
            currentElement_ = Element::Earth;
    }
}

void Character::changeToAir(const GameClock& clock)
{
    if (currentAction_ == Action::Idle) {
        if (checkCD(CooldownItem::ChangeState, cooldown::kChangeStateCD, clock))
            currentElement_ = Element::Air;
    }
}

// --- Power-ups ---

void Character::speedBoost(const GameClock& clock)
{
    movementSpeed_ = kDefaultMovementSpeed * 2.f;
    cooldownTimes_[static_cast<int>(CooldownItem::Speed)] = clock.elapsedMs();
}

void Character::damageBoost(const GameClock& clock)
{
    damageMultiplier_ = 2.f;
    damagePowerup_ = true;
    cooldownTimes_[static_cast<int>(CooldownItem::Damage)] = clock.elapsedMs();
}

void Character::addHealth(int amount)
{
    health_ += static_cast<float>(amount);
    if (health_ > kMaxHealth)
        health_ = kMaxHealth;
}

void Character::respawn()
{
    isAlive_ = true;
    health_ = kMaxHealth;
    iframes_ = 120;
}

// --- Hitboxes ---

RotatedRect Character::getAttackHitbox() const
{
    float w = static_cast<float>(width_);
    // Attack hitbox extends upward from player center
    Rect hitbox{location_.x, location_.y - static_cast<float>(lightAttackRange_) / 2.f,
                w, static_cast<float>(lightAttackRange_)};
    Vec2 pivot{location_.x + w / 2.f, location_.y + w / 2.f};
    return {hitbox, angle_, pivot};
}

Circle Character::getHeavyAttackHitbox() const
{
    float w = static_cast<float>(width_);
    return {location_.x + w, location_.y + w,
            static_cast<float>(kHeavyAttackRange) / 2.f};
}

// --- Cooldown check ---

bool Character::checkCD(CooldownItem item, float cooldown, const GameClock& clock)
{
    long long elapsed = clock.elapsedMs();
    long long lastUsed = cooldownTimes_[static_cast<int>(item)];
    if (elapsed - lastUsed >= static_cast<long long>(cooldown * 1000.f)) {
        cooldownTimes_[static_cast<int>(item)] = elapsed;
        return true;
    }
    return false;
}

} // namespace neon
