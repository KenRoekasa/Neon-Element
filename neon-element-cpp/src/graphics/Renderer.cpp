#include "Renderer.hpp"
#include "engine/model/gametypes/HillGame.hpp"
#include "engine/model/gametypes/Regicide.hpp"
#include "engine/model/AttackTimes.hpp"
#include "engine/entities/PowerUp.hpp"
#include <algorithm>
#include <random>
#include <cmath>

namespace neon {

Renderer::Renderer(float width, float height)
    : width_(width), height_(height), stageCenter_{width / 2.f, height / 2.f}
{
    textures_.loadAll();
    fontLoaded_ = font_.loadFromFile("assets/fonts/Audiowide-Regular.ttf");

    // Generate random stars for background
    std::mt19937 rng{std::random_device{}()};
    std::uniform_real_distribution<float> xDist(0.f, width);
    std::uniform_real_distribution<float> yDist(0.f, height);
    for (int i = 0; i < 40; i++)
        stars_.push_back({xDist(rng), yDist(rng)});
}

void Renderer::render(sf::RenderWindow& window, ClientGameState& gameState, const GameClock& clock)
{
    drawBackground(window);

    auto* player = gameState.getPlayer();
    if (!player || !player->isAlive()) {
        drawDeathScreen(window);
        return;
    }

    calculateOffset(gameState);

    // Get isometric transform centered on screen
    sf::Transform isoTransform = ISOConverter::getIsoTransform(stageCenter_);

    // Draw map
    {
        Vec2 relPos = ISOConverter::playerScreenPos(stageCenter_, offset_);
        relPos.x -= player->getLocation().x;
        relPos.y -= player->getLocation().y;
        sf::RectangleShape mapRect({gameState.getMap().getWidth(), gameState.getMap().getHeight()});
        mapRect.setPosition(relPos);
        if (textures_.has(Sprite::Map)) {
            mapRect.setTexture(textures_.get(Sprite::Map));
        } else {
            mapRect.setFillColor(sf::Color(20, 15, 40));
        }
        window.draw(mapRect, isoTransform);
    }

    // Draw hill if Hill game mode
    if (gameState.getGameType().getType() == GameType::Type::Hill) {
        auto& hillGame = static_cast<HillGame&>(gameState.getGameType());
        Circle hill = hillGame.getHill();
        Vec2 hillPos = getScreenPos({hill.x, hill.y}, *player);
        sf::CircleShape hillShape(hill.radius);
        hillShape.setPosition(hillPos.x - hill.radius, hillPos.y - hill.radius);
        hillShape.setFillColor(sf::Color(127, 255, 212, 80)); // Aquamarine with alpha
        window.draw(hillShape, isoTransform);
    }

    // Sort objects by Y for depth ordering
    std::vector<PhysicsObject*> sorted;
    for (auto& obj : gameState.getObjects())
        sorted.push_back(obj.get());
    std::sort(sorted.begin(), sorted.end(),
        [](const PhysicsObject* a, const PhysicsObject* b) {
            return a->getLocation().y < b->getLocation().y;
        });

    // Draw all objects
    for (auto* obj : sorted) {
        if (obj->getTag() == ObjectType::Player) {
            Vec2 screenPos = getPlayerScreenPos();
            screenPos.x -= static_cast<float>(player->getWidth()) / 2.f;
            screenPos.y -= static_cast<float>(player->getWidth()) / 2.f;
            drawPlayerRect(window, screenPos, *player);
            // Wrap with iso transform handled via states
        } else if (obj->getTag() == ObjectType::Enemy) {
            auto* enemy = static_cast<Character*>(obj);
            if (enemy->isAlive()) {
                Vec2 screenPos = getScreenPos(enemy->getLocation(), *player);
                screenPos.x -= static_cast<float>(player->getWidth()) / 2.f;
                screenPos.y -= static_cast<float>(player->getWidth()) / 2.f;
                drawPlayerRect(window, screenPos, *enemy);
            }
        } else if (obj->getTag() == ObjectType::PowerUp) {
            auto* pu = static_cast<PowerUp*>(obj);
            Vec2 screenPos = getScreenPos(pu->getLocation(), *player);
            sf::CircleShape puShape(static_cast<float>(pu->getWidth()) / 2.f);
            puShape.setPosition(screenPos);
            puShape.setFillColor(getPowerUpColor(pu->getType()));
            window.draw(puShape, isoTransform);
        } else if (obj->getTag() == ObjectType::Obstacle) {
            Vec2 screenPos = getScreenPos(obj->getLocation(), *player);
            screenPos.x -= static_cast<float>(obj->getWidth()) / 2.f;
            screenPos.y -= static_cast<float>(obj->getHeight()) / 2.f;
            sf::RectangleShape wallRect({static_cast<float>(obj->getWidth()),
                                         static_cast<float>(obj->getHeight())});
            wallRect.setPosition(screenPos);
            wallRect.setFillColor(sf::Color(128, 0, 128)); // Purple
            window.draw(wallRect, isoTransform);
        }
    }

    // Draw action states for all players
    for (auto* p : gameState.getAllPlayers()) {
        if (!p->isAlive()) continue;
        bool isClient = (p == player);
        Vec2 screenPos = isClient ? getPlayerScreenPos() : getScreenPos(p->getLocation(), *player);
        if (p->getCurrentAction() != Action::Idle)
            drawActionState(window, *p, screenPos, clock);
    }
}

void Renderer::calculateOffset(ClientGameState& gameState)
{
    constexpr int kMaxAllowedDistance = 300;
    auto* player = gameState.getPlayer();
    float mapW = gameState.getMap().getWidth();
    float mapH = gameState.getMap().getHeight();
    Vec2 playerLoc = player->getLocation();

    offset_ = {0.f, 0.f};

    if (playerLoc.x <= kMaxAllowedDistance)
        offset_.x = -(static_cast<float>(kMaxAllowedDistance) - playerLoc.x);
    else if (mapW - static_cast<float>(kMaxAllowedDistance) <= playerLoc.x)
        offset_.x = -(mapW - playerLoc.x - static_cast<float>(kMaxAllowedDistance));

    if (playerLoc.y <= kMaxAllowedDistance)
        offset_.y = -(static_cast<float>(kMaxAllowedDistance) - playerLoc.y);
    else if (mapH - static_cast<float>(kMaxAllowedDistance) <= playerLoc.y)
        offset_.y = -(mapH - playerLoc.y - static_cast<float>(kMaxAllowedDistance));
}

void Renderer::drawBackground(sf::RenderWindow& window)
{
    // Dark gradient background
    sf::RectangleShape bg({width_, height_});
    bg.setFillColor(sf::Color(6, 4, 25));
    window.draw(bg);

    // Stars
    for (auto& star : stars_) {
        sf::CircleShape s(2.5f);
        s.setPosition(star);
        s.setFillColor(sf::Color::White);
        window.draw(s);
    }
}

void Renderer::drawPlayerRect(sf::RenderWindow& window, Vec2 screenPos, const Character& character)
{
    sf::Transform isoTransform = ISOConverter::getIsoTransform(stageCenter_);
    sf::Color color = getElementColor(character.getCurrentElement());

    sf::RectangleShape rect({static_cast<float>(character.getWidth()),
                              static_cast<float>(character.getWidth())});
    rect.setPosition(screenPos);
    rect.setFillColor(color);

    // Simulate glow via additive blending
    sf::RenderStates states;
    states.transform = isoTransform;
    states.blendMode = sf::BlendAdd;
    window.draw(rect, states);

    // Draw normal on top
    states.blendMode = sf::BlendAlpha;
    rect.setFillColor(sf::Color(color.r, color.g, color.b, 200));
    window.draw(rect, states);
}

void Renderer::drawActionState(sf::RenderWindow& window, const Character& character,
                                Vec2 screenPos, const GameClock& clock)
{
    sf::Transform isoTransform = ISOConverter::getIsoTransform(stageCenter_);
    long long animDuration = attackDurationMs(character.getCurrentAction());
    long long remainAnim = character.getCurrentActionStart() + animDuration - clock.elapsedMs();

    switch (character.getCurrentAction()) {
        case Action::Light:
            drawLightAttack(window, character, screenPos, remainAnim, animDuration);
            break;
        case Action::Charge:
            drawHeavyCharge(window, character, screenPos, remainAnim,
                           attackDurationMs(Action::Charge));
            break;
        case Action::Heavy:
            drawHeavyAttack(window, character, screenPos, remainAnim, animDuration);
            break;
        case Action::Block:
            drawShield(window, character, screenPos);
            break;
        default:
            break;
    }
}

void Renderer::drawLightAttack(sf::RenderWindow& window, const Character& character,
                                Vec2 screenPos, long long remainAnim, long long animDuration)
{
    sf::Transform isoTransform = ISOConverter::getIsoTransform(stageCenter_);
    long long angle = static_cast<long long>(character.getAngle()) +
                      mapInRange(remainAnim, 0, animDuration, 0, 90);

    sf::Color color = getElementColor(character.getCurrentElement());
    sf::RectangleShape blade({8.f, static_cast<float>(character.getLightAttackRange())});
    blade.setOrigin(4.f, static_cast<float>(character.getLightAttackRange()));
    blade.setPosition(screenPos);
    blade.setFillColor(color);
    blade.setRotation(static_cast<float>(angle - 45));

    window.draw(blade, isoTransform);
}

void Renderer::drawHeavyAttack(sf::RenderWindow& window, const Character& character,
                                Vec2 screenPos, long long remainAnim, long long animDuration)
{
    sf::Transform isoTransform = ISOConverter::getIsoTransform(stageCenter_);
    long long angle = mapInRange(remainAnim, 0, animDuration, 0, 180);
    sf::Color color = getElementColor(character.getCurrentElement());

    for (int i = 0; i < 4; i++) {
        sf::RectangleShape blade({8.f, 200.f});
        blade.setOrigin(4.f, 200.f);
        blade.setPosition(screenPos);
        blade.setFillColor(color);
        blade.setRotation(static_cast<float>(angle + i * 90));
        window.draw(blade, isoTransform);
    }
}

void Renderer::drawHeavyCharge(sf::RenderWindow& window, const Character& character,
                                Vec2 screenPos, long long remainAnim, long long animDuration)
{
    sf::Transform isoTransform = ISOConverter::getIsoTransform(stageCenter_);
    float percentCharged = static_cast<float>(mapInRange(remainAnim, 0, animDuration, 80, 0)) / 100.f;
    float radius = character.getHeavyAttackHitbox().radius;

    sf::Color color = getElementColor(character.getCurrentElement());
    color.a = static_cast<sf::Uint8>(percentCharged * 255.f);

    sf::CircleShape circle(radius);
    circle.setOrigin(radius, radius);
    circle.setPosition(screenPos);
    circle.setFillColor(color);
    window.draw(circle, isoTransform);
}

void Renderer::drawShield(sf::RenderWindow& window, const Character& character, Vec2 screenPos)
{
    sf::Transform isoTransform = ISOConverter::getIsoTransform(stageCenter_);
    float shieldRadius = (static_cast<float>(character.getWidth()) + 40.f) / 2.f;

    sf::CircleShape shield(shieldRadius);
    shield.setOrigin(shieldRadius, shieldRadius);
    shield.setPosition(screenPos);
    shield.setFillColor(sf::Color(176, 196, 222, 128)); // Light steel blue with alpha
    shield.setOutlineColor(sf::Color(0, 0, 255, 128));
    shield.setOutlineThickness(2.f);
    window.draw(shield, isoTransform);
}

void Renderer::drawDeathScreen(sf::RenderWindow& window)
{
    if (!fontLoaded_) return;
    sf::Text text;
    text.setFont(font_);
    text.setString("you are dead!");
    text.setCharacterSize(50);
    text.setFillColor(sf::Color::White);
    sf::FloatRect bounds = text.getLocalBounds();
    text.setOrigin(bounds.width / 2.f, bounds.height / 2.f);
    text.setPosition(width_ / 2.f, height_ / 2.f);
    window.draw(text);
}

Vec2 Renderer::getScreenPos(Vec2 worldPos, const Player& player) const
{
    return ISOConverter::toScreen(worldPos, player.getLocation(), stageCenter_, offset_);
}

Vec2 Renderer::getPlayerScreenPos() const
{
    return ISOConverter::playerScreenPos(stageCenter_, offset_);
}

long long Renderer::mapInRange(long long x, long long fromLow, long long fromHigh, long long toLow, long long toHigh)
{
    if (fromHigh == fromLow) return toLow;
    return (x - fromLow) * (toHigh - toLow) / (fromHigh - fromLow) + toLow;
}

} // namespace neon
