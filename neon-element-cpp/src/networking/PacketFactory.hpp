#pragma once

#include "Packet.hpp"
#include "packets/HelloPackets.hpp"
#include "packets/ConnectPackets.hpp"
#include "packets/DisconnectPackets.hpp"
#include "packets/StatePackets.hpp"
#include "packets/BroadcastPackets.hpp"
#include "packets/GamePackets.hpp"
#include <memory>

namespace neon::net {

// Creates a Packet from raw 128-byte datagram
inline std::unique_ptr<Packet> createPacket(const void* raw, size_t len,
                                             const Sender& sender)
{
    if (len < 1) return nullptr;

    PacketBuffer buf(raw, len);
    uint8_t typeId = buf.getUint8(); // consume first byte = packet type

    auto type = packetTypeFromId(typeId);
    if (!type) return nullptr;

    switch (*type) {
        // Client → Server
        case PacketType::Hello:
            return std::make_unique<HelloPacket>(buf, sender);
        case PacketType::Connect:
            return std::make_unique<ConnectPacket>(buf, sender);
        case PacketType::Disconnect:
            return std::make_unique<DisconnectPacket>(buf, sender);
        case PacketType::LocationState:
            return std::make_unique<LocationStatePacket>(buf, sender);
        case PacketType::ActionState:
            return std::make_unique<ActionStatePacket>(buf, sender);
        case PacketType::ElementState:
            return std::make_unique<ElementStatePacket>(buf, sender);
        case PacketType::ReadyState:
            return std::make_unique<ReadyStatePacket>(buf, sender);
        case PacketType::PowerUp:
            return std::make_unique<PowerUpPacket>(buf, sender);
        case PacketType::InitialGameStateAck:
            return std::make_unique<InitialGameStateAckPacket>(buf, sender);

        // Server → Client
        case PacketType::HelloAck:
            return std::make_unique<HelloAckPacket>(buf, sender);
        case PacketType::ConnectAck:
            return std::make_unique<ConnectAckPacket>(buf, sender);
        case PacketType::DisconnectAck:
            return std::make_unique<DisconnectAckPacket>(buf, sender);
        case PacketType::ConnectBroadcast:
            return std::make_unique<ConnectBroadcast>(buf, sender);
        case PacketType::DisconnectBroadcast:
            return std::make_unique<DisconnectBroadcast>(buf, sender);
        case PacketType::ReadyStateBroadcast:
            return std::make_unique<ReadyStateBroadcast>(buf, sender);
        case PacketType::LocationStateBroadcast:
            return std::make_unique<LocationStateBroadcast>(buf, sender);
        case PacketType::ElementStateBroadcast:
            return std::make_unique<ElementStateBroadcast>(buf, sender);
        case PacketType::HealthStateBroadcast:
            return std::make_unique<HealthStateBroadcast>(buf, sender);
        case PacketType::PowerUpPickUpBroadcast:
            return std::make_unique<PowerUpPickUpBroadcast>(buf, sender);
        case PacketType::PowerUpBroadcast:
            return std::make_unique<PowerUpBroadcast>(buf, sender);
        case PacketType::InitialGameStateBroadcast:
            return std::make_unique<InitialGameStateBroadcast>(buf, sender);
        case PacketType::ActionStateBroadcast:
            return std::make_unique<ActionStateBroadcast>(buf, sender);
        case PacketType::RespawnBroadcast:
            return std::make_unique<RespawnBroadcast>(buf, sender);
        case PacketType::ScoreBroadcast:
            return std::make_unique<ScoreBroadcast>(buf, sender);
        case PacketType::GameStartBroadcast:
            return std::make_unique<GameStartBroadcast>(buf, sender);
        case PacketType::GameOverBroadcast:
            return std::make_unique<GameOverBroadcast>(buf, sender);
    }

    return nullptr;
}

} // namespace neon::net
