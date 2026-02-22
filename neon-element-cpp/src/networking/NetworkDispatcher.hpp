#pragma once

#include "Packet.hpp"
#include "Constants.hpp"
#include <SFML/Network.hpp>
#include <mutex>

namespace neon::net {

// Sends packets via a shared UDP socket. Thread-safe.
class NetworkDispatcher {
public:
    explicit NetworkDispatcher(sf::UdpSocket& socket)
        : socket_(socket) {}

    void send(const Packet& packet, const sf::IpAddress& address, unsigned short port)
    {
        auto buf = packet.serialize();
        std::lock_guard lock(mutex_);
        socket_.send(buf.data(), buf.size(), address, port);
    }

protected:
    sf::UdpSocket& socket_;
    std::mutex mutex_;
};

} // namespace neon::net
