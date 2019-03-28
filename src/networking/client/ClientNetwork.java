package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import client.ClientGameState;
import networking.AbstractNetwork;
import networking.packets.*;

public class ClientNetwork extends AbstractNetwork {

    private ClientNetworkDispatcher dispatcher;
    private ClientNetworkHandler handler;

    /**
     * Create a ClientNetwork.
     *
     * @param gameState The client's game state.
     * @param serverAddr The server's remote IP address.
     */
    public ClientNetwork(ClientGameState gameState, InetAddress serverAddr) {
        super();

        this.dispatcher = new ClientNetworkDispatcher(gameState, this.getSocket(), serverAddr);
        this.handler = new ClientNetworkHandler(gameState, this.dispatcher);

        this.start();
    }

    /**
     * Get the ClientNetworkDispatcher.
     *
     * @return The ClientNetworkDispatcher.
     */
    public ClientNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    /**
     * Ends the ClientNetwork and closes the listening port.
     */
    @Override
    public void close() {
        super.close();
        this.dispatcher.close();
    }

    /**
     * After parsing the {@link Packet.PacketToClient#handle(ClientNetworkHandler)} method is called.
     */
    @Override
    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());

        if (packet == null) {
            System.out.println("Invalid packet received");
            return;
        } else if (!(packet instanceof Packet.PacketToClient)) {
            System.out.println(packet.getPacketType() + " received by client which should not be sent to it.");
            return;
        }


        ((Packet.PacketToClient) packet).handle(this.handler);
    }

}
