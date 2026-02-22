// Implementations of handle() for all packet types.
// These dispatch to the appropriate handler methods.

#include "HelloPackets.hpp"
#include "ConnectPackets.hpp"
#include "DisconnectPackets.hpp"
#include "StatePackets.hpp"
#include "BroadcastPackets.hpp"
#include "GamePackets.hpp"
#include "networking/client/ClientNetworkHandler.hpp"
#include "networking/server/ServerNetworkHandler.hpp"

namespace neon::net {

// === Client → Server (PacketToServer) ===

void HelloPacket::handle(ServerNetworkHandler& h)       { h.receiveHello(*this); }
void ConnectPacket::handle(ServerNetworkHandler& h)     { h.receiveConnect(*this); }
void DisconnectPacket::handle(ServerNetworkHandler& h)  { h.receiveDisconnect(*this); }
void LocationStatePacket::handle(ServerNetworkHandler& h) { h.receiveLocationState(*this); }
void ActionStatePacket::handle(ServerNetworkHandler& h)   { h.receiveActionState(*this); }
void ElementStatePacket::handle(ServerNetworkHandler& h)  { h.receiveElementState(*this); }
void ReadyStatePacket::handle(ServerNetworkHandler& h)    { h.receiveReadyState(*this); }
void PowerUpPacket::handle(ServerNetworkHandler& h)       { h.receivePowerUp(*this); }
void InitialGameStateAckPacket::handle(ServerNetworkHandler& h) { h.receiveInitialGameStateAck(*this); }

// === Server → Client (PacketToClient) ===

void HelloAckPacket::handle(ClientNetworkHandler& h)    { h.receiveHelloAck(*this); }
void ConnectAckPacket::handle(ClientNetworkHandler& h)  { h.receiveConnectAck(*this); }
void ConnectBroadcast::handle(ClientNetworkHandler& h)  { h.receiveConnectBroadcast(*this); }
void DisconnectAckPacket::handle(ClientNetworkHandler& h)  { h.receiveDisconnectAck(*this); }
void DisconnectBroadcast::handle(ClientNetworkHandler& h)  { h.receiveDisconnectBroadcast(*this); }
void ReadyStateBroadcast::handle(ClientNetworkHandler& h)  { h.receiveReadyStateBroadcast(*this); }
void LocationStateBroadcast::handle(ClientNetworkHandler& h)  { h.receiveLocationStateBroadcast(*this); }
void ElementStateBroadcast::handle(ClientNetworkHandler& h)   { h.receiveElementStateBroadcast(*this); }
void HealthStateBroadcast::handle(ClientNetworkHandler& h)    { h.receiveHealthStateBroadcast(*this); }
void PowerUpPickUpBroadcast::handle(ClientNetworkHandler& h)  { h.receivePowerUpPickUpBroadcast(*this); }
void PowerUpBroadcast::handle(ClientNetworkHandler& h)        { h.receivePowerUpBroadcast(*this); }
void InitialGameStateBroadcast::handle(ClientNetworkHandler& h) { h.receiveInitialGameStateBroadcast(*this); }
void ActionStateBroadcast::handle(ClientNetworkHandler& h)    { h.receiveActionStateBroadcast(*this); }
void RespawnBroadcast::handle(ClientNetworkHandler& h)        { h.receiveRespawnBroadcast(*this); }
void ScoreBroadcast::handle(ClientNetworkHandler& h)          { h.receiveScoreBroadcast(*this); }
void GameStartBroadcast::handle(ClientNetworkHandler& h)      { h.receiveGameStartBroadcast(*this); }
void GameOverBroadcast::handle(ClientNetworkHandler& h)       { h.receiveGameOverBroadcast(*this); }

} // namespace neon::net
