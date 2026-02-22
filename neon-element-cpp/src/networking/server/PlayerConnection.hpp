#pragma once

#include "engine/entities/Player.hpp"
#include <SFML/Network.hpp>

namespace neon::net {

// Wraps a Player with its network identity (IP + port)
class PlayerConnection {
public:
    PlayerConnection(Player* player, const sf::IpAddress& address, unsigned short port)
        : player_(player), address_(address), port_(port) {}

    Player* getPlayer() { return player_; }
    const Player* getPlayer() const { return player_; }
    int getId() const { return player_->getId(); }

    const sf::IpAddress& getAddress() const { return address_; }
    unsigned short getPort() const { return port_; }

    bool is(const sf::IpAddress& addr, unsigned short p) const
    {
        return address_ == addr && port_ == p;
    }

    bool hasInitialState() const { return hasInitialState_; }
    void setHasInitialState() { hasInitialState_ = true; }

private:
    Player* player_;
    sf::IpAddress address_;
    unsigned short port_;
    bool hasInitialState_ = false;
};

} // namespace neon::net
