#include "AiCalculations.hpp"
#include "engine/ai/controller/AiController.hpp"
#include "engine/model/gametypes/HillGame.hpp"
#include "engine/model/gametypes/Regicide.hpp"
#include "engine/physics/CollisionDetector.hpp"

namespace neon {

AiCalculations::AiCalculations(AiController& aiCon, const Rect& map, ScoreBoard* scoreboard, GameType& gameType)
    : gameType_(gameType), scoreboard_(scoreboard), aiPlayer_(aiCon.getAiPlayer()),
      map_(map), moveCalc_(aiCon, map), playerCalc_(aiCon, map, scoreboard, moveCalc_),
      puCalc_(aiCon, map, playerCalc_)
{
}

std::unique_ptr<AiCalculations> AiCalculations::create(const Rect& map, ScoreBoard* scoreboard,
                                                        GameType& gameType, AiController& aiCon)
{
    switch (gameType.getType()) {
        case GameType::Type::FirstToXKills:
            return std::make_unique<KillsCalculations>(aiCon, map, scoreboard, gameType);
        case GameType::Type::Timed:
            return std::make_unique<TimedCalculations>(aiCon, map, scoreboard, gameType);
        case GameType::Type::Hill:
            return std::make_unique<HillCalculations>(aiCon, map, scoreboard, gameType);
        case GameType::Type::Regicide:
            return std::make_unique<RegicideCalculations>(aiCon, map, scoreboard, gameType);
    }
    return std::make_unique<KillsCalculations>(aiCon, map, scoreboard, gameType);
}

// --- HillCalculations ---

HillCalculations::HillCalculations(AiController& aiCon, const Rect& map, ScoreBoard* scoreboard, GameType& gameType)
    : AiCalculations(aiCon, map, scoreboard, gameType)
{
}

bool HillCalculations::onHill() const
{
    auto& hillGame = static_cast<HillGame&>(gameType_);
    Circle hill = hillGame.getHill();
    return CollisionDetector::checkCollision(hill, aiPlayer_->getBounds());
}

bool HillCalculations::closeToHill() const
{
    auto& hillGame = static_cast<HillGame&>(gameType_);
    Circle hill = hillGame.getHill();
    float dist = distance(aiPlayer_->getLocation(), Vec2{hill.x, hill.y});
    return dist < map_.width * 0.3f;
}

Player* HillCalculations::getOnHillPlayer()
{
    auto& hillGame = static_cast<HillGame&>(gameType_);
    Circle hill = hillGame.getHill();
    auto players = playerCalc_.getOtherPlayers();
    for (auto* p : players) {
        if (CollisionDetector::checkCollision(hill, p->getBounds()))
            return p;
    }
    return nullptr;
}

// --- RegicideCalculations ---

RegicideCalculations::RegicideCalculations(AiController& aiCon, const Rect& map, ScoreBoard* scoreboard, GameType& gameType)
    : AiCalculations(aiCon, map, scoreboard, gameType)
{
}

Player* RegicideCalculations::getKing()
{
    auto& regicide = static_cast<Regicide&>(gameType_);
    return regicide.getKing();
}

bool RegicideCalculations::kingIsClose()
{
    Player* king = getKing();
    if (!king || king == aiPlayer_) return false;
    return playerCalc_.isTooClose(king->getLocation());
}

} // namespace neon
