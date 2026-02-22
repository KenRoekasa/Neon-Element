#pragma once

#include "Sprites.hpp"
#include <SFML/Graphics/Texture.hpp>
#include <unordered_map>
#include <iostream>

namespace neon {

class TextureLoader {
public:
    void loadAll()
    {
        load(Sprite::Blade);
        load(Sprite::Map);
        load(Sprite::Pointer);
        load(Sprite::Crown);
        load(Sprite::Background);
    }

    const sf::Texture* get(Sprite sprite) const
    {
        auto it = textures_.find(sprite);
        if (it != textures_.end())
            return &it->second;
        return nullptr;
    }

    bool has(Sprite sprite) const
    {
        return textures_.find(sprite) != textures_.end();
    }

private:
    void load(Sprite sprite)
    {
        sf::Texture tex;
        if (tex.loadFromFile(spritePath(sprite))) {
            tex.setSmooth(false);
            textures_[sprite] = std::move(tex);
        } else {
            std::cerr << "Warning: could not load texture: " << spritePath(sprite) << "\n";
        }
    }

    std::unordered_map<Sprite, sf::Texture> textures_;
};

} // namespace neon
