#include "ServerNetworkHandler.hpp"
#include "ServerNetworkDispatcher.hpp"
#include "networking/packets/HelloPackets.hpp"
#include "networking/packets/ConnectPackets.hpp"
#include "networking/packets/DisconnectPackets.hpp"
#include "networking/packets/StatePackets.hpp"
#include "networking/packets/GamePackets.hpp"
#include "engine/model/enums/ObjectType.hpp"
#include "engine/model/GameType.hpp"
#include "engine/model/gametypes/FirstToXKillsGame.hpp"
#include "engine/model/gametypes/TimedGame.hpp"
#include "engine/model/gametypes/HillGame.hpp"
#include "engine/model/gametypes/Regicide.hpp"

namespace neon::net {

// Helper: get the game param (kills needed, time, etc.) from the game type
static int getGameParam(const GameType& gt)
{
    switch (gt.getType()) {
        case GameType::Type::FirstToXKills:
            return static_cast<const FirstToXKillsGame&>(gt).getKillsNeeded();
        case GameType::Type::Timed:
            return static_cast<int>(static_cast<const TimedGame&>(gt).getDuration());
        case GameType::Type::Hill:
            return static_cast<const HillGame&>(gt).getScoreNeeded();
        case GameType::Type::Regicide:
            return static_cast<const Regicide&>(gt).getScoreNeeded();
    }
    return 0;
}

void ServerNetworkHandler::receiveHello(HelloPacket& packet)
{
    auto& sender = packet.getSender();
    HelloAckPacket ack(
        players_.count(),
        state_.getNumPlayers(),
        state_.getGameType().getType(),
        getGameParam(state_.getGameType()));
    dispatcher_.sendTo(ack, sender.address, sender.port);
}

void ServerNetworkHandler::receiveConnect(ConnectPacket& packet)
{
    auto& sender = packet.getSender();

    // Check if game already started
    if (state_.isStarted()) {
        ConnectAckPacket ack(0, ConnectAckPacket::Status::ErrGameStarted);
        dispatcher_.sendTo(ack, sender.address, sender.port);
        return;
    }

    // Check max players
    if (players_.count() >= state_.getNumPlayers()) {
        ConnectAckPacket ack(0, ConnectAckPacket::Status::ErrMaxPlayers);
        dispatcher_.sendTo(ack, sender.address, sender.port);
        return;
    }

    // Create new player and add to game
    auto player = std::make_unique<Player>(ObjectType::Player);
    int playerId = player->getId();
    Player* playerPtr = player.get();

    state_.getObjects().push_back(std::move(player));
    state_.rebuildPlayerList();

    players_.addConnection(playerPtr, sender.address, sender.port);

    // Acknowledge
    ConnectAckPacket ack(playerId, ConnectAckPacket::Status::Success);
    dispatcher_.sendTo(ack, sender.address, sender.port);

    // Broadcast to all
    dispatcher_.broadcastConnectedUser(playerId);
}

void ServerNetworkHandler::receiveDisconnect(DisconnectPacket& packet)
{
    auto& sender = packet.getSender();
    DisconnectAckPacket ack(true);
    dispatcher_.sendTo(ack, sender.address, sender.port);
}

void ServerNetworkHandler::receiveLocationState(LocationStatePacket& packet)
{
    auto& sender = packet.getSender();
    auto* conn = players_.getPlayerConnection(sender.address, sender.port);
    if (!conn) return;

    auto* player = conn->getPlayer();
    player->setLocation(static_cast<float>(packet.getX()),
                        static_cast<float>(packet.getY()));
    player->setAngle(static_cast<float>(packet.getAngle()));

    if (state_.isStarted() && players_.count() >= 2) {
        dispatcher_.broadcastLocationState(
            player->getId(), packet.getX(), packet.getY(), packet.getAngle());
    }
}

void ServerNetworkHandler::receiveActionState(ActionStatePacket& packet)
{
    auto& sender = packet.getSender();
    auto* conn = players_.getPlayerConnection(sender.address, sender.port);
    if (!conn) return;

    auto* player = conn->getPlayer();
    player->setCurrentAction(packet.getAction());

    if (state_.isStarted() && players_.count() >= 2) {
        dispatcher_.broadcastActionState(player->getId(), packet.getAction());
    }
}

void ServerNetworkHandler::receiveElementState(ElementStatePacket& packet)
{
    auto& sender = packet.getSender();
    auto* conn = players_.getPlayerConnection(sender.address, sender.port);
    if (!conn) return;

    auto* player = conn->getPlayer();
    player->setCurrentElement(packet.getElement());

    if (state_.isStarted() && players_.count() >= 2) {
        dispatcher_.broadcastElementState(player->getId(), packet.getElement());
    }
}

void ServerNetworkHandler::receiveReadyState(ReadyStatePacket& /*packet*/)
{
    // Ready state tracking for lobby — currently a stub
}

void ServerNetworkHandler::receivePowerUp(PowerUpPacket& /*packet*/)
{
    // Power-up pickup handled server-side via physics — stub
}

void ServerNetworkHandler::receiveInitialGameStateAck(InitialGameStateAckPacket& packet)
{
    auto& sender = packet.getSender();
    auto* conn = players_.getPlayerConnection(sender.address, sender.port);
    if (conn) {
        conn->setHasInitialState();
    }
}

} // namespace neon::net
