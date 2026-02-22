#include "GameClient.hpp"
#include "engine/model/generator/GameStateGenerator.hpp"
#include "server/GameServer.hpp"

namespace neon {

// Local game constructor
GameClient::GameClient(std::unique_ptr<ClientGameState> gameState)
    : window_(sf::VideoMode(static_cast<unsigned>(kWindowWidth), static_cast<unsigned>(kWindowHeight)),
              "Neon Element", sf::Style::Titlebar | sf::Style::Close)
    , gameState_(std::move(gameState))
    , physics_(*gameState_)
    , powerUpController_(*gameState_)
    , respawnController_(*gameState_)
    , renderer_(kWindowWidth, kWindowHeight)
{
    window_.setFramerateLimit(60);

    pauseOverlay_.setOnResume([this]() { gameState_->resume(); });
    pauseOverlay_.setOnQuit([this]() { window_.close(); });
    gameOverScreen_.setOnQuit([this]() { window_.close(); });
    gameOverScreen_.setOnPlayAgain([this]() { window_.close(); });
}

// Online game constructor
GameClient::GameClient(std::unique_ptr<ClientGameState> gameState,
                       const sf::IpAddress& serverAddr,
                       GameServer* server)
    : window_(sf::VideoMode(static_cast<unsigned>(kWindowWidth), static_cast<unsigned>(kWindowHeight)),
              "Neon Element - Online", sf::Style::Titlebar | sf::Style::Close)
    , gameState_(std::move(gameState))
    , physics_(*gameState_)
    , powerUpController_(*gameState_)
    , respawnController_(*gameState_)
    , renderer_(kWindowWidth, kWindowHeight)
    , isOnline_(true)
    , gameServer_(server)
{
    window_.setFramerateLimit(60);

    pauseOverlay_.setOnResume([this]() { gameState_->resume(); });
    pauseOverlay_.setOnQuit([this]() { window_.close(); });
    gameOverScreen_.setOnQuit([this]() { window_.close(); });
    gameOverScreen_.setOnPlayAgain([this]() { window_.close(); });

    // Create network and perform handshake
    clientNetwork_ = std::make_unique<net::ClientNetwork>(*gameState_, serverAddr);
    clientNetwork_->getDispatcher().sendHello();
}

void GameClient::run()
{
    audio_.loadAll();
    audio_.playMusic(MusicTrack::GameTheme);

    if (isOnline_) {
        // Online: send connect and wait for game to start
        clientNetwork_->getDispatcher().sendConnect();

        // Wait for server to send initial state and start
        while (!gameState_->getRunning() && window_.isOpen()) {
            sf::Event event{};
            while (window_.pollEvent(event)) {
                if (event.type == sf::Event::Closed) {
                    window_.close();
                    return;
                }
            }
            window_.clear(sf::Color(10, 0, 20));
            window_.display();
            sf::sleep(sf::milliseconds(16));
        }

        clock_.start();
        gameLoopOnline();
    } else {
        initAi();
        clock_.start();
        gameState_->start();
        gameLoop();
    }
}

void GameClient::initAi()
{
    aiManager_ = std::make_unique<AiControllersManager>(
        gameState_->getObjects(),
        Rect(0.f, 0.f, gameState_->getMap().getWidth(), gameState_->getMap().getHeight()),
        &gameState_->getScoreBoard(),
        gameState_->getGameType(),
        clock_);

    for (auto* player : gameState_->getAllPlayers()) {
        if (player != gameState_->getPlayer()) {
            aiManager_->registerExistingAi(player, AiType::Normal);
        }
    }
}

void GameClient::gameLoop()
{
    while (window_.isOpen()) {
        clock_.update();

        sf::Event event{};
        while (window_.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window_.close();
                break;
            }
            if (event.type == sf::Event::KeyPressed) {
                if (event.key.code == sf::Keyboard::Escape) {
                    window_.close();
                    break;
                }
                if (event.key.code == sf::Keyboard::P) {
                    if (gameState_->isPaused())
                        gameState_->resume();
                    else
                        gameState_->pause();
                }
            }

            if (gameState_->isPaused()) {
                pauseOverlay_.handleEvent(event, window_);
            } else if (!gameState_->getRunning()) {
                gameOverScreen_.handleEvent(event, window_);
            }
        }

        if (!window_.isOpen()) break;

        if (gameState_->isPaused()) {
            clock_.pause();
            if (aiManager_) aiManager_->pauseAllAi();

            window_.clear(sf::Color(10, 0, 20));
            renderer_.render(window_, *gameState_, clock_);
            pauseOverlay_.draw(window_);
            window_.display();
            continue;
        } else {
            clock_.resume();
        }

        if (!gameState_->getRunning()) {
            gameOverScreen_.init(*gameState_);

            window_.clear(sf::Color(10, 0, 20));
            renderer_.render(window_, *gameState_, clock_);
            gameOverScreen_.draw(window_);
            window_.display();
            continue;
        }

        input_.handleInput(window_, *gameState_, clock_);
        physics_.clientLoop(clock_);

        if (aiManager_) aiManager_->updateAllAi();

        powerUpController_.update(clock_);
        respawnController_.update(clock_);

        handleAudio();
        hud_.update(*gameState_, clock_);

        window_.clear(sf::Color(10, 0, 20));
        renderer_.render(window_, *gameState_, clock_);
        hud_.draw(window_, *gameState_);
        window_.display();
    }

    audio_.stopMusic();
}

void GameClient::gameLoopOnline()
{
    while (window_.isOpen()) {
        clock_.update();

        sf::Event event{};
        while (window_.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window_.close();
                break;
            }
            if (event.type == sf::Event::KeyPressed) {
                if (event.key.code == sf::Keyboard::Escape) {
                    window_.close();
                    break;
                }
                if (event.key.code == sf::Keyboard::P) {
                    if (gameState_->isPaused())
                        gameState_->resume();
                    else
                        gameState_->pause();
                }
            }

            if (gameState_->isPaused()) {
                pauseOverlay_.handleEvent(event, window_);
            } else if (!gameState_->getRunning()) {
                gameOverScreen_.handleEvent(event, window_);
            }
        }

        if (!window_.isOpen()) break;

        if (gameState_->isPaused()) {
            clock_.pause();
            window_.clear(sf::Color(10, 0, 20));
            renderer_.render(window_, *gameState_, clock_);
            pauseOverlay_.draw(window_);
            window_.display();
            continue;
        } else {
            clock_.resume();
        }

        if (!gameState_->getRunning()) {
            gameOverScreen_.init(*gameState_);
            window_.clear(sf::Color(10, 0, 20));
            renderer_.render(window_, *gameState_, clock_);
            gameOverScreen_.draw(window_);
            window_.display();
            continue;
        }

        // Online mode: handle input locally, send to server
        input_.handleInput(window_, *gameState_, clock_);
        sendNetworkState();

        // Use dumb client physics (just interpolate positions from server)
        physics_.dumbClientLoop(clock_);

        handleAudio();
        hud_.update(*gameState_, clock_);

        window_.clear(sf::Color(10, 0, 20));
        renderer_.render(window_, *gameState_, clock_);
        hud_.draw(window_, *gameState_);
        window_.display();
    }

    // Disconnect cleanly
    if (clientNetwork_) {
        clientNetwork_->getDispatcher().sendDisconnect();
        clientNetwork_->stop();
    }
    audio_.stopMusic();
}

void GameClient::sendNetworkState()
{
    if (!clientNetwork_) return;

    auto* player = gameState_->getPlayer();
    if (!player) return;

    auto& dispatcher = clientNetwork_->getDispatcher();
    auto loc = player->getLocation();

    dispatcher.sendLocationState(
        static_cast<double>(loc.x),
        static_cast<double>(loc.y),
        static_cast<double>(player->getAngle()));

    if (player->getCurrentAction() != Action::Idle) {
        dispatcher.sendActionState(player->getCurrentAction());
    }
}

void GameClient::handleAudio()
{
    auto* player = gameState_->getPlayer();
    if (!player) return;

    for (auto* p : gameState_->getAllPlayers()) {
        if (!p->hasActionSounded()) {
            switch (p->getCurrentAction()) {
                case Action::Light:
                    audio_.playSound(SoundEffect::LightAttack);
                    p->setActionHasSounded(true);
                    break;
                case Action::Charge:
                    audio_.playSound(SoundEffect::Charge);
                    p->setActionHasSounded(true);
                    break;
                case Action::Heavy:
                    audio_.playSound(SoundEffect::HeavyAttack);
                    p->setActionHasSounded(true);
                    break;
                case Action::Block:
                    audio_.playSound(SoundEffect::Shield);
                    p->setActionHasSounded(true);
                    break;
                default:
                    break;
            }
        }
    }
}

} // namespace neon
