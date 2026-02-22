#pragma once

#include "networking/NetworkDispatcher.hpp"
#include "networking/Constants.hpp"
#include "networking/packets/HelloPackets.hpp"
#include "networking/packets/ConnectPackets.hpp"
#include "networking/packets/DisconnectPackets.hpp"
#include "networking/packets/StatePackets.hpp"
#include "engine/model/enums/Action.hpp"
#include "engine/model/enums/Elements.hpp"
#include <SFML/Network.hpp>

namespace neon::net {

class ClientNetworkDispatcher : public NetworkDispatcher {
public:
    ClientNetworkDispatcher(sf::UdpSocket& socket, const sf::IpAddress& serverAddr)
        : NetworkDispatcher(socket)
        , serverAddr_(serverAddr)
        , serverPort_(SERVER_LISTENING_PORT) {}

    void sendHello()
    {
        HelloPacket pkt;
        send(pkt, serverAddr_, serverPort_);
    }

    void sendConnect()
    {
        ConnectPacket pkt;
        send(pkt, serverAddr_, serverPort_);
    }

    void sendDisconnect()
    {
        DisconnectPacket pkt;
        send(pkt, serverAddr_, serverPort_);
    }

    void sendLocationState(double x, double y, double angle)
    {
        LocationStatePacket pkt(x, y, angle);
        send(pkt, serverAddr_, serverPort_);
    }

    void sendActionState(Action action)
    {
        ActionStatePacket pkt(action);
        send(pkt, serverAddr_, serverPort_);
    }

    void sendElementState(Element element)
    {
        ElementStatePacket pkt(element);
        send(pkt, serverAddr_, serverPort_);
    }

    void sendReadyState(bool ready)
    {
        ReadyStatePacket pkt(ready);
        send(pkt, serverAddr_, serverPort_);
    }

    void sendPowerUp(int pickupId)
    {
        PowerUpPacket pkt(pickupId);
        send(pkt, serverAddr_, serverPort_);
    }

    void sendInitialGameStateAck()
    {
        InitialGameStateAckPacket pkt;
        send(pkt, serverAddr_, serverPort_);
    }

private:
    sf::IpAddress serverAddr_;
    unsigned short serverPort_;
};

} // namespace neon::net
