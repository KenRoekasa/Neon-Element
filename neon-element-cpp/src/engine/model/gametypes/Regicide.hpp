#pragma once

#include "engine/model/GameType.hpp"

namespace neon {

class Player;

class Regicide : public GameType {
public:
    Regicide(Player* king, int scoreNeeded);
    Regicide(int kingId, int scoreNeeded);

    int getScoreNeeded() const { return scoreNeeded_; }
    Player* getKing() const { return king_; }
    int getKingId() const { return kingId_; }

    void setKing(Player* king);
    void setKingId(int id) { kingId_ = id; }

private:
    Player* king_ = nullptr;
    int kingId_ = -1;
    int scoreNeeded_;
};

} // namespace neon
