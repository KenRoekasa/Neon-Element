#pragma once

#include "Screen.hpp"
#include "Widget.hpp"
#include "networking/client/ClientNetwork.hpp"
#include "networking/server/ServerNetwork.hpp"
#include "engine/model/ClientGameState.hpp"
#include "engine/model/ServerGameState.hpp"
#include "server/GameServer.hpp"
#include <memory>
#include <string>
#include <vector>

namespace neon {

class LobbyScreen : public Screen {
public:
    explicit LobbyScreen(bool isHost);

    void handleEvent(const sf::Event& event, sf::RenderWindow& window) override;
    void update(float dt) override;
    void draw(sf::RenderWindow& window) override;

    void addPlayer(const std::string& name);

private:
    void initWidgets();
    void startNetworking();

    sf::Font font_;
    bool fontLoaded_ = false;
    bool isHost_;
    bool networkStarted_ = false;

    std::unique_ptr<Label> title_;
    std::unique_ptr<Label> statusLabel_;
    std::unique_ptr<Label> ipLabel_;
    std::vector<std::unique_ptr<Label>> playerLabels_;
    std::unique_ptr<Button> startBtn_;
    std::unique_ptr<Button> backBtn_;

    std::vector<std::string> playerNames_;
};

} // namespace neon
