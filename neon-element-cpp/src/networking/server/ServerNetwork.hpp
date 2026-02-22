#pragma once

#include "networking/Network.hpp"
#include "ServerNetworkDispatcher.hpp"
#include "ServerNetworkHandler.hpp"
#include "ConnectedPlayers.hpp"
#include "engine/model/ServerGameState.hpp"
#include <memory>

namespace neon::net {

class ServerNetwork : public Network {
public:
    explicit ServerNetwork(ServerGameState& state)
    {
        bind(SERVER_LISTENING_PORT);
        connectedPlayers_ = std::make_unique<ConnectedPlayers>();
        dispatcher_ = std::make_unique<ServerNetworkDispatcher>(
            socket_, state, *connectedPlayers_);
        handler_ = std::make_unique<ServerNetworkHandler>(
            state, *connectedPlayers_, *dispatcher_);
        start();
    }

    ServerNetworkDispatcher& getDispatcher() { return *dispatcher_; }
    ServerNetworkHandler& getHandler() { return *handler_; }
    ConnectedPlayers& getConnectedPlayers() { return *connectedPlayers_; }

protected:
    void parse(std::unique_ptr<Packet> packet) override
    {
        auto* serverPkt = dynamic_cast<PacketToServer*>(packet.get());
        if (serverPkt) {
            serverPkt->handle(*handler_);
        }
    }

private:
    std::unique_ptr<ConnectedPlayers> connectedPlayers_;
    std::unique_ptr<ServerNetworkDispatcher> dispatcher_;
    std::unique_ptr<ServerNetworkHandler> handler_;
};

} // namespace neon::net
