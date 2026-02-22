#pragma once

#include "PhysicsObject.hpp"
#include "engine/model/enums/PowerUpType.hpp"
#include "core/Circle.hpp"

namespace neon {

class Character;
class GameClock;

class PowerUp : public PhysicsObject {
public:
    PowerUp();
    PowerUp(int id, float x, float y, PowerUpType type);

    void update(const GameClock& /*clock*/) override {}

    void activatePowerUp(Character& player, const GameClock& clock);

    int getId() const { return id_; }
    PowerUpType getType() const { return type_; }
    bool isActive() const { return isActive_; }
    void setActive(bool active) { isActive_ = active; }

    Circle getCircleBounds() const
    {
        float w = static_cast<float>(width_);
        return {location_.x + w, location_.y + w, w};
    }

private:
    int id_;
    PowerUpType type_;
    bool isActive_ = true;
    static int nextId_;
};

} // namespace neon
