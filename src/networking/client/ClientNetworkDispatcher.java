package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;

import client.ClientGameState;
import engine.enums.Action;
import engine.enums.Elements;
import networking.packets.*;
import networking.Constants;
import networking.NetworkDispatcher;

public class ClientNetworkDispatcher extends NetworkDispatcher {

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


    public void sendLocationState(double x, double y) {
        try {
            Packet packet = new LocationStatePacket(x, y);
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

    private void send(Packet packet) {
        super.send(packet, serverAddr, Constants.SERVER_LISTENING_PORT);
    }

}
