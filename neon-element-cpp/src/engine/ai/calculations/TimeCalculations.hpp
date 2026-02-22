#pragma once

#include <chrono>

namespace neon {

class TimeCalculations {
public:
    TimeCalculations()
        : startTime_(currentTime()), wanderingTime_(0.f), tickCtr_(0), paused_(false), pausingTime_(0.f) {}

    bool hasBeenWanderingFor(int seconds)
    {
        if (paused_) resetStartingTimes();
        if (wanderingTime_ == 0.f) wanderingTime_ = currentTime();
        float endTime = currentTime();
        if (endTime - wanderingTime_ >= static_cast<float>(seconds)) {
            wanderingTime_ = endTime;
            return true;
        }
        return false;
    }

    float secondsElapsed() const
    {
        return currentTime() - startTime_;
    }

    void setStartTime(float time) { startTime_ = time; }

    void setPaused(bool paused)
    {
        if (paused) pausingTime_ = currentTime();
        paused_ = paused;
    }

    void tick() { tickCtr_++; }

    bool gameTicked(int times)
    {
        if (tickCtr_ >= times) {
            tickCtr_ = 0;
            return true;
        }
        return false;
    }

    int getTickCtr() const { return tickCtr_; }

private:
    static float currentTime()
    {
        using namespace std::chrono;
        return static_cast<float>(duration_cast<seconds>(
            steady_clock::now().time_since_epoch()).count());
    }

    void resetStartingTimes()
    {
        paused_ = false;
        wanderingTime_ += pausingTime_;
        startTime_ += pausingTime_;
    }

    float startTime_;
    float wanderingTime_;
    int tickCtr_;
    bool paused_;
    float pausingTime_;
};

} // namespace neon
