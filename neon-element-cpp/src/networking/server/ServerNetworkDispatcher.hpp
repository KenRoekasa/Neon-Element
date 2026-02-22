#pragma once

#include "networking/NetworkDispatcher.hpp"
#include "ConnectedPlayers.hpp"
#include "networking/packets/HelloPackets.hpp"
#include "networking/packets/ConnectPackets.hpp"
#include "networking/packets/DisconnectPackets.hpp"
#include "networking/packets/BroadcastPackets.hpp"
#include "networking/packets/GamePackets.hpp"
#include "engine/model/ServerGameState.hpp"
#include "engine/entities/PowerUp.hpp"

namespace neon::net {

class ServerNetworkDispatcher : public NetworkDispatcher {
public:
    ServerNetworkDispatcher(sf::UdpSocket& socket,
                             ServerGameState& state,
                             ConnectedPlayers& players)
        : NetworkDispatcher(socket)
        , state_(state), players_(players) {}

    // Send to a single client
    void sendTo(const Packet& packet, const sf::IpAddress& addr, unsigned short port)
    {
        send(packet, addr, port);
    }

    // Send to all connected clients
    void broadcast(const Packet& packet)
    {
        std::lock_guard lock(players_.getMutex());
        for (auto& conn : players_.getConnections()) {
            send(packet, conn.getAddress(), conn.getPort());
        }
    }

    // --- High-level broadcast methods ---

    void broadcastGameState()
    {
        std::vector<InitialGameStateBroadcast::PlayerEntry> entries;
        {
            std::lock_guard lock(players_.getMutex());
            for (auto& conn : players_.getConnections()) {
                auto* p = conn.getPlayer();
                entries.push_back({
                    p->getId(),
                    static_cast<double>(p->getLocation().x),
                    static_cast<double>(p->getLocation().y)
                });
            }
        }
        InitialGameStateBroadcast pkt(
            state_.getMap().getWidth(),
            state_.getMap().getHeight(),
            entries);
        broadcast(pkt);
    }

    void broadcastGameStarted()
    {
        GameStartBroadcast pkt(true, players_.count());
        broadcast(pkt);
    }

    void broadcastGameEnded()
    {
        GameOverBroadcast pkt(true);
        broadcast(pkt);
    }

    void broadcastLocationState(int id, double x, double y, double angle)
    {
        LocationStateBroadcast pkt(id, x, y, angle);
        broadcast(pkt);
    }

    void broadcastElementState(int id, Element element)
    {
        ElementStateBroadcast pkt(id, element);
        broadcast(pkt);
    }

    void broadcastHealthState(int id, float health)
    {
        HealthStateBroadcast pkt(id, health);
        broadcast(pkt);
    }

    void broadcastActionState(int id, Action action)
    {
        ActionStateBroadcast pkt(id, action);
        broadcast(pkt);
    }

    void broadcastConnectedUser(int id)
    {
        ConnectBroadcast pkt(id);
        broadcast(pkt);
    }

    void broadcastRespawn(int id, double x, double y)
    {
        RespawnBroadcast pkt(id, x, y);
        broadcast(pkt);
    }

    void broadcastScore(int id, int score, int kills, int victimId)
    {
        ScoreBroadcast pkt(id, score, kills, victimId);
        broadcast(pkt);
    }

    void broadcastNewPowerUp(const PowerUp& pu)
    {
        PowerUpBroadcast pkt(
            pu.getId(),
            static_cast<double>(pu.getLocation().x),
            static_cast<double>(pu.getLocation().y),
            pu.getType());
        broadcast(pkt);
    }

    void broadcastPowerUpPickUp(int pickupId)
    {
        PowerUpPickUpBroadcast pkt(pickupId);
        broadcast(pkt);
    }

private:
    ServerGameState& state_;
    ConnectedPlayers& players_;
};

} // namespace neon::net
