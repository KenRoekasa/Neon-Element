#pragma once

#include <cstdint>

namespace neon::net {

inline constexpr uint16_t SERVER_LISTENING_PORT = 53582;
inline constexpr uint16_t BROADCASTING_PORT     = 8889;
inline constexpr int      NUM_PLAYERS           = 2;
inline constexpr size_t   PACKET_SIZE           = 128;

} // namespace neon::net
