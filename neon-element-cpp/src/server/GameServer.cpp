#include "GameServer.hpp"
#include <chrono>
#include <thread>

namespace neon {

GameServer::GameServer(ServerGameState& state)
    : state_(state)
    , physics_(state)
    , puController_(state)
    , resController_(state)
{
    network_ = std::make_unique<net::ServerNetwork>(state_);
}

GameServer::~GameServer()
{
    stop();
}

void GameServer::start()
{
    if (running_.exchange(true)) return;
    thread_ = std::jthread([this](std::stop_token) { run(); });
}

void GameServer::stop()
{
    running_.store(false);
    if (thread_.joinable()) {
        thread_.request_stop();
        thread_.join();
    }
    network_->stop();
}

void GameServer::run()
{
    // Phase 1: wait for all players to connect
    waitForPlayersToConnect();

    if (!running_.load()) return;

    // Phase 2: game loop
    gameLoop();

    // Game over
    network_->getDispatcher().broadcastGameEnded();
    network_->stop();
}

void GameServer::waitForPlayersToConnect()
{
    auto& connPlayers = network_->getConnectedPlayers();

    while (running_.load()) {
        if (connPlayers.count() >= state_.getNumPlayers()) {
            // All players connected
            connPlayers.assignStartingLocations(
                state_.getMap().getWidth(),
                state_.getMap().getHeight());

            state_.rebuildPlayerList();
            state_.getScoreBoard().initialise(state_.getAllPlayers());

            // Broadcast initial game state
            network_->getDispatcher().broadcastGameState();

            // Wait for all clients to ACK
            while (running_.load() && !connPlayers.allHaveInitialGameState()) {
                std::this_thread::sleep_for(std::chrono::milliseconds(1));
            }

            if (!running_.load()) return;

            // Start the game
            state_.setStarted(true);
            state_.start();
            network_->getDispatcher().broadcastGameStarted();
            clock_.start();
            return;
        }

        std::this_thread::sleep_for(std::chrono::milliseconds(1));
    }
}

void GameServer::gameLoop()
{
    using namespace std::chrono;

    while (running_.load()) {
        clock_.update();

        physics_.serverLoop(clock_);
        puController_.update(clock_);
        resController_.update(clock_);

        if (!GameTypeHandler::checkRunning(state_, clock_)) {
            state_.stop();
            break;
        }

        sendLocations();
        sendHealthUpdates();

        std::this_thread::sleep_for(milliseconds(15));
    }
}

void GameServer::sendLocations()
{
    auto& dispatcher = network_->getDispatcher();
    auto& players = state_.getAllPlayers();

    for (auto* player : players) {
        if (player->isAlive()) {
            auto loc = player->getLocation();
            dispatcher.broadcastLocationState(
                player->getId(),
                static_cast<double>(loc.x),
                static_cast<double>(loc.y),
                static_cast<double>(player->getAngle()));
        }
    }
}

void GameServer::sendHealthUpdates()
{
    auto& dispatcher = network_->getDispatcher();
    auto& players = state_.getAllPlayers();

    for (auto* player : players) {
        dispatcher.broadcastHealthState(player->getId(), player->getHealth());
    }
}

} // namespace neon
