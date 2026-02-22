#pragma once

#include "engine/model/Map.hpp"

namespace neon {

namespace MapGenerator {

inline Map createEmptyMap()
{
    std::vector<Vec2> respawnPoints = {
        {50.f, 50.f},
        {50.f, 1950.f},
        {1950.f, 50.f},
        {1950.f, 1950.f}
    };
    return Map(Rect{1000.f, 1000.f, 2000.f, 2000.f}, {}, std::move(respawnPoints));
}

inline Map createMap1()
{
    std::vector<Wall> walls;
    walls.emplace_back(100.f, 100.f, 700, 100);
    walls.emplace_back(100.f, 200.f, 100, 600);
    walls.emplace_back(100.f, 1200.f, 100, 600);
    walls.emplace_back(100.f, 1800.f, 700, 100);

    walls.emplace_back(1200.f, 100.f, 700, 100);
    walls.emplace_back(1800.f, 200.f, 100, 600);
    walls.emplace_back(1800.f, 1200.f, 100, 600);
    walls.emplace_back(1200.f, 1800.f, 700, 100);

    walls.emplace_back(400.f, 400.f, 300, 300);
    walls.emplace_back(400.f, 1300.f, 300, 300);
    walls.emplace_back(1300.f, 400.f, 300, 300);
    walls.emplace_back(1300.f, 1300.f, 300, 300);

    walls.emplace_back(800.f, 800.f, 400, 400);

    std::vector<Vec2> respawnPoints = {
        {50.f, 50.f},
        {50.f, 1950.f},
        {1950.f, 50.f},
        {1950.f, 1950.f}
    };

    return Map(Rect{1000.f, 1000.f, 2000.f, 2000.f}, std::move(walls), std::move(respawnPoints));
}

} // namespace MapGenerator

} // namespace neon
