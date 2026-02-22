#pragma once

#include "Character.hpp"

namespace neon {

class Player : public Character {
public:
    explicit Player(ObjectType type, int id);
    explicit Player(ObjectType type);

    void update(const GameClock& clock) override;

    void doAction(Action action, const GameClock& clock);

    int getId() const { return id_; }
    void setId(int id) { id_ = id; }
    void setLocation(float x, float y) { location_ = {x, y}; }
    void setHealth(float health) { health_ = health; }

private:
    int id_;
    static int nextId_;
};

} // namespace neon
