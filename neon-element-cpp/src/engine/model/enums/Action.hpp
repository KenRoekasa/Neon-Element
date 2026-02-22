#pragma once

#include <cstdint>
#include <optional>

namespace neon {

enum class Action : uint8_t {
    Idle   = 0,
    Light  = 1,
    Heavy  = 2,
    Block  = 3,
    Charge = 4
};

inline std::optional<Action> actionFromId(uint8_t id)
{
    switch (id) {
        case 0: return Action::Idle;
        case 1: return Action::Light;
        case 2: return Action::Heavy;
        case 3: return Action::Block;
        case 4: return Action::Charge;
        default: return std::nullopt;
    }
}

} // namespace neon
