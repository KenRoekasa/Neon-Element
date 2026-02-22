#pragma once

#include "networking/server/ServerNetwork.hpp"
#include "engine/model/ServerGameState.hpp"
#include "engine/physics/PhysicsController.hpp"
#include "engine/controller/PowerUpController.hpp"
#include "engine/controller/RespawnController.hpp"
#include "engine/controller/GameTypeHandler.hpp"
#include "core/Clock.hpp"
#include <thread>
#include <atomic>
#include <chrono>

namespace neon {

class GameServer {
public:
    explicit GameServer(ServerGameState& state);
    ~GameServer();

    void start();
    void stop();

    bool isRunning() const { return running_.load(); }

private:
    void run();
    void waitForPlayersToConnect();
    void gameLoop();
    void sendLocations();
    void sendHealthUpdates();

    ServerGameState& state_;
    std::unique_ptr<net::ServerNetwork> network_;
    PhysicsController physics_;
    PowerUpController puController_;
    RespawnController resController_;
    GameClock clock_;

    std::jthread thread_;
    std::atomic<bool> running_{false};
};

} // namespace neon
