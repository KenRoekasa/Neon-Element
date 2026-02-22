#include "UIManager.hpp"

namespace neon {

UIManager::UIManager(sf::RenderWindow& window, AudioManager& audio)
    : window_(window), audio_(audio)
{
}

void UIManager::pushScreen(std::unique_ptr<Screen> screen)
{
    screen->setManager(this);
    screens_.push_back(std::move(screen));
}

void UIManager::popScreen()
{
    if (!screens_.empty())
        screens_.pop_back();
}

void UIManager::replaceScreen(std::unique_ptr<Screen> screen)
{
    screen->setManager(this);
    if (!screens_.empty())
        screens_.back() = std::move(screen);
    else
        screens_.push_back(std::move(screen));
}

void UIManager::clearScreens()
{
    screens_.clear();
}

void UIManager::handleEvent(const sf::Event& event)
{
    if (!screens_.empty())
        screens_.back()->handleEvent(event, window_);
}

void UIManager::update(float dt)
{
    if (!screens_.empty())
        screens_.back()->update(dt);
}

void UIManager::draw()
{
    if (!screens_.empty())
        screens_.back()->draw(window_);
}

} // namespace neon
