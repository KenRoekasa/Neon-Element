#include "client/GameClient.hpp"
#include "engine/model/generator/GameStateGenerator.hpp"
#include "engine/ai/enums/AiType.hpp"
#include "ui/UIManager.hpp"
#include "ui/MenuScreen.hpp"
#include "audio/AudioManager.hpp"
#include "server/GameServer.hpp"
#include <SFML/Graphics.hpp>
#include <memory>

int main()
{
    sf::RenderWindow window(sf::VideoMode(1280, 720), "Neon Element",
                            sf::Style::Titlebar | sf::Style::Close);
    window.setFramerateLimit(60);

    neon::AudioManager audio;
    audio.loadAll();
    audio.playMusic(neon::MusicTrack::MenuTheme);

    neon::UIManager ui(window, audio);
    ui.pushScreen(std::make_unique<neon::MenuScreen>());

    bool startGame = false;
    bool startOnlineGame = false;
    int enemyCount = 3;
    int difficulty = 1;
    neon::GameType::Type gameMode = neon::GameType::Type::FirstToXKills;

    // Online game params
    bool isHost = false;
    std::string serverAddr = "localhost";
    int numOnlinePlayers = 2;

    ui.setOnStartGame([&](int enemies, int diff, neon::GameType::Type mode) {
        startGame = true;
        enemyCount = enemies;
        difficulty = diff;
        gameMode = mode;
    });

    ui.setOnStartOnlineGame([&](bool host, const std::string& addr,
                                 neon::GameType::Type mode, int numPlayers) {
        startOnlineGame = true;
        isHost = host;
        serverAddr = addr;
        gameMode = mode;
        numOnlinePlayers = numPlayers;
    });

    // Menu loop
    while (window.isOpen() && !startGame && !startOnlineGame) {
        sf::Event event{};
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed) {
                window.close();
                break;
            }
            if (event.type == sf::Event::KeyPressed && event.key.code == sf::Keyboard::Escape) {
                if (ui.hasScreens()) {
                    auto* current = ui.currentScreen();
                    if (dynamic_cast<neon::MenuScreen*>(current)) {
                        window.close();
                    } else {
                        ui.popScreen();
                    }
                }
            }
            ui.handleEvent(event);
        }
        if (!window.isOpen()) break;

        ui.update(1.f / 60.f);
        window.clear(sf::Color(10, 0, 20));
        ui.draw();
        window.display();
    }

    if (!window.isOpen()) return 0;

    // Close menu window and start game
    audio.stopMusic();
    window.close();

    if (startGame) {
        // Local game
        auto gameState = neon::GameStateGenerator::createDemoGameState(
            enemyCount, gameMode);

        neon::GameClient client(std::move(gameState));
        client.run();
    } else if (startOnlineGame) {
        // Online game
        std::unique_ptr<neon::GameServer> server;

        if (isHost) {
            // Create server game state and start server
            auto serverState = neon::GameStateGenerator::createServerGameState(
                numOnlinePlayers, gameMode);
            server = std::make_unique<neon::GameServer>(*serverState);
            server->start();
        }

        // Create client game state (empty — will be populated by network)
        auto clientState = neon::GameStateGenerator::createEmptyClientGameState(gameMode);
        sf::IpAddress addr(serverAddr);

        neon::GameClient client(std::move(clientState), addr, server.get());
        client.run();

        // Server stops when GameClient exits
        if (server) server->stop();
    }

    return 0;
}
