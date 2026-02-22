#pragma once

#include "networking/Packet.hpp"
#include <vector>

namespace neon::net {

// Server → All: initial game state (map info + player positions)
class InitialGameStateBroadcast : public PacketToClient {
public:
    struct PlayerEntry {
        int id;
        double x;
        double y;
    };

    InitialGameStateBroadcast(float mapWidth, float mapHeight,
                               const std::vector<PlayerEntry>& players)
        : PacketToClient(PacketType::InitialGameStateBroadcast)
        , mapWidth_(mapWidth), mapHeight_(mapHeight)
        , players_(players) {}

    explicit InitialGameStateBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::InitialGameStateBroadcast)
    {
        sender_ = s;
        mapWidth_  = static_cast<float>(buf.getDouble());
        mapHeight_ = static_cast<float>(buf.getDouble());
        int count  = buf.getInt32();
        for (int i = 0; i < count; ++i) {
            PlayerEntry e;
            e.id = buf.getInt32();
            e.x  = buf.getDouble();
            e.y  = buf.getDouble();
            players_.push_back(e);
        }
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putDouble(static_cast<double>(mapWidth_));
        buf.putDouble(static_cast<double>(mapHeight_));
        buf.putInt32(static_cast<int>(players_.size()));
        for (auto& p : players_) {
            buf.putInt32(p.id);
            buf.putDouble(p.x);
            buf.putDouble(p.y);
        }
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    float getMapWidth() const { return mapWidth_; }
    float getMapHeight() const { return mapHeight_; }
    const std::vector<PlayerEntry>& getPlayers() const { return players_; }

private:
    float mapWidth_ = 0;
    float mapHeight_ = 0;
    std::vector<PlayerEntry> players_;
};

// Server → All: game started
class GameStartBroadcast : public PacketToClient {
public:
    GameStartBroadcast(bool started, int playerCount)
        : PacketToClient(PacketType::GameStartBroadcast)
        , started_(started), playerCount_(playerCount) {}

    explicit GameStartBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::GameStartBroadcast)
    {
        sender_ = s;
        started_     = buf.getBool();
        playerCount_ = buf.getInt32();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putBool(started_);
        buf.putInt32(playerCount_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    bool isStarted() const { return started_; }
    int getPlayerCount() const { return playerCount_; }

private:
    bool started_ = false;
    int playerCount_ = 0;
};

// Server → All: game over
class GameOverBroadcast : public PacketToClient {
public:
    explicit GameOverBroadcast(bool gameOver)
        : PacketToClient(PacketType::GameOverBroadcast)
        , gameOver_(gameOver) {}

    explicit GameOverBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::GameOverBroadcast)
    {
        sender_ = s;
        gameOver_ = buf.getBool();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putBool(gameOver_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    bool isGameOver() const { return gameOver_; }

private:
    bool gameOver_ = false;
};

} // namespace neon::net
