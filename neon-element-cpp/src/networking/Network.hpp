#pragma once

#include "Packet.hpp"
#include "PacketFactory.hpp"
#include "Constants.hpp"
#include <SFML/Network.hpp>
#include <thread>
#include <atomic>
#include <functional>

namespace neon::net {

// Abstract base: runs a jthread that receives UDP datagrams and dispatches them.
// Subclasses implement parse() to handle incoming packets.
class Network {
public:
    virtual ~Network() { stop(); }

    void start()
    {
        if (running_.exchange(true)) return;
        thread_ = std::jthread([this](std::stop_token token) {
            receiveLoop(token);
        });
    }

    void stop()
    {
        if (!running_.exchange(false)) return;
        thread_.request_stop();
        // Unblock the socket by setting a short timeout
        socket_.setBlocking(false);
        if (thread_.joinable())
            thread_.join();
    }

    sf::UdpSocket& getSocket() { return socket_; }

protected:
    // Bind socket before calling start()
    bool bind(unsigned short port)
    {
        return socket_.bind(port) == sf::Socket::Done;
    }

    // Bind to any available port
    bool bindAny()
    {
        return socket_.bind(sf::Socket::AnyPort) == sf::Socket::Done;
    }

    // Subclass: handle a deserialized packet
    virtual void parse(std::unique_ptr<Packet> packet) = 0;

    sf::UdpSocket socket_;

private:
    void receiveLoop(std::stop_token token)
    {
        std::array<char, PACKET_SIZE> buffer{};

        while (!token.stop_requested() && running_.load()) {
            std::size_t received = 0;
            sf::IpAddress remoteAddr;
            unsigned short remotePort = 0;

            socket_.setBlocking(true);
            auto status = socket_.receive(buffer.data(), PACKET_SIZE,
                                           received, remoteAddr, remotePort);

            if (token.stop_requested() || !running_.load()) break;

            if (status == sf::Socket::Done && received > 0) {
                Sender sender{remoteAddr, remotePort};
                auto packet = createPacket(buffer.data(), received, sender);
                if (packet) {
                    parse(std::move(packet));
                }
            }
        }
    }

    std::jthread thread_;
    std::atomic<bool> running_{false};
};

} // namespace neon::net
