#pragma once

#include "enums/Action.hpp"
#include <cstdint>

namespace neon {

inline constexpr long long attackDurationMs(Action action)
{
    switch (action) {
        case Action::Heavy:  return 200;
        case Action::Light:  return 100;
        case Action::Charge: return 2500;
        default:             return 5;
    }
}

} // namespace neon
