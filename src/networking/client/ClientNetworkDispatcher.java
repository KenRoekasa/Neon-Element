package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import client.ClientGameState;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import networking.packets.*;
import networking.Constants;
import networking.NetworkDispatcher;

public class ClientNetworkDispatcher extends NetworkDispatcher {

    private ClientGameState gameState;
    
	protected String serverAddress;

    protected ClientNetworkDispatcher(DatagramSocket socket, /*MulticastSocket multicastSocket, InetAddress groupAddress,*/ ClientGameState gameState) {
        super(socket/*, multicastSocket, groupAddress*/);
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

    public void sendConnect() {
        try {
            Packet packet = new ConnectPacket(InetAddress.getByName(Constants.SERVER_ADDRESS), Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void receiveConnectAck(ConnectAckPacket packet) {
        switch (packet.getStatus()) {
            case ERR_GAME_STARTED:
            case ERR_MAX_PLAYERS:
                System.out.println("Error connecting");
                break;
            case SUC_CONNECTED:
                System.out.println("Successfully connected.  My id: " + packet.getId());
                this.gameState.getPlayer().setId(packet.getId());
                break;
        }
    }
    
    protected void receiveConnectedUserBroadcast(BroadCastConnectedUserPacket packet) {
        Player player = new Player(ObjectType.PLAYER);
        player.setId(packet.getId());
        this.gameState.getEnemies().add((Character) player);
        this.gameState.getObjects().add((Character) player);
    }
    
    protected void receivePowerUpBroadcast(BroadCastPowerUpPacket packet) {
        PowerUp powerUp = new PowerUp(packet.getPowerUpId(), packet.getX(), packet.getY());
        this.gameState.getObjects().add(powerUp);
    }

    protected void receiveLocationStateBroadcast(BroadCastLocationStatePacket packet) {
        // Only update locations of other players
        if (packet.getId() != this.gameState.getPlayer().getId()) {
            int id = packet.getId();
            
            Player player = this.gameState.getObjects().stream()
                .filter(p -> p.getTag().equals(ObjectType.PLAYER))
                .map(p -> (Player) p)
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

            if (player != null) {
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
