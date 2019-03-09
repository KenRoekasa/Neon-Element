package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import networking.packets.*;
import server.ServerGameState;
import networking.Constants;

public class ServerNetwork extends Thread {
    /** True if the ServerNetwork is running and receiving Packets */
    protected boolean running;

    protected DatagramSocket socket;

    protected ServerNetworkDispatcher dispatcher;
    private ServerNetworkHandler handler;

    private ConnectedPlayers connectedPlayers;

    /**
     * Create a ServerNetwork.
     *
     * @param gameState The server's game state.
     */
    public ServerNetwork(ServerGameState gameState) {
        try {
            socket = new DatagramSocket(Constants.SERVER_LISTENING_PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.connectedPlayers = new ConnectedPlayers();

        this.dispatcher = new ServerNetworkDispatcher(gameState, connectedPlayers, this.socket);
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
    public void close() {
        this.running = false;
        this.dispatcher.close();
    }

    /**
     * Start the ServerNetwork and begin receiving packets.
     */
    public void run() {
        this.running = true;
        while (this.running) {
            byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];

            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                this.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.parse(packet);
        }
    }

    /**
     * Parse the incoming DatagramPacket into a {@link Packet} and handle it.
     *
     * After parsing the {@link Packet.PacketToServer#handle(ServerNetworkHandler) method is called.
     *
     * @param datagram The incoming packet.
     */
    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());

        if (packet == null) {
            System.out.println("Invalid packet received");
            return;
        } else if (!(packet instanceof Packet.PacketToServer)) {
            System.out.println(packet.getPacketType() + " received by server which should not be sent to it.");
            return;
        }

        if (!packet.getPacketType().equals(Packet.PacketType.LOCATION_STATE)) {
            System.out.println("Got " + packet.getPacketType() + " from " + packet.getIpAddress() + ":" + packet.getPort());
        }

        ((Packet.PacketToServer) packet).handle(this.handler);
    }
}
