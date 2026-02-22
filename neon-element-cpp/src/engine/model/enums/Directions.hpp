#pragma once

#include <cstdint>

namespace neon {

enum class Direction : uint8_t {
    Down,
    DownCart,
    Left,
    LeftCart,
    Right,
    RightCart,
    Up,
    UpCart
};

} // namespace neon
