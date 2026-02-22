#pragma once

#include "InputHandler.hpp"
#include "engine/model/ClientGameState.hpp"
#include "engine/physics/PhysicsController.hpp"
#include "engine/controller/PowerUpController.hpp"
#include "engine/controller/RespawnController.hpp"
#include "engine/ai/controller/AiControllersManager.hpp"
#include "engine/ai/enums/AiType.hpp"
#include "graphics/Renderer.hpp"
#include "audio/AudioManager.hpp"
#include "ui/HUD.hpp"
#include "ui/PauseOverlay.hpp"
#include "ui/GameOverScreen.hpp"
#include "core/Clock.hpp"
#include "networking/client/ClientNetwork.hpp"
#include <SFML/Graphics.hpp>
#include <memory>

namespace neon {

class GameServer;

class GameClient {
public:
    // Local game constructor
    GameClient(std::unique_ptr<ClientGameState> gameState);

    // Online game constructor
    GameClient(std::unique_ptr<ClientGameState> gameState,
               const sf::IpAddress& serverAddr,
               GameServer* server = nullptr);

    void run();

private:
    void gameLoop();
    void gameLoopOnline();
    void handleAudio();
    void initAi();
    void sendNetworkState();

    static constexpr float kWindowWidth = 1280.f;
    static constexpr float kWindowHeight = 720.f;

    sf::RenderWindow window_;
    std::unique_ptr<ClientGameState> gameState_;
    GameClock clock_;
    PhysicsController physics_;
    PowerUpController powerUpController_;
    RespawnController respawnController_;
    std::unique_ptr<AiControllersManager> aiManager_;
    Renderer renderer_;
    AudioManager audio_;
    InputHandler input_;
    HUD hud_;
    PauseOverlay pauseOverlay_;
    GameOverScreen gameOverScreen_;

    // Online mode
    bool isOnline_ = false;
    std::unique_ptr<net::ClientNetwork> clientNetwork_;
    GameServer* gameServer_ = nullptr; // Non-owning; host keeps the server alive
};

} // namespace neon
