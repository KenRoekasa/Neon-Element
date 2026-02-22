#include "PlayersCalculations.hpp"
#include "engine/ai/controller/AiController.hpp"
#include "engine/model/enums/Action.hpp"
#include <limits>
#include <cmath>

namespace neon {

PlayersCalculations::PlayersCalculations(AiController& aiCon, const Rect& map,
                                         ScoreBoard* scoreboard, MovementCalculations& moveCalc)
    : aiCon_(aiCon), aiPlayer_(aiCon.getAiPlayer()), map_(map),
      scoreboard_(scoreboard), moveCalc_(moveCalc)
{
}

bool PlayersCalculations::playerIsTooClose()
{
    Player* nearest = getNearestPlayer();
    if (!nearest) return false;
    return isTooClose(nearest->getLocation());
}

bool PlayersCalculations::scoreDifferenceIsMoreThan(int score)
{
    Player* winner = getWinningPlayer();
    if (!winner || !scoreboard_) return false;
    int diff = scoreboard_->getPlayerKills(winner->getId()) - scoreboard_->getPlayerKills(aiPlayer_->getId());
    return diff >= score;
}

double PlayersCalculations::getScore(Player& player)
{
    if (!scoreboard_) return 0;
    return static_cast<double>(scoreboard_->getPlayerKills(player.getId()));
}

bool PlayersCalculations::someoneCloseIsCharging()
{
    auto players = getOtherPlayers();
    for (auto* player : players) {
        if (isCharging(*player) &&
            moveCalc_.calcDistance(aiPlayer_->getLocation(), player->getLocation()) < static_cast<double>(map_.width) * 0.1)
            return true;
    }
    return false;
}

bool PlayersCalculations::inAttackDistance(Player& player)
{
    return moveCalc_.calcDistance(aiPlayer_->getLocation(), player.getLocation()) < aiPlayer_->getLightAttackRange();
}

bool PlayersCalculations::isCharging(Player& player) const
{
    return player.getCurrentAction() == Action::Charge;
}

bool PlayersCalculations::isTooClose(Vec2 loc) const
{
    Vec2 aiLoc = aiPlayer_->getLocation();
    return distance(aiLoc, loc) < map_.width * 0.2f;
}

bool PlayersCalculations::isNearestPlayer(Player& player)
{
    return &player == getNearestPlayer();
}

Player* PlayersCalculations::getNearestPlayer()
{
    auto players = getOtherPlayers();
    if (players.empty()) return nullptr;

    double minDist = std::numeric_limits<double>::max();
    size_t index = 0;
    for (size_t i = 0; i < players.size(); i++) {
        double d = moveCalc_.calcDistance(players[i]->getLocation(), aiPlayer_->getLocation());
        if (d < minDist) {
            minDist = d;
            index = i;
        }
    }

    float angle = static_cast<float>(moveCalc_.calcAngle(aiPlayer_->getLocation(), players[index]->getLocation()));
    aiPlayer_->setAngle(angle);

    return players[index];
}

std::vector<Player*> PlayersCalculations::getOtherPlayers()
{
    std::vector<Player*> result;
    auto& objects = aiCon_.getObjects();
    for (auto& obj : objects) {
        if ((obj->getTag() == ObjectType::Player ||
             (obj->getTag() == ObjectType::Enemy && obj.get() != static_cast<PhysicsObject*>(aiPlayer_)))) {
            result.push_back(static_cast<Player*>(obj.get()));
        }
    }
    return result;
}

std::vector<Player*> PlayersCalculations::getAllPlayers()
{
    std::vector<Player*> result;
    auto& objects = aiCon_.getObjects();
    for (auto& obj : objects) {
        if (obj->getTag() == ObjectType::Player || obj->getTag() == ObjectType::Enemy)
            result.push_back(static_cast<Player*>(obj.get()));
    }
    return result;
}

std::vector<Player*> PlayersCalculations::getRealPlayers()
{
    std::vector<Player*> result;
    auto& objects = aiCon_.getObjects();
    for (auto& obj : objects) {
        if (obj->getTag() == ObjectType::Player)
            result.push_back(static_cast<Player*>(obj.get()));
    }
    return result;
}

Player* PlayersCalculations::getPlayerWithLowestHealth()
{
    auto players = getOtherPlayers();
    if (players.empty()) return nullptr;
    Player* lowest = players[0];
    float minH = Character::kMaxHealth;
    for (auto* p : players) {
        if (p->getHealth() < minH) {
            minH = p->getHealth();
            lowest = p;
        }
    }
    return lowest;
}

Player* PlayersCalculations::getWinningPlayer()
{
    if (!scoreboard_) return nullptr;
    auto& lb = scoreboard_->getLeaderBoard();
    if (lb.empty()) return nullptr;
    int winId = lb[0];
    auto players = getAllPlayers();
    for (auto* p : players) {
        if (p->getId() == winId)
            return p;
    }
    return nullptr;
}

} // namespace neon
