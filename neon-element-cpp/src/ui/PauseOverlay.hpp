#pragma once

#include "Widget.hpp"
#include "engine/model/ClientGameState.hpp"
#include <SFML/Graphics.hpp>
#include <memory>
#include <functional>

namespace neon {

class PauseOverlay {
public:
    PauseOverlay();

    void handleEvent(const sf::Event& event, sf::RenderWindow& window);
    void draw(sf::RenderWindow& window);

    void setOnResume(std::function<void()> cb) { onResume_ = std::move(cb); }
    void setOnOptions(std::function<void()> cb) { onOptions_ = std::move(cb); }
    void setOnQuit(std::function<void()> cb) { onQuit_ = std::move(cb); }

private:
    sf::Font font_;
    bool fontLoaded_ = false;

    std::unique_ptr<Label> title_;
    std::unique_ptr<Button> resumeBtn_;
    std::unique_ptr<Button> optionsBtn_;
    std::unique_ptr<Button> quitBtn_;

    std::function<void()> onResume_;
    std::function<void()> onOptions_;
    std::function<void()> onQuit_;
};

} // namespace neon
