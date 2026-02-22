#pragma once

#include "engine/model/ServerGameState.hpp"
#include "ConnectedPlayers.hpp"

namespace neon::net {

class ServerNetworkDispatcher;

// Forward declarations for packet types
class HelloPacket;
class ConnectPacket;
class DisconnectPacket;
class LocationStatePacket;
class ActionStatePacket;
class ElementStatePacket;
class ReadyStatePacket;
class PowerUpPacket;
class InitialGameStateAckPacket;

// Handles incoming client → server packets
class ServerNetworkHandler {
public:
    ServerNetworkHandler(ServerGameState& state,
                          ConnectedPlayers& players,
                          ServerNetworkDispatcher& dispatcher)
        : state_(state), players_(players), dispatcher_(dispatcher) {}

    void receiveHello(HelloPacket& packet);
    void receiveConnect(ConnectPacket& packet);
    void receiveDisconnect(DisconnectPacket& packet);
    void receiveLocationState(LocationStatePacket& packet);
    void receiveActionState(ActionStatePacket& packet);
    void receiveElementState(ElementStatePacket& packet);
    void receiveReadyState(ReadyStatePacket& packet);
    void receivePowerUp(PowerUpPacket& packet);
    void receiveInitialGameStateAck(InitialGameStateAckPacket& packet);

private:
    ServerGameState& state_;
    ConnectedPlayers& players_;
    ServerNetworkDispatcher& dispatcher_;
};

} // namespace neon::net
