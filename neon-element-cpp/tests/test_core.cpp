#include <catch2/catch_test_macros.hpp>
#include <catch2/catch_approx.hpp>
#include "core/Vec2.hpp"
#include "core/Rect.hpp"
#include "core/Circle.hpp"
#include "core/Transform2D.hpp"
#include "core/Clock.hpp"
#include "engine/model/enums/Action.hpp"
#include "engine/model/enums/Elements.hpp"
#include "engine/model/enums/ObjectType.hpp"
#include "engine/model/enums/PowerUpType.hpp"
#include "engine/model/AttackTimes.hpp"
#include "engine/entities/CooldownSystem.hpp"

using namespace neon;
using Catch::Approx;

// --- Vec2 ---

TEST_CASE("Vec2 distance", "[core][vec2]")
{
    Vec2 a{0.f, 0.f};
    Vec2 b{3.f, 4.f};
    REQUIRE(distance(a, b) == Approx(5.f));
}

TEST_CASE("Vec2 length", "[core][vec2]")
{
    REQUIRE(length(Vec2{3.f, 4.f}) == Approx(5.f));
    REQUIRE(length(Vec2{0.f, 0.f}) == Approx(0.f));
}

TEST_CASE("Vec2 normalize", "[core][vec2]")
{
    Vec2 n = normalize(Vec2{3.f, 0.f});
    REQUIRE(n.x == Approx(1.f));
    REQUIRE(n.y == Approx(0.f));

    Vec2 zero = normalize(Vec2{0.f, 0.f});
    REQUIRE(zero.x == Approx(0.f));
    REQUIRE(zero.y == Approx(0.f));
}

TEST_CASE("Vec2 dot product", "[core][vec2]")
{
    REQUIRE(dot(Vec2{1.f, 0.f}, Vec2{0.f, 1.f}) == Approx(0.f));
    REQUIRE(dot(Vec2{2.f, 3.f}, Vec2{4.f, 5.f}) == Approx(23.f));
}

// --- Rect ---

TEST_CASE("Rect edges", "[core][rect]")
{
    Rect r{100.f, 100.f, 60.f, 60.f};
    REQUIRE(r.left() == Approx(70.f));
    REQUIRE(r.right() == Approx(130.f));
    REQUIRE(r.top() == Approx(70.f));
    REQUIRE(r.bottom() == Approx(130.f));
}

TEST_CASE("Rect AABB intersection", "[core][rect]")
{
    Rect a{100.f, 100.f, 60.f, 60.f};
    Rect b{120.f, 120.f, 60.f, 60.f};
    Rect c{300.f, 300.f, 60.f, 60.f};

    REQUIRE(intersects(a, b) == true);
    REQUIRE(intersects(a, c) == false);
}

TEST_CASE("Rect fromTopLeft", "[core][rect]")
{
    Rect r = Rect::fromTopLeft(0.f, 0.f, 100.f, 50.f);
    REQUIRE(r.x == Approx(50.f));
    REQUIRE(r.y == Approx(25.f));
    REQUIRE(r.width == Approx(100.f));
    REQUIRE(r.height == Approx(50.f));
}

// --- Circle ---

TEST_CASE("Circle-Rect intersection", "[core][circle]")
{
    Circle c{100.f, 100.f, 50.f};
    Rect inside{110.f, 110.f, 20.f, 20.f};
    Rect outside{300.f, 300.f, 20.f, 20.f};
    Rect edge{149.f, 100.f, 20.f, 20.f};

    REQUIRE(intersects(c, inside) == true);
    REQUIRE(intersects(c, outside) == false);
    REQUIRE(intersects(c, edge) == true);
}

TEST_CASE("Circle-Circle intersection", "[core][circle]")
{
    Circle a{0.f, 0.f, 10.f};
    Circle b{15.f, 0.f, 10.f};
    Circle c{100.f, 0.f, 5.f};

    REQUIRE(intersects(a, b) == true);
    REQUIRE(intersects(a, c) == false);
}

// --- Transform2D ---

TEST_CASE("RotatedRect vs Rect intersection", "[core][transform]")
{
    // Unrotated case should match AABB
    RotatedRect rr{Rect{100.f, 100.f, 60.f, 60.f}, 0.f, Vec2{100.f, 100.f}};
    Rect target{120.f, 120.f, 40.f, 40.f};
    REQUIRE(intersectsRotated(rr, target) == true);

    Rect far{500.f, 500.f, 10.f, 10.f};
    REQUIRE(intersectsRotated(rr, far) == false);
}

// --- Enums ---

TEST_CASE("Action from id", "[enums]")
{
    REQUIRE(actionFromId(0) == Action::Idle);
    REQUIRE(actionFromId(1) == Action::Light);
    REQUIRE(actionFromId(4) == Action::Charge);
    REQUIRE(actionFromId(99) == std::nullopt);
}

TEST_CASE("Element from id", "[enums]")
{
    REQUIRE(elementFromId(0) == Element::Fire);
    REQUIRE(elementFromId(3) == Element::Air);
    REQUIRE(elementFromId(10) == std::nullopt);
}

TEST_CASE("ObjectType sizes", "[enums]")
{
    REQUIRE(objectSize(ObjectType::Player) == 60);
    REQUIRE(objectSize(ObjectType::Enemy) == 60);
    REQUIRE(objectSize(ObjectType::Obstacle) == 100);
    REQUIRE(objectSize(ObjectType::PowerUp) == 20);
}

TEST_CASE("PowerUpType from id", "[enums]")
{
    REQUIRE(powerUpTypeFromId(1) == PowerUpType::Heal);
    REQUIRE(powerUpTypeFromId(2) == PowerUpType::Speed);
    REQUIRE(powerUpTypeFromId(3) == PowerUpType::Damage);
    REQUIRE(powerUpTypeFromId(0) == std::nullopt);
}

TEST_CASE("AttackTimes", "[model]")
{
    REQUIRE(attackDurationMs(Action::Light) == 100);
    REQUIRE(attackDurationMs(Action::Heavy) == 200);
    REQUIRE(attackDurationMs(Action::Charge) == 2500);
}

TEST_CASE("Cooldown constants", "[model]")
{
    REQUIRE(cooldown::kLightAttackCD == Approx(0.30f));
    REQUIRE(cooldown::kHeavyAttackCD == Approx(7.5f));
    REQUIRE(cooldown::kChangeStateCD == Approx(1.5f));
    REQUIRE(cooldown::kSpeedBoostDuration == Approx(4.0f));
    REQUIRE(cooldown::kDamageBoostDuration == Approx(5.0f));
}
