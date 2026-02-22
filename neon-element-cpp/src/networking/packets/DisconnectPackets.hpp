#pragma once

#include "networking/Packet.hpp"

namespace neon::net {

// Client → Server: disconnect request (no data)
class DisconnectPacket : public PacketToServer {
public:
    DisconnectPacket() : PacketToServer(PacketType::Disconnect) {}

    explicit DisconnectPacket(PacketBuffer& /*buf*/, const Sender& s)
        : PacketToServer(PacketType::Disconnect) { sender_ = s; }

    void handle(ServerNetworkHandler& handler) override;
};

// Server → Client: disconnect acknowledgement
class DisconnectAckPacket : public PacketToClient {
public:
    explicit DisconnectAckPacket(bool allowed)
        : PacketToClient(PacketType::DisconnectAck)
        , allowed_(allowed) {}

    explicit DisconnectAckPacket(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::DisconnectAck)
    {
        sender_ = s;
        allowed_ = buf.getBool();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putBool(allowed_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    bool isAllowed() const { return allowed_; }

private:
    bool allowed_ = true;
};

// Server → All: player disconnected
class DisconnectBroadcast : public PacketToClient {
public:
    DisconnectBroadcast()
        : PacketToClient(PacketType::DisconnectBroadcast) {}

    explicit DisconnectBroadcast(PacketBuffer& /*buf*/, const Sender& s)
        : PacketToClient(PacketType::DisconnectBroadcast) { sender_ = s; }

    void handle(ClientNetworkHandler& handler) override;
};

} // namespace neon::net
