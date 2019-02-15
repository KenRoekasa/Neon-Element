package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import client.ClientGameState;
import entities.Player;
import entities.PowerUp;
import networking.packets.*;
import networking.Constants;
import networking.NetworkDispatcher;

public class ClientNetworkDispatcher extends NetworkDispatcher {

    private ClientGameState gameState;
    
	protected String serverAddress;

    protected ClientNetworkDispatcher(DatagramSocket socket, MulticastSocket multicastSocket, InetAddress groupAddress, ClientGameState gameState) {
        super(socket, multicastSocket, groupAddress);
        this.gameState = gameState;
    }

    public void sendHello() {
        try {
            Packet packet = new HelloPacket(InetAddress.getByName(Constants.SERVER_ADDRESS), Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void receiveHelloAck(HelloAckPacket packet) {
        int players = packet.getPlayers();
        int maxPlayers = packet.getMaxPlayers();
        System.out.println("Got players: " + players + " max: " + maxPlayers);
    }
    
    protected void receivePowerUpBroadcast(BroadCastPowerUpPacket packet) {
        PowerUp powerUp = new PowerUp(packet.getPowerUpId(), packet.getX(), packet.getY());
        this.gameState.getEntities().add(powerUp);
    }

    protected void receiveLocationStateBroadcast(BroadCastLocationStatePacket packet) {
        // Only update locations of other players
        if (packet.getId() != this.gameState.getPlayer().getId()) {
            int id = packet.getId();
            double x = packet.getX();
            double y = packet.getY();
            
            Player player = this.gameState.getPlayers().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

            if (player != null) {
                // Just update the location for now
                // TODO - validate if the location
                player.setLocation(packet.getX(), packet.getY());
            } else {
                // Player id not found
            }
        }
    }

    public void sendLocationState(double x, double y) {
        try {
            Packet packet = new LocationStatePacket(x, y, InetAddress.getByName(Constants.SERVER_ADDRESS), Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
