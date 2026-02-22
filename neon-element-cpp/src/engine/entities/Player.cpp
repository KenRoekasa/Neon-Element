#include "Player.hpp"
#include "core/Clock.hpp"

namespace neon {

int Player::nextId_ = 1;

Player::Player(ObjectType type, int id)
    : id_(id)
{
    location_ = {0.f, 0.f};
    angle_ = 0.f;
    health_ = kMaxHealth;
    movementSpeed_ = kDefaultMovementSpeed;
    isShielded_ = false;
    currentElement_ = Element::Fire;
    tag_ = type;
    width_ = objectSize(type);
    height_ = width_;
    actionHasSounded_ = false;
    lightAttackRange_ = static_cast<double>(width_) * 4.0;

    // Initialize cooldown map with values far in the past
    cooldownTimes_[static_cast<int>(CooldownItem::Light)] = -10000LL;
    cooldownTimes_[static_cast<int>(CooldownItem::Heavy)] = -1000000LL;
    cooldownTimes_[static_cast<int>(CooldownItem::ChangeState)] = -10000LL;
    cooldownTimes_[static_cast<int>(CooldownItem::Damage)] = 0LL;
    cooldownTimes_[static_cast<int>(CooldownItem::Speed)] = 0LL;
}

Player::Player(ObjectType type)
    : Player(type, (type == ObjectType::Player) ? nextId_++ : 0)
{
}

void Player::update(const GameClock& clock)
{
    // Death check
    if (health_ <= 0.f) {
        if (isAlive_) {
            isAlive_ = false;
            deathTime_ = clock.elapsedMs();
        }
    } else {
        isAlive_ = true;
    }

    // Apply movement with delta time
    float dt = clock.deltaTimeScaled();
    location_.x += horizontalMove_ * dt;
    location_.y += verticalMove_ * dt;
    horizontalMove_ = 0.f;
    verticalMove_ = 0.f;

    // Decrease iframes
    iframes_--;

    // Handle action timers (replaces Java's thread-based timers)
    if (currentAction_ == Action::Light || currentAction_ == Action::Heavy) {
        long long duration = attackDurationMs(currentAction_);
        if (clock.elapsedMs() - currentActionStart_ >= duration) {
            currentAction_ = Action::Idle;
        }
    } else if (currentAction_ == Action::Charge) {
        long long duration = attackDurationMs(Action::Charge);
        if (clock.elapsedMs() - currentActionStart_ >= duration) {
            // Charge complete -> transition to heavy attack
            actionHasSounded_ = false;
            currentAction_ = Action::Heavy;
            currentActionStart_ = clock.elapsedMs();
        }
    }

    // Speed boost expiry
    long long speedStart = cooldownTimes_[static_cast<int>(CooldownItem::Speed)];
    if (clock.elapsedMs() - speedStart >= static_cast<long long>(cooldown::kSpeedBoostDuration * 1000.f)) {
        movementSpeed_ = kDefaultMovementSpeed;
    }

    // Damage boost expiry
    long long damageStart = cooldownTimes_[static_cast<int>(CooldownItem::Damage)];
    if (clock.elapsedMs() - damageStart >= static_cast<long long>(cooldown::kDamageBoostDuration * 1000.f)) {
        damageMultiplier_ = 1.f;
        damagePowerup_ = false;
    }
}

void Player::doAction(Action action, const GameClock& clock)
{
    switch (action) {
        case Action::Light:
            lightAttack(clock);
            break;
        case Action::Charge:
            chargeHeavyAttack(clock);
            break;
        case Action::Block:
            shield();
            break;
        case Action::Idle:
            setCurrentAction(Action::Idle);
            break;
        default:
            break;
    }
}

} // namespace neon
