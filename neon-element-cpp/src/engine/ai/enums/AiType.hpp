#pragma once

#include <cstdint>
#include <string>
#include <optional>

namespace neon {

enum class AiType : uint8_t {
    Easy,
    Normal,
    Hard
};

inline std::optional<AiType> aiTypeFromString(const std::string& str)
{
    if (str == "easy" || str == "EASY") return AiType::Easy;
    if (str == "normal" || str == "NORMAL") return AiType::Normal;
    if (str == "hard" || str == "HARD") return AiType::Hard;
    return std::nullopt;
}

} // namespace neon
