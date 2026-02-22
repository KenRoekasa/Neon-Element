#pragma once

#include "networking/Packet.hpp"
#include "engine/model/enums/Action.hpp"
#include "engine/model/enums/Elements.hpp"
#include "engine/model/enums/PowerUpType.hpp"
#include <vector>

namespace neon::net {

// Server → All: ready state update
class ReadyStateBroadcast : public PacketToClient {
public:
    explicit ReadyStateBroadcast(bool ready)
        : PacketToClient(PacketType::ReadyStateBroadcast)
        , ready_(ready) {}

    explicit ReadyStateBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::ReadyStateBroadcast)
    {
        sender_ = s;
        ready_ = buf.getBool();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putBool(ready_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    bool isReady() const { return ready_; }

private:
    bool ready_ = false;
};

// Server → All: player position update
class LocationStateBroadcast : public PacketToClient {
public:
    LocationStateBroadcast(int id, double x, double y, double angle)
        : PacketToClient(PacketType::LocationStateBroadcast)
        , playerId_(id), x_(x), y_(y), angle_(angle) {}

    explicit LocationStateBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::LocationStateBroadcast)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
        x_        = buf.getDouble();
        y_        = buf.getDouble();
        angle_    = buf.getDouble();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        buf.putDouble(x_);
        buf.putDouble(y_);
        buf.putDouble(angle_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }
    double getX() const { return x_; }
    double getY() const { return y_; }
    double getAngle() const { return angle_; }

private:
    int playerId_ = 0;
    double x_ = 0, y_ = 0, angle_ = 0;
};

// Server → All: element state update
class ElementStateBroadcast : public PacketToClient {
public:
    ElementStateBroadcast(int id, Element element)
        : PacketToClient(PacketType::ElementStateBroadcast)
        , playerId_(id), element_(element) {}

    explicit ElementStateBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::ElementStateBroadcast)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
        element_  = static_cast<Element>(buf.getUint8());
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        buf.putUint8(static_cast<uint8_t>(element_));
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }
    Element getElement() const { return element_; }

private:
    int playerId_ = 0;
    Element element_ = Element::Fire;
};

// Server → All: health state update
class HealthStateBroadcast : public PacketToClient {
public:
    HealthStateBroadcast(int id, float health)
        : PacketToClient(PacketType::HealthStateBroadcast)
        , playerId_(id), health_(health) {}

    explicit HealthStateBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::HealthStateBroadcast)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
        health_   = buf.getFloat();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        buf.putFloat(health_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }
    float getHealth() const { return health_; }

private:
    int playerId_ = 0;
    float health_ = 0.f;
};

// Server → All: powerup picked up
class PowerUpPickUpBroadcast : public PacketToClient {
public:
    explicit PowerUpPickUpBroadcast(int pickupId)
        : PacketToClient(PacketType::PowerUpPickUpBroadcast)
        , pickupId_(pickupId) {}

    explicit PowerUpPickUpBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::PowerUpPickUpBroadcast)
    {
        sender_ = s;
        pickupId_ = buf.getInt32();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(pickupId_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPickupId() const { return pickupId_; }

private:
    int pickupId_ = 0;
};

// Server → All: new powerup spawned
class PowerUpBroadcast : public PacketToClient {
public:
    PowerUpBroadcast(int id, double x, double y, PowerUpType type)
        : PacketToClient(PacketType::PowerUpBroadcast)
        , id_(id), x_(x), y_(y), type_(type) {}

    explicit PowerUpBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::PowerUpBroadcast)
    {
        sender_ = s;
        id_   = buf.getInt32();
        x_    = buf.getDouble();
        y_    = buf.getDouble();
        type_ = static_cast<PowerUpType>(buf.getUint8());
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(id_);
        buf.putDouble(x_);
        buf.putDouble(y_);
        buf.putUint8(static_cast<uint8_t>(type_));
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getId() const { return id_; }
    double getX() const { return x_; }
    double getY() const { return y_; }
    PowerUpType getPowerUpType() const { return type_; }

private:
    int id_ = 0;
    double x_ = 0, y_ = 0;
    PowerUpType type_ = PowerUpType::Heal;
};

// Server → All: action state broadcast
class ActionStateBroadcast : public PacketToClient {
public:
    ActionStateBroadcast(int id, Action action)
        : PacketToClient(PacketType::ActionStateBroadcast)
        , playerId_(id), action_(action) {}

    explicit ActionStateBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::ActionStateBroadcast)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
        action_   = static_cast<Action>(buf.getUint8());
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        buf.putUint8(static_cast<uint8_t>(action_));
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }
    Action getAction() const { return action_; }

private:
    int playerId_ = 0;
    Action action_ = Action::Idle;
};

// Server → All: player respawned
class RespawnBroadcast : public PacketToClient {
public:
    RespawnBroadcast(int id, double x, double y)
        : PacketToClient(PacketType::RespawnBroadcast)
        , playerId_(id), x_(x), y_(y) {}

    explicit RespawnBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::RespawnBroadcast)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
        x_        = buf.getDouble();
        y_        = buf.getDouble();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        buf.putDouble(x_);
        buf.putDouble(y_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }
    double getX() const { return x_; }
    double getY() const { return y_; }

private:
    int playerId_ = 0;
    double x_ = 0, y_ = 0;
};

// Server → All: score update
class ScoreBroadcast : public PacketToClient {
public:
    ScoreBroadcast(int id, int score, int kills, int victimId)
        : PacketToClient(PacketType::ScoreBroadcast)
        , playerId_(id), score_(score), kills_(kills), victimId_(victimId) {}

    explicit ScoreBroadcast(PacketBuffer& buf, const Sender& s)
        : PacketToClient(PacketType::ScoreBroadcast)
    {
        sender_ = s;
        playerId_ = buf.getInt32();
        score_    = buf.getInt32();
        kills_    = buf.getInt32();
        victimId_ = buf.getInt32();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putInt32(playerId_);
        buf.putInt32(score_);
        buf.putInt32(kills_);
        buf.putInt32(victimId_);
        return buf;
    }

    void handle(ClientNetworkHandler& handler) override;

    int getPlayerId() const { return playerId_; }
    int getScore() const { return score_; }
    int getKills() const { return kills_; }
    int getVictimId() const { return victimId_; }

private:
    int playerId_ = 0;
    int score_ = 0;
    int kills_ = 0;
    int victimId_ = 0;
};

} // namespace neon::net
