#pragma once

#include "core/Vec2.hpp"
#include "core/Rect.hpp"
#include "engine/entities/Wall.hpp"
#include <vector>
#include <memory>

namespace neon {

class Map {
public:
    Map(Rect ground, std::vector<Wall> walls, std::vector<Vec2> respawnPoints)
        : ground_(ground), walls_(std::move(walls)), respawnPoints_(std::move(respawnPoints)) {}

    float getWidth() const { return ground_.width; }
    float getHeight() const { return ground_.height; }
    const Rect& getGround() const { return ground_; }
    std::vector<Wall>& getWalls() { return walls_; }
    const std::vector<Wall>& getWalls() const { return walls_; }
    const std::vector<Vec2>& getRespawnPoints() const { return respawnPoints_; }

private:
    Rect ground_;
    std::vector<Wall> walls_;
    std::vector<Vec2> respawnPoints_;
};

} // namespace neon
