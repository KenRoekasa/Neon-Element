#pragma once

#include "PacketType.hpp"
#include "Constants.hpp"
#include <SFML/Network.hpp>
#include <array>
#include <cstring>
#include <cstdint>

namespace neon::net {

// Sender info attached to incoming packets
struct Sender {
    sf::IpAddress address;
    unsigned short port = 0;
};

// Buffer helper: big-endian read/write over a fixed-size byte array
class PacketBuffer {
public:
    PacketBuffer() { data_.fill(std::byte{0}); }

    explicit PacketBuffer(const void* raw, size_t len)
    {
        data_.fill(std::byte{0});
        std::memcpy(data_.data(), raw, std::min(len, PACKET_SIZE));
    }

    // --- write helpers (big-endian) ---
    void putUint8(uint8_t v)
    {
        data_[pos_++] = static_cast<std::byte>(v);
    }

    void putInt32(int32_t v)
    {
        auto u = static_cast<uint32_t>(v);
        data_[pos_++] = static_cast<std::byte>((u >> 24) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((u >> 16) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((u >> 8)  & 0xFF);
        data_[pos_++] = static_cast<std::byte>( u        & 0xFF);
    }

    void putFloat(float v)
    {
        uint32_t bits;
        std::memcpy(&bits, &v, 4);
        putInt32(static_cast<int32_t>(bits));
    }

    void putDouble(double v)
    {
        uint64_t bits;
        std::memcpy(&bits, &v, 8);
        data_[pos_++] = static_cast<std::byte>((bits >> 56) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((bits >> 48) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((bits >> 40) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((bits >> 32) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((bits >> 24) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((bits >> 16) & 0xFF);
        data_[pos_++] = static_cast<std::byte>((bits >> 8)  & 0xFF);
        data_[pos_++] = static_cast<std::byte>( bits        & 0xFF);
    }

    void putBool(bool v) { putUint8(v ? 0x01 : 0x00); }

    // --- read helpers (big-endian) ---
    uint8_t getUint8()
    {
        return static_cast<uint8_t>(data_[pos_++]);
    }

    int32_t getInt32()
    {
        uint32_t u = 0;
        u |= static_cast<uint32_t>(data_[pos_++]) << 24;
        u |= static_cast<uint32_t>(data_[pos_++]) << 16;
        u |= static_cast<uint32_t>(data_[pos_++]) << 8;
        u |= static_cast<uint32_t>(data_[pos_++]);
        return static_cast<int32_t>(u);
    }

    float getFloat()
    {
        auto bits = static_cast<uint32_t>(getInt32());
        float v;
        std::memcpy(&v, &bits, 4);
        return v;
    }

    double getDouble()
    {
        uint64_t bits = 0;
        bits |= static_cast<uint64_t>(data_[pos_++]) << 56;
        bits |= static_cast<uint64_t>(data_[pos_++]) << 48;
        bits |= static_cast<uint64_t>(data_[pos_++]) << 40;
        bits |= static_cast<uint64_t>(data_[pos_++]) << 32;
        bits |= static_cast<uint64_t>(data_[pos_++]) << 24;
        bits |= static_cast<uint64_t>(data_[pos_++]) << 16;
        bits |= static_cast<uint64_t>(data_[pos_++]) << 8;
        bits |= static_cast<uint64_t>(data_[pos_++]);
        double v;
        std::memcpy(&v, &bits, 8);
        return v;
    }

    bool getBool() { return getUint8() != 0; }

    void resetPos() { pos_ = 0; }
    void setPos(size_t p) { pos_ = p; }
    size_t pos() const { return pos_; }
    const std::byte* data() const { return data_.data(); }
    size_t size() const { return PACKET_SIZE; }

private:
    std::array<std::byte, PACKET_SIZE> data_{};
    size_t pos_ = 0;
};

// Forward declarations for handler interfaces
class ClientNetworkHandler;
class ServerNetworkHandler;

// Base packet class
class Packet {
public:
    virtual ~Packet() = default;

    PacketType getType() const { return type_; }
    const Sender& getSender() const { return sender_; }
    void setSender(const Sender& s) { sender_ = s; }

    // Serialize to 128-byte buffer; first byte = packet type ID
    virtual PacketBuffer serialize() const
    {
        PacketBuffer buf;
        buf.putUint8(static_cast<uint8_t>(type_));
        return buf;
    }

protected:
    explicit Packet(PacketType type) : type_(type) {}

    PacketType type_;
    Sender sender_;
};

// Packets sent client → server
class PacketToServer : public Packet {
public:
    virtual void handle(ServerNetworkHandler& handler) = 0;
protected:
    using Packet::Packet;
};

// Packets sent server → client
class PacketToClient : public Packet {
public:
    virtual void handle(ClientNetworkHandler& handler) = 0;
protected:
    using Packet::Packet;
};

} // namespace neon::net
