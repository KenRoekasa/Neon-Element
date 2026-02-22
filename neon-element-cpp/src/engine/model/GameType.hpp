#pragma once

#include <cstdint>
#include <optional>

namespace neon {

class GameType {
public:
    enum class Type : uint8_t {
        FirstToXKills = 1,
        Timed = 2,
        Hill = 3,
        Regicide = 4
    };

    explicit GameType(Type type) : type_(type) {}
    virtual ~GameType() = default;

    Type getType() const { return type_; }

    static std::optional<Type> typeFromId(uint8_t id)
    {
        switch (id) {
            case 1: return Type::FirstToXKills;
            case 2: return Type::Timed;
            case 3: return Type::Hill;
            case 4: return Type::Regicide;
            default: return std::nullopt;
        }
    }

private:
    Type type_;
};

} // namespace neon
