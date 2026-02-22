#pragma once

#include "networking/Packet.hpp"

namespace neon::net {

// Client → Server: join game request (no data)
class ConnectPacket : public PacketToServer {
public:
    ConnectPacket() : PacketToServer(PacketType::Connect) {}

    explicit ConnectPacket(PacketBuffer& /*buf*/, const Sender& s)
        : PacketToServer(PacketType::Connect) { sender_ = s; }

    void handle(ServerNetworkHandler& handler) override;
};

// Server → Client: connect response
class ConnectAckPacket : public PacketToClient {
public:
    enum class Status : uint8_t {
        Success        = 0x00,
        ErrGameStarted = 0x01,
        ErrMaxPlayers  = 0x02,
        ErrUnknown     = 0xFF
    };

    ConnectAckPacket(int playerId, Status status)
        : PacketToClient(PacketType::ConnectAck)
        , playerId_(playerId), status_(status) {}

    explicit ConnectAckPacket(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::ConnectAck)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
        status_   = static_cast<Status>(buf.getUint8());
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        buf.putUint8(static_cast<uint8_t>(status_));
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }
    Status getStatus() const { return status_; }

private:
    int playerId_ = 0;
    Status status_ = Status::ErrUnknown;
};

// Server → All: new player connected
class ConnectBroadcast : public PacketToClient {
public:
    explicit ConnectBroadcast(int playerId)
        : PacketToClient(PacketType::ConnectBroadcast)
        , playerId_(playerId) {}

    explicit ConnectBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::ConnectBroadcast)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }

private:
    int playerId_ = 0;
};

} // namespace neon::net
