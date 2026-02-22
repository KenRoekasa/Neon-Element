#pragma once

#include "engine/model/ClientGameState.hpp"
#include "engine/model/enums/ObjectType.hpp"
#include "engine/entities/Player.hpp"
#include "engine/entities/PowerUp.hpp"

namespace neon::net {

class ClientNetworkDispatcher;

// Handles incoming server → client packets, updating ClientGameState
class ClientNetworkHandler {
public:
    ClientNetworkHandler(ClientGameState& state, ClientNetworkDispatcher& dispatcher)
        : state_(state), dispatcher_(dispatcher) {}

    void receiveHelloAck(class HelloAckPacket& packet);
    void receiveConnectAck(class ConnectAckPacket& packet);
    void receiveConnectBroadcast(class ConnectBroadcast& packet);
    void receiveDisconnectAck(class DisconnectAckPacket& packet);
    void receiveDisconnectBroadcast(class DisconnectBroadcast& packet);
    void receiveReadyStateBroadcast(class ReadyStateBroadcast& packet);
    void receiveLocationStateBroadcast(class LocationStateBroadcast& packet);
    void receiveElementStateBroadcast(class ElementStateBroadcast& packet);
    void receiveHealthStateBroadcast(class HealthStateBroadcast& packet);
    void receivePowerUpPickUpBroadcast(class PowerUpPickUpBroadcast& packet);
    void receivePowerUpBroadcast(class PowerUpBroadcast& packet);
    void receiveInitialGameStateBroadcast(class InitialGameStateBroadcast& packet);
    void receiveActionStateBroadcast(class ActionStateBroadcast& packet);
    void receiveRespawnBroadcast(class RespawnBroadcast& packet);
    void receiveScoreBroadcast(class ScoreBroadcast& packet);
    void receiveGameStartBroadcast(class GameStartBroadcast& packet);
    void receiveGameOverBroadcast(class GameOverBroadcast& packet);

private:
    Player* findPlayer(int id);

    ClientGameState& state_;
    ClientNetworkDispatcher& dispatcher_;
};

} // namespace neon::net
