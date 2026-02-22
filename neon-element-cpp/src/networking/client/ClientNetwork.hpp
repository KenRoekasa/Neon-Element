#pragma once

#include "networking/Network.hpp"
#include "ClientNetworkDispatcher.hpp"
#include "ClientNetworkHandler.hpp"
#include "engine/model/ClientGameState.hpp"
#include <memory>

namespace neon::net {

class ClientNetwork : public Network {
public:
    ClientNetwork(ClientGameState& state, const sf::IpAddress& serverAddr)
    {
        bindAny();
        dispatcher_ = std::make_unique<ClientNetworkDispatcher>(socket_, serverAddr);
        handler_    = std::make_unique<ClientNetworkHandler>(state, *dispatcher_);
        start();
    }

    ClientNetworkDispatcher& getDispatcher() { return *dispatcher_; }
    ClientNetworkHandler& getHandler() { return *handler_; }

protected:
    void parse(std::unique_ptr<Packet> packet) override
    {
        auto* clientPkt = dynamic_cast<PacketToClient*>(packet.get());
        if (clientPkt) {
            clientPkt->handle(*handler_);
        }
    }

private:
    std::unique_ptr<ClientNetworkDispatcher> dispatcher_;
    std::unique_ptr<ClientNetworkHandler> handler_;
};

} // namespace neon::net
