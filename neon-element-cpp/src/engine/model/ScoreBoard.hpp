#pragma once

#include <unordered_map>
#include <vector>
#include <algorithm>

namespace neon {

class Player;

class ScoreBoard {
public:
    ScoreBoard() = default;

    void initialise(const std::vector<Player*>& playerList);

    void addKill(int killerId, int victimId);
    void addDeath(int victimId);
    void addScore(int scorerId, int score);

    int getPlayerKills(int playerId) const;
    int getPlayerScore(int playerId) const;
    int getPlayerDeaths(int playerId) const;
    int getTotalKills() const { return totalScore_; }

    const std::vector<int>& getLeaderBoard() const { return leaderBoard_; }

private:
    struct Score {
        int score = 0;
        int kills = 0;
        int deaths = 0;
    };

    void updateLeaderBoard();

    std::unordered_map<int, Score> board_;
    int totalScore_ = 0;
    std::vector<int> leaderBoard_;
};

} // namespace neon
