#pragma once

#include <cstdint>
#include <optional>

namespace neon::net {

enum class PacketType : uint8_t {
    // Client → Server
    Hello              = 0x00,
    HelloAck           = 0x01,
    Connect            = 0x02,
    ConnectAck         = 0x03,
    Disconnect         = 0x04,
    DisconnectAck      = 0x05,
    ReadyState         = 0x06,
    LocationState      = 0x08,
    ElementState       = 0x0A,
    PowerUp            = 0x0E,
    ActionState        = 0x10,
    InitialGameStateAck = 0x13,

    // Server → Client (broadcasts)
    ConnectBroadcast          = 0xF0,
    DisconnectBroadcast       = 0xF1,
    ReadyStateBroadcast       = 0xF2,
    LocationStateBroadcast    = 0xF3,
    ElementStateBroadcast     = 0xF4,
    HealthStateBroadcast      = 0xF5,
    PowerUpPickUpBroadcast    = 0xF6,
    PowerUpBroadcast          = 0xF7,
    InitialGameStateBroadcast = 0xF8,
    ActionStateBroadcast      = 0xF9,
    RespawnBroadcast          = 0xFA,
    ScoreBroadcast            = 0xFB,
    GameStartBroadcast        = 0xFE,
    GameOverBroadcast         = 0xFF
};

inline std::optional<PacketType> packetTypeFromId(uint8_t id)
{
    switch (id) {
        case 0x00: return PacketType::Hello;
        case 0x01: return PacketType::HelloAck;
        case 0x02: return PacketType::Connect;
        case 0x03: return PacketType::ConnectAck;
        case 0x04: return PacketType::Disconnect;
        case 0x05: return PacketType::DisconnectAck;
        case 0x06: return PacketType::ReadyState;
        case 0x08: return PacketType::LocationState;
        case 0x0A: return PacketType::ElementState;
        case 0x0E: return PacketType::PowerUp;
        case 0x10: return PacketType::ActionState;
        case 0x13: return PacketType::InitialGameStateAck;
        case 0xF0: return PacketType::ConnectBroadcast;
        case 0xF1: return PacketType::DisconnectBroadcast;
        case 0xF2: return PacketType::ReadyStateBroadcast;
        case 0xF3: return PacketType::LocationStateBroadcast;
        case 0xF4: return PacketType::ElementStateBroadcast;
        case 0xF5: return PacketType::HealthStateBroadcast;
        case 0xF6: return PacketType::PowerUpPickUpBroadcast;
        case 0xF7: return PacketType::PowerUpBroadcast;
        case 0xF8: return PacketType::InitialGameStateBroadcast;
        case 0xF9: return PacketType::ActionStateBroadcast;
        case 0xFA: return PacketType::RespawnBroadcast;
        case 0xFB: return PacketType::ScoreBroadcast;
        case 0xFE: return PacketType::GameStartBroadcast;
        case 0xFF: return PacketType::GameOverBroadcast;
        default:   return std::nullopt;
    }
}

} // namespace neon::net
