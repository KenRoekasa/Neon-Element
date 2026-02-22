#include "ClientNetworkHandler.hpp"
#include "ClientNetworkDispatcher.hpp"
#include "networking/packets/HelloPackets.hpp"
#include "networking/packets/ConnectPackets.hpp"
#include "networking/packets/DisconnectPackets.hpp"
#include "networking/packets/BroadcastPackets.hpp"
#include "networking/packets/GamePackets.hpp"
#include "engine/model/enums/ObjectType.hpp"
#include <algorithm>

namespace neon::net {

Player* ClientNetworkHandler::findPlayer(int id)
{
    for (auto* p : state_.getAllPlayers()) {
        if (p->getId() == id) return p;
    }
    return nullptr;
}

void ClientNetworkHandler::receiveHelloAck(HelloAckPacket& packet)
{
    auto typeOpt = GameType::typeFromId(static_cast<uint8_t>(packet.getGameType()));
    if (typeOpt)
        state_.setMode(*typeOpt);
}

void ClientNetworkHandler::receiveConnectAck(ConnectAckPacket& packet)
{
    if (packet.getStatus() == ConnectAckPacket::Status::Success) {
        state_.setClientId(packet.getPlayerId());
    }
}

void ClientNetworkHandler::receiveConnectBroadcast(ConnectBroadcast& /*packet*/)
{
    // A new player connected — UI can update player count
}

void ClientNetworkHandler::receiveDisconnectAck(DisconnectAckPacket& /*packet*/)
{
    // Server acknowledged our disconnect
}

void ClientNetworkHandler::receiveDisconnectBroadcast(DisconnectBroadcast& /*packet*/)
{
    // Another player disconnected
}

void ClientNetworkHandler::receiveReadyStateBroadcast(ReadyStateBroadcast& /*packet*/)
{
    // Ready state updated in lobby
}

void ClientNetworkHandler::receiveLocationStateBroadcast(LocationStateBroadcast& packet)
{
    if (packet.getPlayerId() == state_.getClientId()) return;

    Player* player = findPlayer(packet.getPlayerId());
    if (player) {
        player->setLocation(static_cast<float>(packet.getX()),
                            static_cast<float>(packet.getY()));
        player->setAngle(static_cast<float>(packet.getAngle()));
    }
}

void ClientNetworkHandler::receiveElementStateBroadcast(ElementStateBroadcast& packet)
{
    Player* player = findPlayer(packet.getPlayerId());
    if (player) {
        player->setCurrentElement(packet.getElement());
    }
}

void ClientNetworkHandler::receiveHealthStateBroadcast(HealthStateBroadcast& packet)
{
    Player* player = findPlayer(packet.getPlayerId());
    if (player) {
        player->setHealth(packet.getHealth());
    }
}

void ClientNetworkHandler::receivePowerUpPickUpBroadcast(PowerUpPickUpBroadcast& packet)
{
    for (auto& obj : state_.getObjects()) {
        auto* pu = dynamic_cast<PowerUp*>(obj.get());
        if (pu && pu->getId() == packet.getPickupId()) {
            pu->setActive(false);
            break;
        }
    }
}

void ClientNetworkHandler::receivePowerUpBroadcast(PowerUpBroadcast& packet)
{
    auto pu = std::make_unique<PowerUp>(
        packet.getId(),
        static_cast<float>(packet.getX()),
        static_cast<float>(packet.getY()),
        packet.getPowerUpType());
    state_.getObjects().push_back(std::move(pu));
}

void ClientNetworkHandler::receiveInitialGameStateBroadcast(
    InitialGameStateBroadcast& packet)
{
    // Create player objects for all players in the game
    for (auto& entry : packet.getPlayers()) {
        auto player = std::make_unique<Player>(ObjectType::Player, entry.id);
        player->setLocation(static_cast<float>(entry.x),
                            static_cast<float>(entry.y));

        if (entry.id == state_.getClientId()) {
            state_.setPlayer(player.get());
        }
        state_.getObjects().push_back(std::move(player));
    }
    state_.rebuildPlayerList();

    // Acknowledge receipt
    dispatcher_.sendInitialGameStateAck();
}

void ClientNetworkHandler::receiveActionStateBroadcast(ActionStateBroadcast& packet)
{
    if (packet.getPlayerId() == state_.getClientId()) return;

    Player* player = findPlayer(packet.getPlayerId());
    if (player) {
        player->setCurrentAction(packet.getAction());
    }
}

void ClientNetworkHandler::receiveRespawnBroadcast(RespawnBroadcast& packet)
{
    Player* player = findPlayer(packet.getPlayerId());
    if (player) {
        player->setLocation(static_cast<float>(packet.getX()),
                            static_cast<float>(packet.getY()));
        player->respawn();

        // Remove from dead players list
        auto& dead = state_.getDeadPlayers();
        dead.erase(std::remove(dead.begin(), dead.end(), player), dead.end());
    }
}

void ClientNetworkHandler::receiveScoreBroadcast(ScoreBroadcast& packet)
{
    auto& sb = state_.getScoreBoard();
    sb.addScore(packet.getPlayerId(), packet.getScore());
    sb.addKill(packet.getPlayerId(), packet.getVictimId());
}

void ClientNetworkHandler::receiveGameStartBroadcast(GameStartBroadcast& packet)
{
    if (packet.isStarted()) {
        state_.start();
    }
}

void ClientNetworkHandler::receiveGameOverBroadcast(GameOverBroadcast& /*packet*/)
{
    state_.stop();
}

} // namespace neon::net
