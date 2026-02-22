#pragma once

#include "Sound.hpp"
#include "Music.hpp"
#include <SFML/Audio.hpp>
#include <unordered_map>
#include <array>
#include <iostream>

namespace neon {

class AudioManager {
public:
    static constexpr int kMaxConcurrentSounds = 16;

    AudioManager() { sounds_.fill(sf::Sound()); }

    void loadAll()
    {
        // SFML's SoundBuffer doesn't support MP3 natively in all builds.
        // We attempt to load; if it fails, audio just won't play for that effect.
        loadBuffer(SoundEffect::LightAttack);
        loadBuffer(SoundEffect::HeavyAttack);
        loadBuffer(SoundEffect::Charge);
        loadBuffer(SoundEffect::LightHit);
        loadBuffer(SoundEffect::HeavyHit);
        loadBuffer(SoundEffect::Shield);
        loadBuffer(SoundEffect::Button);
        loadBuffer(SoundEffect::Hover1);
    }

    void playSound(SoundEffect sfx, float volume = 100.f)
    {
        auto it = buffers_.find(sfx);
        if (it == buffers_.end()) return;

        auto& sound = sounds_[nextSound_];
        sound.setBuffer(it->second);
        sound.setVolume(volume);
        sound.play();
        nextSound_ = (nextSound_ + 1) % kMaxConcurrentSounds;
    }

    void playMusic(MusicTrack track, bool loop = true)
    {
        if (music_.openFromFile(musicPath(track))) {
            music_.setLoop(loop);
            music_.play();
        }
    }

    void stopMusic() { music_.stop(); }
    void setMusicVolume(float vol) { music_.setVolume(vol); }
    void setSoundVolume(float vol) { masterVolume_ = vol; }

private:
    void loadBuffer(SoundEffect sfx)
    {
        sf::SoundBuffer buf;
        if (buf.loadFromFile(soundPath(sfx)))
            buffers_[sfx] = std::move(buf);
    }

    std::unordered_map<SoundEffect, sf::SoundBuffer> buffers_;
    std::array<sf::Sound, kMaxConcurrentSounds> sounds_;
    int nextSound_ = 0;
    sf::Music music_;
    float masterVolume_ = 100.f;
};

} // namespace neon
