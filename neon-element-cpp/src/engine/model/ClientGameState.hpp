#pragma once

#include "GameState.hpp"

namespace neon {

class ClientGameState : public GameState {
public:
    ClientGameState(Player* player,
                    Map map,
                    std::vector<std::unique_ptr<PhysicsObject>> objects,
                    ScoreBoard scoreBoard,
                    std::unique_ptr<GameType> gameType,
                    GameType::Type mode);

    Player* getPlayer() { return player_; }
    const Player* getPlayer() const { return player_; }
    void setPlayer(Player* p) { player_ = p; }

    int getClientId() const { return clientId_; }
    void setClientId(int id) { clientId_ = id; }

    GameType::Type getMode() const { return mode_; }
    void setMode(GameType::Type mode) { mode_ = mode; }

    bool isPaused() const { return paused_; }
    void pause() { paused_ = true; }
    void resume() { paused_ = false; }

    bool isTab() const { return tab_; }
    void setTab(bool tab) { tab_ = tab; }

private:
    Player* player_ = nullptr;
    int clientId_ = 0;
    GameType::Type mode_;
    bool paused_ = false;
    bool tab_ = false;
};

} // namespace neon
