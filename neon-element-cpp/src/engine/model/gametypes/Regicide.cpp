#include "Regicide.hpp"
#include "engine/entities/Player.hpp"

namespace neon {

Regicide::Regicide(Player* king, int scoreNeeded)
    : GameType(Type::Regicide), king_(king), scoreNeeded_(scoreNeeded)
{
    if (king_)
        kingId_ = king_->getId();
}

Regicide::Regicide(int kingId, int scoreNeeded)
    : GameType(Type::Regicide), kingId_(kingId), scoreNeeded_(scoreNeeded)
{
}

void Regicide::setKing(Player* king)
{
    king_ = king;
    if (king_)
        kingId_ = king_->getId();
}

} // namespace neon
