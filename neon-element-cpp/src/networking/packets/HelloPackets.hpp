#pragma once

#include "networking/Packet.hpp"
#include "engine/model/GameType.hpp"

namespace neon::net {

// Client → Server: initial handshake inquiry (no data)
class HelloPacket : public PacketToServer {
public:
    HelloPacket() : PacketToServer(PacketType::Hello) {}

    explicit HelloPacket(PacketBuffer& /*buf*/, const Sender& s)
        : PacketToServer(PacketType::Hello) { sender_ = s; }

    void handle(ServerNetworkHandler& handler) override;
};

// Server → Client: handshake response
class HelloAckPacket : public PacketToClient {
public:
    HelloAckPacket(int players, int maxPlayers, GameType::Type gameType, int param)
        : PacketToClient(PacketType::HelloAck)
        , players_(players), maxPlayers_(maxPlayers)
        , gameType_(gameType), gameParam_(param) {}

    explicit HelloAckPacket(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::HelloAck)
    {
        sender_ = s;
        players_    = buf.getInt32();
        maxPlayers_ = buf.getInt32();
        gameType_   = static_cast<GameType::Type>(buf.getUint8());
        gameParam_  = buf.getInt32();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(players_);
        buf.putInt32(maxPlayers_);
        buf.putUint8(static_cast<uint8_t>(gameType_));
        buf.putInt32(gameParam_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayers() const { return players_; }
    int getMaxPlayers() const { return maxPlayers_; }
    GameType::Type getGameType() const { return gameType_; }
    int getGameParam() const { return gameParam_; }

private:
    int players_ = 0;
    int maxPlayers_ = 0;
    GameType::Type gameType_ = GameType::Type::FirstToXKills;
    int gameParam_ = 0;
};

} // namespace neon::net
