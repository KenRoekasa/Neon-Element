#include "AiActions.hpp"
#include "engine/ai/controller/AiController.hpp"
#include <cmath>

namespace neon {

AiActions::AiActions(AiController& aiCon, AiCalculations& calc, const Rect& map, const GameClock& clock)
    : aiCon_(aiCon), aiPlayer_(aiCon.getAiPlayer()), map_(map), clock_(clock),
      calc_(calc), timeCalc_(calc.getTimeCalc()), moveCalc_(calc.getMoveCalc()),
      playerCalc_(calc.getPlayerCalc())
{
    assignRandomElement();
}

void AiActions::assignRandomElement()
{
    std::uniform_int_distribution<int> dist(0, 3);
    switch (dist(rng_)) {
        case 0: aiPlayer_->setCurrentElement(Element::Earth); break;
        case 1: aiPlayer_->setCurrentElement(Element::Fire); break;
        case 2: aiPlayer_->setCurrentElement(Element::Water); break;
        case 3: aiPlayer_->setCurrentElement(Element::Air); break;
    }
}

void AiActions::assignDifferentRandomElement()
{
    Element current = aiPlayer_->getCurrentElement();
    Element elements[3];
    int idx = 0;
    for (int i = 0; i < 4; i++) {
        auto e = static_cast<Element>(i);
        if (e != current) elements[idx++] = e;
    }
    std::uniform_int_distribution<int> dist(0, 2);
    aiPlayer_->setCurrentElement(elements[dist(rng_)]);
}

void AiActions::moveAwayFromPlayer(Player& player)
{
    attackIfInDistanceWithShield(player);
    if (moveCalc_.reachedAnEdge())
        moveAwayFromEdge();
    else
        simpleMovement(player.getLocation(), aiPlayer_->getLocation());
}

void AiActions::moveAwayFromEdge()
{
    int i = moveCalc_.closestEdgeLocation();
    switch (i) {
        case 0: down(); break;
        case 1: rightcart(); break;
        case 2: downcart(); break;
        case 3: right(); break;
        case 4: upcart(); break;
        case 5: left(); break;
        case 6: leftcart(); break;
        case 7: up(); break;
        default: break;
    }
}

void AiActions::moveTo(int powerupIndex, Vec2 loc)
{
    Player* player = playerCalc_.getNearestPlayer();
    if (player) attackIfInDistanceWithShield(*player);

    double dist = moveCalc_.calcDistance(aiPlayer_->getLocation(), loc);
    if (!(static_cast<int>(dist) > 2 || powerupIndex == -1))
        return;
    simpleMovement(aiPlayer_->getLocation(), loc);
}

void AiActions::moveTo(Player& player)
{
    Vec2 playerLoc = player.getLocation();
    double dist = moveCalc_.calcDistance(aiPlayer_->getLocation(), playerLoc);

    if (player.getTag() == ObjectType::Player && dist <= 2.0 * aiPlayer_->getWidth())
        return;
    if (player.getTag() == ObjectType::Enemy && dist <= 2.0)
        return;

    float angle = static_cast<float>(moveCalc_.calcAngle(aiPlayer_->getLocation(), player.getLocation()));
    aiPlayer_->setAngle(angle);

    simpleMovement(aiPlayer_->getLocation(), playerLoc);
}

void AiActions::moveToAndKeepDistance(Player& player)
{
    if (playerCalc_.isCharging(*aiPlayer_)) {
        if (moveCalc_.calcDistance(aiPlayer_->getLocation(), player.getLocation()) > static_cast<double>(map_.width) * 0.075) {
            moveTo(player);
        } else {
            moveAwayFromPlayer(player);
        }
    } else {
        moveTo(player);
    }
}

void AiActions::simpleMovement(Vec2 myLoc, Vec2 theirLoc)
{
    float myX = myLoc.x, myY = myLoc.y;
    float theirX = theirLoc.x, theirY = theirLoc.y;

    if (myX > theirX && myY > theirY) up();
    else if (myX > theirX && static_cast<int>(myY) == static_cast<int>(theirY)) leftcart();
    else if (myX > theirX && myY < theirY) left();
    else if (static_cast<int>(myX) == static_cast<int>(theirX) && myY > theirY) upcart();
    else if (static_cast<int>(myX) == static_cast<int>(theirX) && myY < theirY) downcart();
    else if (myX < theirX && static_cast<int>(myY) == static_cast<int>(theirY)) rightcart();
    else if (myX < theirX && myY > theirY) right();
    else if (myX < theirX && myY < theirY) down();
}

void AiActions::changeToRandomElementAfter(int seconds)
{
    if (timeCalc_.secondsElapsed() >= static_cast<float>(seconds)) {
        timeCalc_.setStartTime(static_cast<float>(
            std::chrono::duration_cast<std::chrono::seconds>(
                std::chrono::steady_clock::now().time_since_epoch()).count()));
        assignDifferentRandomElement();
    }
}

void AiActions::startWandering()
{
    Player* player = playerCalc_.getNearestPlayer();
    if (player) attackIfInDistance(*player);

    if (moveCalc_.reachedAnEdge()) {
        switch (wanderingDirection_) {
            case 0: wanderingDirection_ = 1; break;
            case 1: wanderingDirection_ = 0; break;
            case 2: wanderingDirection_ = 3; break;
            case 3: wanderingDirection_ = 2; break;
            case 4: wanderingDirection_ = 5; break;
            case 5: wanderingDirection_ = 4; break;
            case 6: wanderingDirection_ = 7; break;
            case 7: wanderingDirection_ = 0; break;
        }
    }

    switch (wanderingDirection_) {
        case 0: down(); break;
        case 1: up(); break;
        case 2: left(); break;
        case 3: right(); break;
        case 4: downcart(); break;
        case 5: upcart(); break;
        case 6: leftcart(); break;
        case 7: rightcart(); break;
    }
}

void AiActions::changeToBefittingElement()
{
    Player* player = playerCalc_.getNearestPlayer();
    if (!player) return;

    AiState state = aiCon_.getActiveState();
    bool attacking = (state == AiState::Attack || state == AiState::AggressiveAttack || state == AiState::AttackWinner);

    if (attacking) {
        switch (player->getCurrentElement()) {
            case Element::Water:
            case Element::Air:
                aiPlayer_->setCurrentElement(Element::Fire);
                break;
            case Element::Earth:
            case Element::Fire:
                aiPlayer_->setCurrentElement(Element::Water);
                break;
        }
    } else {
        switch (player->getCurrentElement()) {
            case Element::Earth:
            case Element::Fire:
                aiPlayer_->setCurrentElement(Element::Water);
                break;
            case Element::Air:
                aiPlayer_->setCurrentElement(Element::Earth);
                break;
            case Element::Water:
                aiPlayer_->setCurrentElement(Element::Air);
                break;
        }
    }
}

void AiActions::attackIfInDistance(Player& player)
{
    if (playerCalc_.inAttackDistance(player) && player.getHealth() > 0.f && !playerCalc_.isCharging(player)) {
        std::uniform_int_distribution<int> dist(0, 9);
        if (dist(rng_) > 7)
            aiPlayer_->chargeHeavyAttack(clock_);
        else
            aiPlayer_->lightAttack(clock_);
    }
}

void AiActions::attackIfInDistanceWithShield(Player& player)
{
    if (playerCalc_.inAttackDistance(player) && player.getHealth() > 0.f && !playerCalc_.isCharging(player)) {
        std::uniform_int_distribution<int> dist(0, 9);
        aiPlayer_->unShield();
        if (dist(rng_) > 7)
            aiPlayer_->chargeHeavyAttack(clock_);
        else
            aiPlayer_->lightAttack(clock_);
        if (timeCalc_.gameTicked(kNumberOfTicks))
            aiPlayer_->shield();
    }
}

void AiActions::shieldWhenAlone()
{
    if (playerCalc_.playerIsTooClose()) {
        if (timeCalc_.gameTicked(kNumberOfTicks))
            aiPlayer_->shield();
    } else {
        aiPlayer_->unShield();
    }
}

// Movement helpers
void AiActions::up()    { aiPlayer_->moveUp(); }
void AiActions::down()  { aiPlayer_->moveDownCartesian(map_.width); }
void AiActions::left()  { aiPlayer_->moveLeft(map_.width); }
void AiActions::right() { aiPlayer_->moveRight(map_.width, map_.height); }
void AiActions::upcart()    { aiPlayer_->moveUpCartesian(); }
void AiActions::downcart()  { aiPlayer_->moveDownCartesian(map_.width); }
void AiActions::leftcart()  { aiPlayer_->moveLeftCartesian(); }
void AiActions::rightcart() { aiPlayer_->moveRightCartesian(map_.width); }

} // namespace neon
