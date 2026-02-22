#pragma once

namespace neon {

enum class MusicTrack {
    GameTheme,
    MenuTheme,
    NeonFlicker
};

inline const char* musicPath(MusicTrack track)
{
    switch (track) {
        case MusicTrack::GameTheme:   return "assets/audio/game_theme.mp3";
        case MusicTrack::MenuTheme:   return "assets/audio/menu_theme.mp3";
        case MusicTrack::NeonFlicker: return "assets/audio/neon_flicker.mp3";
    }
    return "";
}

} // namespace neon
