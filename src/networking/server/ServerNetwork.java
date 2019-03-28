package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import networking.packets.*;
import server.ServerGameState;
import networking.Constants;

public class ServerNetwork extends networking.AbstractNetwork {

    private ServerNetworkDispatcher dispatcher;
    private ServerNetworkHandler handler;

    private ConnectedPlayers connectedPlayers;

    /**
     * Create a ServerNetwork.
     *
     * @param gameState The server's game state.
     */
    public ServerNetwork(ServerGameState gameState) {
        super(Constants.SERVER_LISTENING_PORT);

        this.connectedPlayers = new ConnectedPlayers();
        this.dispatcher = new ServerNetworkDispatcher(gameState, connectedPlayers, this.getSocket());
        this.handler = new ServerNetworkHandler(gameState, connectedPlayers, this.dispatcher);
    }

    /**
     * Get the ServerNetworkDispatcher.
     *
     * @return The ServerNetworkDispatcher.
     */
    public ServerNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    /**
     * Get the connected players.
     *
     * @return The ConnectedPlayers.
     */
    public ConnectedPlayers getConnectedPlayers() {
        return this.connectedPlayers;
    }

    /**
     * Ends the ServerNetwork and closes the listening port.
     */
    @Override
    public void close() {
        super.close();
        this.dispatcher.close();
    }

    /**
     * {@inheritDoc}
     *
     * After parsing the {@link Packet.PacketToServer#handle(ServerNetworkHandler)} method is called.
     */
    @Override
    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());

        if (packet == null) {
            System.out.println("Invalid packet received");
            return;
        } else if (!(packet instanceof Packet.PacketToServer)) {
            System.out.println(packet.getPacketType() + " received by server which should not be sent to it.");
            return;
        }



        ((Packet.PacketToServer) packet).handle(this.handler);
    }
}
