#pragma once

#include <SFML/System/Clock.hpp>

namespace neon {

class GameClock {
public:
    GameClock() = default;

    void start()
    {
        clock_.restart();
        running_ = true;
        paused_ = false;
        totalPauseTime_ = 0.f;
        elapsed_ = 0.f;
        deltaTime_ = 0.f;
    }

    void update()
    {
        if (!running_ || paused_)
            return;

        float now = clock_.getElapsedTime().asSeconds() - totalPauseTime_;
        deltaTime_ = now - elapsed_;
        elapsed_ = now;
    }

    void pause()
    {
        if (!paused_) {
            paused_ = true;
            pauseStart_ = clock_.getElapsedTime().asSeconds();
        }
    }

    void resume()
    {
        if (paused_) {
            totalPauseTime_ += clock_.getElapsedTime().asSeconds() - pauseStart_;
            paused_ = false;
        }
    }

    // Time since game start in seconds
    float elapsed() const { return elapsed_; }

    // Time since game start in milliseconds (int64 for compatibility with Java code)
    long long elapsedMs() const { return static_cast<long long>(elapsed_ * 1000.f); }

    // Time since last frame in seconds
    float deltaTime() const { return deltaTime_; }

    // Delta time scaled to match Java's frame-dependent movement
    // Java used DeltaTime.deltaTime which was frame-count-based
    float deltaTimeScaled() const { return deltaTime_ * 1000.f; }

    bool isRunning() const { return running_; }
    bool isPaused() const { return paused_; }

private:
    sf::Clock clock_;
    bool running_ = false;
    bool paused_ = false;
    float elapsed_ = 0.f;
    float deltaTime_ = 0.f;
    float pauseStart_ = 0.f;
    float totalPauseTime_ = 0.f;
};

} // namespace neon
