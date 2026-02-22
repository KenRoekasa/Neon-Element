#pragma once

#include <string>

namespace neon {

enum class Sprite {
    Blade,
    Map,
    Pointer,
    Crown,
    Background
};

inline const char* spritePath(Sprite sprite)
{
    switch (sprite) {
        case Sprite::Blade:      return "assets/textures/orangeblade.png";
        case Sprite::Map:        return "assets/textures/map.png";
        case Sprite::Pointer:    return "assets/textures/bluepointer.png";
        case Sprite::Crown:      return "assets/icons/star-icon.png";
        case Sprite::Background: return "assets/textures/background.png";
    }
    return "";
}

} // namespace neon
