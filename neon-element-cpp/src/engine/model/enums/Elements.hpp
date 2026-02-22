#pragma once

#include <cstdint>
#include <optional>

namespace neon {

enum class Element : uint8_t {
    Fire  = 0,
    Water = 1,
    Earth = 2,
    Air   = 3
};

inline constexpr int kElementCount = 4;

inline std::optional<Element> elementFromId(uint8_t id)
{
    switch (id) {
        case 0: return Element::Fire;
        case 1: return Element::Water;
        case 2: return Element::Earth;
        case 3: return Element::Air;
        default: return std::nullopt;
    }
}

} // namespace neon
