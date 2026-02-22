#pragma once

#include "PlayerConnection.hpp"
#include "engine/entities/Player.hpp"
#include <vector>
#include <algorithm>
#include <mutex>

namespace neon::net {

class ConnectedPlayers {
public:
    ConnectedPlayers() = default;

    void addConnection(Player* player, const sf::IpAddress& address, unsigned short port)
    {
        std::lock_guard lock(mutex_);
        connections_.emplace_back(player, address, port);
        playerIds_.push_back(player->getId());
    }

    PlayerConnection* getPlayerConnection(const sf::IpAddress& addr, unsigned short port)
    {
        std::lock_guard lock(mutex_);
        for (auto& conn : connections_) {
            if (conn.is(addr, port)) return &conn;
        }
        return nullptr;
    }

    int count() const
    {
        std::lock_guard lock(mutex_);
        return static_cast<int>(connections_.size());
    }

    bool allHaveInitialGameState() const
    {
        std::lock_guard lock(mutex_);
        return std::all_of(connections_.begin(), connections_.end(),
            [](const PlayerConnection& c) { return c.hasInitialState(); });
    }

    // Assigns starting locations: 4 corners of the map
    void assignStartingLocations(float width, float height)
    {
        std::lock_guard lock(mutex_);
        float positions[][2] = {
            {100.f, 100.f},
            {width - 100.f, 100.f},
            {100.f, height - 100.f},
            {width - 100.f, height - 100.f}
        };
        for (size_t i = 0; i < connections_.size() && i < 4; ++i) {
            connections_[i].getPlayer()->setLocation(positions[i][0], positions[i][1]);
        }
    }

    std::vector<PlayerConnection>& getConnections()
    {
        return connections_;
    }

    const std::vector<int>& getPlayerIds() const { return playerIds_; }

    std::mutex& getMutex() { return mutex_; }

private:
    std::vector<PlayerConnection> connections_;
    std::vector<int> playerIds_;
    mutable std::mutex mutex_;
};

} // namespace neon::net
