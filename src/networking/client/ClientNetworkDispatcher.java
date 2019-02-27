package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;

import client.ClientGameState;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import engine.gameTypes.GameType;
import networking.packets.*;
import networking.Constants;
import networking.NetworkDispatcher;

public class ClientNetworkDispatcher extends NetworkDispatcher {

    private InetAddress serverAddr;
    private ClientGameState gameState;
    
	protected String serverAddress;

    protected ClientNetworkDispatcher(DatagramSocket socket, InetAddress serverAddr, /*MulticastSocket multicastSocket, InetAddress groupAddress,*/ ClientGameState gameState) {
        super(socket/*, multicastSocket, groupAddress*/);
        this.serverAddr = serverAddr;
        this.gameState = gameState;
    }

    public void sendHello() {
        try {
            Packet packet = new HelloPacket(serverAddr, Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected void receiveHelloAck(HelloAckPacket packet) {
        int players = packet.getPlayers();
        int maxPlayers = packet.getMaxPlayers();
        GameType gameType = packet.getGameType();
        this.gameState.setGameType(gameType);
    }

    public void sendConnect() {
        try {
            Packet packet = new ConnectPacket(serverAddr, Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void receiveGameStart(BroadCastGameStartPacket packet) {
        // todo include gamestate in this packet
        this.gameState.start();


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
        Player player = new Player(ObjectType.PLAYER, packet.getId());
        //todo this is probably broken
        this.gameState.getAllPlayers().add(player);
        this.gameState.getObjects().add(player);
    }

    protected void receivePowerUpBroadcast(BroadCastPowerUpPacket packet) {
        PowerUp powerUp = new PowerUp(packet.getPowerUpId(), packet.getX(), packet.getY());
        this.gameState.getObjects().add(powerUp);
    }

    protected void receiveLocationStateBroadcast(BroadCastLocationStatePacket packet) {
        // Only update locations of other players
        if (packet.getId() != this.gameState.getPlayer().getId()) {
            int id = packet.getId();

            Player foundPlayer = this.gameState.getObjects().stream()
                .filter(p -> p.getTag().equals(ObjectType.PLAYER))
                .map(p -> (Player) p)
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

            Player player;
            if (foundPlayer != null) {
                player = foundPlayer;
            } else {
                // Player id not found
                player = new Player(ObjectType.PLAYER, id);
                this.gameState.getAllPlayers().add(player);
                this.gameState.getObjects().add(player);
            }
            player.setLocation(packet.getX(), packet.getY());
        }
    }


    public void sendLocationState(double x, double y) {
        try {
            Packet packet = new LocationStatePacket(x, y, serverAddr, Constants.SERVER_LISTENING_PORT);
            this.send(packet);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
