#pragma once

#include "networking/Packet.hpp"
#include "engine/model/enums/Action.hpp"
#include "engine/model/enums/Elements.hpp"

namespace neon::net {

// Client → Server: player position update
class LocationStatePacket : public PacketToServer {
public:
    LocationStatePacket(double x, double y, double angle)
        : PacketToServer(PacketType::LocationState)
        , x_(x), y_(y), angle_(angle) {}

    explicit LocationStatePacket(PacketBuffer& buf, const Sender& s)
        : PacketToServer(PacketType::LocationState)
    {
        sender_ = s;
        x_     = buf.getDouble();
        y_     = buf.getDouble();
        angle_ = buf.getDouble();
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putDouble(x_);
        buf.putDouble(y_);
        buf.putDouble(angle_);
        return buf;
    }

    void handle(ServerNetworkHandler& handler) override;

    double getX() const { return x_; }
    double getY() const { return y_; }
    double getAngle() const { return angle_; }

private:
    double x_ = 0, y_ = 0, angle_ = 0;
};

// Client → Server: action state change
class ActionStatePacket : public PacketToServer {
public:
    explicit ActionStatePacket(Action action)
        : PacketToServer(PacketType::ActionState)
        , action_(action) {}

    explicit ActionStatePacket(PacketBuffer& buf, const Sender& s)
        : PacketToServer(PacketType::ActionState)
    {
        sender_ = s;
        action_ = static_cast<Action>(buf.getUint8());
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putUint8(static_cast<uint8_t>(action_));
        return buf;
    }

    void handle(ServerNetworkHandler& handler) override;

    Action getAction() const { return action_; }

private:
    Action action_ = Action::Idle;
};

// Client → Server: element state change
class ElementStatePacket : public PacketToServer {
public:
    explicit ElementStatePacket(Element element)
        : PacketToServer(PacketType::ElementState)
        , element_(element) {}

    explicit ElementStatePacket(PacketBuffer& buf, const Sender& s)
        : PacketToServer(PacketType::ElementState)
    {
        sender_ = s;
        element_ = static_cast<Element>(buf.getUint8());
    }

    PacketBuffer serialize() const override
    {
        auto buf = Packet::serialize();
        buf.putUint8(static_cast<uint8_t>(element_));
        return buf;
    }

    void handle(ServerNetworkHandler& handler) override;

    Element getElement() const { return element_; }

private:
    Element element_ = Element::Fire;
};

// Client → Server: ready state
class ReadyStatePacket : public PacketToServer {
public:
    explicit ReadyStatePacket(bool ready)
        : PacketToServer(PacketType::ReadyState)
        , ready_(ready) {}

    explicit ReadyStatePacket(PacketBuffer& buf, const Sender& s)
        : PacketToServer(PacketType::ReadyState)
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

    void handle(ServerNetworkHandler& handler) override;

    bool isReady() const { return ready_; }

private:
    bool ready_ = false;
};

// Client → Server: power-up pickup
class PowerUpPacket : public PacketToServer {
public:
    explicit PowerUpPacket(int pickupId)
        : PacketToServer(PacketType::PowerUp)
        , pickupId_(pickupId) {}

    explicit PowerUpPacket(PacketBuffer& buf, const Sender& s)
        : PacketToServer(PacketType::PowerUp)
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

    void handle(ServerNetworkHandler& handler) override;

    int getPickupId() const { return pickupId_; }

private:
    int pickupId_ = 0;
};

// Client → Server: acknowledges receipt of initial game state
class InitialGameStateAckPacket : public PacketToServer {
public:
    InitialGameStateAckPacket()
        : PacketToServer(PacketType::InitialGameStateAck) {}

    explicit InitialGameStateAckPacket(PacketBuffer& /*buf*/, const Sender& s)
        : PacketToServer(PacketType::InitialGameStateAck) { sender_ = s; }

    void handle(ServerNetworkHandler& handler) override;
};

} // namespace neon::net
