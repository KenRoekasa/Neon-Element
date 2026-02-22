#include "PhysicsController.hpp"
#include "CollisionDetector.hpp"
#include "engine/calculations/DamageCalculation.hpp"
#include "engine/model/GameState.hpp"
#include "engine/model/GameType.hpp"
#include "engine/controller/GameTypeHandler.hpp"
#include "engine/entities/Player.hpp"
#include "engine/entities/PowerUp.hpp"
#include <algorithm>

namespace neon {

PhysicsController::PhysicsController(GameState& gameState)
    : gameState_(gameState)
{
}

void PhysicsController::clientLoop(const GameClock& clock)
{
    doCollisionDetection(clock);
    doHitDetection();
    doUpdates(clock);
    deathHandler(clock);

    auto& gameType = gameState_.getGameType();
    if (gameType.getType() == GameType::Type::Hill)
        kingOfHillHandler(clock);
    if (gameType.getType() == GameType::Type::Regicide)
        regicideHandler(clock);

    if (!GameTypeHandler::checkRunning(gameState_, clock))
        gameState_.stop();
}

void PhysicsController::serverLoop(const GameClock& clock)
{
    doCollisionDetection(clock);
    doHitDetection();
    doUpdates(clock);
    deathHandler(clock);

    auto& gameType = gameState_.getGameType();
    if (gameType.getType() == GameType::Type::Hill)
        kingOfHillHandler(clock);
    if (gameType.getType() == GameType::Type::Regicide)
        regicideHandler(clock);

    if (!GameTypeHandler::checkRunning(gameState_, clock))
        gameState_.stop();
}

void PhysicsController::dumbClientLoop(const GameClock& clock)
{
    doCollisionDetection(clock);
    doUpdates(clock);
}

void PhysicsController::doUpdates(const GameClock& clock)
{
    auto& objects = gameState_.getObjects();
    for (auto& obj : objects) {
        obj->update(clock);
    }
}

void PhysicsController::doCollisionDetection(const GameClock& clock)
{
    auto& objects = gameState_.getObjects();
    auto& allPlayers = gameState_.getAllPlayers();

    for (auto* player : allPlayers) {
        // Create projected position for wall collision
        float dt = clock.deltaTimeScaled();
        Vec2 checkNext = player->getLocation();
        checkNext.x += player->getHorizontalMove() * dt;
        checkNext.y += player->getVerticalMove() * dt;

        Player projected(ObjectType::Player, -1);
        projected.setLocation(checkNext.x, checkNext.y);

        for (auto it = objects.begin(); it != objects.end();) {
            auto& obj = *it;
            if (obj.get() == static_cast<PhysicsObject*>(player)) {
                ++it;
                continue;
            }

            if (obj->getTag() == ObjectType::PowerUp) {
                if (CollisionDetector::checkCollision(*player, *obj)) {
                    auto* powerUp = static_cast<PowerUp*>(obj.get());
                    powerUp->activatePowerUp(*player, clock);
                    it = objects.erase(it);
                    continue;
                }
            } else {
                if (CollisionDetector::checkCollision(projected, *obj)) {
                    player->setVerticalMove(0.f);
                    player->setHorizontalMove(0.f);
                }
            }
            ++it;
        }
    }
}

void PhysicsController::doHitDetection()
{
    auto& allPlayers = gameState_.getAllPlayers();

    for (auto* player : allPlayers) {
        for (auto* other : allPlayers) {
            if (other == player)
                continue;

            auto applyDamage = [&](bool inRange) {
                if (!inRange)
                    return;
                if (other->getLastAttacker() != nullptr) {
                    if (other->getIframes() <= 0 || other->getLastAttacker()->getId() != player->getId()) {
                        float damage = DamageCalculation::calculateDealtDamage(*player, *other);
                        other->takeDamage(damage, player);
                    }
                } else {
                    float damage = DamageCalculation::calculateDealtDamage(*player, *other);
                    other->takeDamage(damage, player);
                }
            };

            if (player->getCurrentAction() == Action::Light) {
                bool hit = CollisionDetector::checkCollision(
                    player->getAttackHitbox(), other->getBounds());
                applyDamage(hit);
            }

            if (player->getCurrentAction() == Action::Heavy) {
                bool hit = CollisionDetector::checkCollision(
                    player->getHeavyAttackHitbox(), other->getBounds());
                applyDamage(hit);
            }
        }
    }
}

void PhysicsController::deathHandler(const GameClock& clock)
{
    auto& allPlayers = gameState_.getAllPlayers();
    auto& deadPlayers = gameState_.getDeadPlayers();
    auto& scoreBoard = gameState_.getScoreBoard();

    for (auto* player : allPlayers) {
        bool alreadyDead = std::find(deadPlayers.begin(), deadPlayers.end(), player) != deadPlayers.end();
        if (!alreadyDead && !player->isAlive() && player->getLastAttacker() != nullptr) {
            deadPlayers.push_back(player);
            scoreBoard.addKill(player->getLastAttacker()->getId(), player->getId());

            auto& gameType = gameState_.getGameType();
            if (gameType.getType() == GameType::Type::FirstToXKills) {
                scoreBoard.addScore(player->getLastAttacker()->getId(), 1);
            } else if (gameType.getType() == GameType::Type::Regicide) {
                auto& regicide = static_cast<Regicide&>(gameType);
                int baseScore = 5;
                if (regicide.getKingId() == player->getId()) {
                    scoreBoard.addScore(player->getLastAttacker()->getId(), baseScore * 2);
                    regicide.setKing(player->getLastAttacker());
                } else {
                    scoreBoard.addScore(player->getLastAttacker()->getId(), baseScore);
                }
            }

            // Teleport off screen
            player->setLocation(5000.f, 5000.f);
        }
    }
}

void PhysicsController::kingOfHillHandler(const GameClock& clock)
{
    auto& gameType = gameState_.getGameType();
    auto& hillGame = static_cast<HillGame&>(gameType);
    Circle hill = hillGame.getHill();
    auto& allPlayers = gameState_.getAllPlayers();
    auto& scoreBoard = gameState_.getScoreBoard();

    std::vector<Player*> playersInside;
    for (auto* player : allPlayers) {
        if (CollisionDetector::checkCollision(hill, player->getBounds()))
            playersInside.push_back(player);
    }

    if (playersInside.size() == 1) {
        int playerId = playersInside[0]->getId();
        scoreBoard.addScore(playerId, static_cast<int>(clock.deltaTimeScaled()));
    }
}

void PhysicsController::regicideHandler(const GameClock& clock)
{
    auto& gameType = gameState_.getGameType();
    auto& regicide = static_cast<Regicide&>(gameType);
    Player* king = regicide.getKing();

    if (king && clock.elapsedMs() - lastTimeRegi_ >= 10000) {
        gameState_.getScoreBoard().addScore(king->getId(), 20);
        lastTimeRegi_ = clock.elapsedMs();
    }
}

} // namespace neon
