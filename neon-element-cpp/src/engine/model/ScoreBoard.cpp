#include "ScoreBoard.hpp"
#include "engine/entities/Player.hpp"

namespace neon {

void ScoreBoard::initialise(const std::vector<Player*>& playerList)
{
    board_.clear();
    leaderBoard_.clear();
    for (auto* player : playerList) {
        board_[player->getId()] = Score{};
        leaderBoard_.push_back(player->getId());
    }
}

void ScoreBoard::addKill(int killerId, int victimId)
{
    board_[killerId].kills++;
    addDeath(victimId);
}

void ScoreBoard::addDeath(int victimId)
{
    board_[victimId].deaths++;
    totalScore_++;
    updateLeaderBoard();
}

void ScoreBoard::addScore(int scorerId, int score)
{
    board_[scorerId].score += score;
    totalScore_++;
    updateLeaderBoard();
}

int ScoreBoard::getPlayerKills(int playerId) const
{
    auto it = board_.find(playerId);
    return (it != board_.end()) ? it->second.kills : 0;
}

int ScoreBoard::getPlayerScore(int playerId) const
{
    auto it = board_.find(playerId);
    return (it != board_.end()) ? it->second.score : 0;
}

int ScoreBoard::getPlayerDeaths(int playerId) const
{
    auto it = board_.find(playerId);
    return (it != board_.end()) ? it->second.deaths : 0;
}

void ScoreBoard::updateLeaderBoard()
{
    std::sort(leaderBoard_.begin(), leaderBoard_.end(),
        [this](int a, int b) {
            return board_[a].score > board_[b].score;
        });
}

} // namespace neon
