#pragma once

#include "engine/model/enums/Elements.hpp"
#include "engine/model/enums/PowerUpType.hpp"
#include <SFML/Graphics/Color.hpp>

namespace neon {

inline sf::Color getElementColor(Element element)
{
    switch (element) {
        case Element::Fire:  return sf::Color(254, 68, 108);   // #FE446C
        case Element::Air:   return sf::Color(255, 171, 69);   // #FFAB45
        case Element::Earth: return sf::Color(133, 253, 68);   // #85FD44
        case Element::Water: return sf::Color(77, 184, 248);   // #4DB8F8
    }
    return sf::Color::Black;
}

inline sf::Color getPowerUpColor(PowerUpType type)
{
    switch (type) {
        case PowerUpType::Speed:  return sf::Color(255, 165, 0);     // Orange
        case PowerUpType::Heal:   return sf::Color(128, 0, 128);     // Purple
        case PowerUpType::Damage: return sf::Color(255, 0, 255);     // Fuchsia
    }
    return sf::Color::Black;
}

} // namespace neon
