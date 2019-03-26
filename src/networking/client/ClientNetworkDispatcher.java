package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;

import client.ClientGameState;
import engine.model.enums.*;
import networking.packets.*;
import networking.Constants;
import networking.AbstractNetworkDispatcher;

public class ClientNetworkDispatcher extends AbstractNetworkDispatcher {

    private InetAddress serverAddr;
    private ClientGameState gameState;

    protected ClientNetworkDispatcher(ClientGameState gameState, DatagramSocket socket, InetAddress serverAddr) {
        super(socket);
        this.serverAddr = serverAddr;
        this.gameState = gameState;
    }

    public void sendHello() {
        try {
            Packet packet = new HelloPacket();
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendConnect() {
        try {
            Packet packet = new ConnectPacket();
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendInitialGameStateAck() {
        Packet packet = new InitialGameStateAckPacket();
        this.send(packet);
    }

    public void sendLocationState(double x, double y, double playerAngle, float playerHealth) {
        try {
            Packet packet = new LocationStatePacket(x, y, playerAngle, playerHealth);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendActionState(Action action) {
        try {
            Packet packet = new ActionStatePacket(action);
            this.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendElementState(Elements element) {
        try {
            Packet packet = new ElementStatePacket(element);
            this.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a Packet to the server.
     *
     * @param packet The packet to send.
     */
    private void send(Packet packet) {
        super.send(packet, serverAddr, Constants.SERVER_LISTENING_PORT);
    }

}
