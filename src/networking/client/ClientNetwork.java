package networking.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import client.ClientGameState;
import networking.packets.*;

public class ClientNetwork {
    protected DatagramSocket socket;

    private ClientNetworkConnection conn;
    private ClientNetworkDispatcher dispatcher;
    private ClientNetworkHandler handler;

    /**
     * Create a ClientNetwork.
     *
     * @param gameState The client's game state.
     * @param serverAddr The server's remote IP address.
     */
    public ClientNetwork(ClientGameState gameState, InetAddress serverAddr) {
        this.conn = new ClientNetworkConnection(this);
        this.socket = conn.getSocket();

        this.dispatcher = new ClientNetworkDispatcher(gameState, this.socket, serverAddr);
        this.handler = new ClientNetworkHandler(gameState);

        this.conn.start();
    }

    /**
     * Get the ClientNetworkDispatcher.
     *
     * @return The ClientNetworkDispatcher.
     */
    public ClientNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void close() {
        this.conn.close();
        this.dispatcher.close();
    }

    /**
     * Parse the incoming DatagramPacket into a {@link Packet} and handle it.
     *
     * After parsing the {@link Packet.PacketToClient#handle(ClientNetworkHandler) method is called.
     *
     * @param datagram The incoming packet.
     */
    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());

        if (packet == null) {
            System.out.println("Invalid packet received");
            return;
        } else if (!(packet instanceof Packet.PacketToClient)) {
            System.out.println(packet.getPacketType() + " received by client which should not be sent to it.");
            return;
        }

        if (!packet.getPacketType().equals(Packet.PacketType.LOCATION_STATE_BCAST)) {
            if (packet.getIpAddress() != null) {
                System.out.println("Got " + packet.getPacketType() + " from " + packet.getIpAddress() + ":" + packet.getPort());
            } else {
                System.out.println("Got " + packet.getPacketType());
            }
        }

        ((Packet.PacketToClient) packet).handle(this.handler);
    }

}
