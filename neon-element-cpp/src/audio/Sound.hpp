#pragma once

namespace neon {

enum class SoundEffect {
    LightAttack,
    HeavyAttack,
    Charge,
    Charge2,
    LightHit,
    HeavyHit,
    Shield,
    Shield2,
    Button,
    Button1,
    Button2,
    Button3,
    Button4,
    Hover1,
    Hover2
};

inline const char* soundPath(SoundEffect sfx)
{
    switch (sfx) {
        case SoundEffect::LightAttack: return "assets/audio/LIGHT_ATTACK.mp3";
        case SoundEffect::HeavyAttack: return "assets/audio/HEAVY_ATTACK.mp3";
        case SoundEffect::Charge:      return "assets/audio/CHARGE.mp3";
        case SoundEffect::Charge2:     return "assets/audio/CHARGE2.mp3";
        case SoundEffect::LightHit:    return "assets/audio/LIGHT_HIT.mp3";
        case SoundEffect::HeavyHit:    return "assets/audio/HEAVY_HIT.mp3";
        case SoundEffect::Shield:      return "assets/audio/SHIELD.mp3";
        case SoundEffect::Shield2:     return "assets/audio/SHIELD2.mp3";
        case SoundEffect::Button:      return "assets/audio/button.mp3";
        case SoundEffect::Button1:     return "assets/audio/button_1.mp3";
        case SoundEffect::Button2:     return "assets/audio/button_2.mp3";
        case SoundEffect::Button3:     return "assets/audio/button_3.mp3";
        case SoundEffect::Button4:     return "assets/audio/button_4.mp3";
        case SoundEffect::Hover1:      return "assets/audio/hover_1.mp3";
        case SoundEffect::Hover2:      return "assets/audio/hover_2.mp3";
    }
    return "";
}

} // namespace neon
