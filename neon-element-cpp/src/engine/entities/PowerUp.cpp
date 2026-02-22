#include "PowerUp.hpp"
#include "Character.hpp"
#include <random>

namespace neon {

int PowerUp::nextId_ = 0;

PowerUp::PowerUp()
    : id_(nextId_++)
{
    tag_ = ObjectType::PowerUp;
    width_ = objectSize(ObjectType::PowerUp);
    height_ = width_;

    // Randomize type
    static std::mt19937 rng{std::random_device{}()};
    std::uniform_int_distribution<int> typeDist(0, 2);
    switch (typeDist(rng)) {
        case 0: type_ = PowerUpType::Heal; break;
        case 1: type_ = PowerUpType::Speed; break;
        case 2: type_ = PowerUpType::Damage; break;
    }

    // Random position on 2000x2000 map
    std::uniform_int_distribution<int> posDist(0, 2000);
    location_ = {static_cast<float>(posDist(rng)), static_cast<float>(posDist(rng))};
}

PowerUp::PowerUp(int id, float x, float y, PowerUpType type)
    : id_(id), type_(type)
{
    tag_ = ObjectType::PowerUp;
    width_ = objectSize(ObjectType::PowerUp);
    height_ = width_;
    location_ = {x, y};
}

void PowerUp::activatePowerUp(Character& player, const GameClock& clock)
{
    if (!isActive_)
        return;

    switch (type_) {
        case PowerUpType::Heal:
            player.addHealth(10);
            break;
        case PowerUpType::Speed:
            player.speedBoost(clock);
            break;
        case PowerUpType::Damage:
            player.damageBoost(clock);
            break;
    }
    isActive_ = false;
}

} // namespace neon
