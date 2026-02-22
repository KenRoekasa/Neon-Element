#include "MovementCalculations.hpp"
#include "engine/ai/controller/AiController.hpp"

namespace neon {

MovementCalculations::MovementCalculations(AiController& aiCon, const Rect& map)
    : aiPlayer_(aiCon.getAiPlayer()), map_(map)
{
}

bool MovementCalculations::reachedAnEdge() const
{
    float x = aiPlayer_->getLocation().x;
    float y = aiPlayer_->getLocation().y;
    float w = map_.width;
    float h = map_.height;
    return (x < h * 0.02f || x > h * 0.98f || y < w * 0.02f || y > w * 0.98f);
}

int MovementCalculations::closestEdgeLocation() const
{
    Vec2 loc = aiPlayer_->getLocation();
    float x = loc.x;
    float y = loc.y;
    float w = map_.width;
    float h = map_.height;

    if (x < w / 2.f) {
        if (y < h / 2.f) {
            if (x < w * 0.05f) {
                if (y < h * 0.05f) return 0; // down
                else return 1; // right cart
            } else {
                if (y < h * 0.05f) return 2; // down cart
            }
        } else {
            if (x < w * 0.05f) {
                if (y > h * 0.95f) return 3; // right
                else return 1; // right cart
            } else {
                if (y > h * 0.95f) return 4; // up cart
            }
        }
    } else {
        if (y < h / 2.f) {
            if (y < h * 0.05f) {
                if (x > w * 0.95f) return 5; // left
                else return 2; // down cart
            } else {
                if (x > w * 0.95f) return 6; // left cart
            }
        } else {
            if (x > w * 0.95f) {
                if (y > h * 0.95f) return 7; // up
                else return 6; // left cart
            } else {
                if (y > h * 0.95f) return 4; // up cart
            }
        }
    }
    return -1;
}

double MovementCalculations::calcAngle(Vec2 loc1, Vec2 loc2) const
{
    double x = static_cast<double>(loc1.x - loc2.x);
    double y = static_cast<double>(loc1.y - loc2.y);
    return std::atan2(y, x) * 180.0 / 3.14159265358979323846 - 90.0;
}

double MovementCalculations::calcDistance(Vec2 a, Vec2 b) const
{
    return static_cast<double>(distance(a, b));
}

} // namespace neon
