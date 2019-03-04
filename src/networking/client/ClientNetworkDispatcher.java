package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import client.ClientGameState;
import engine.ScoreBoard;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.Elements;
import engine.enums.ObjectType;
import engine.gameTypes.GameType;
import networking.packets.*;
import networking.Constants;
import networking.NetworkDispatcher;

public class ClientNetworkDispatcher extends NetworkDispatcher {

    private InetAddress serverAddr;
    private ClientGameState gameState;
    private InetAddress clientIpAddress;
    protected String serverAddress;
    private int clientID;

    protected ClientNetworkDispatcher(DatagramSocket socket, InetAddress serverAddr,
            /* MulticastSocket multicastSocket, InetAddress groupAddress, */ ClientGameState gameState) {
        super(socket/* , multicastSocket, groupAddress */);
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

    protected void receiveHelloAck(HelloAckPacket packet) {
        int players = packet.getPlayers();
        int maxPlayers = packet.getMaxPlayers();
        GameType gameType = packet.getGameType();
        this.gameState.setGameType(gameType);
    }

    public void sendConnect() {
        try {
            Packet packet = new ConnectPacket();
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
        PowerUp powerUp = new PowerUp(packet.getPowerUpId(), packet.getX(), packet.getY(), packet.getPowerUpType());
        this.gameState.getObjects().add(powerUp);
    }

    protected void receiveInitialGameStartStateBroadcast(BroadCastinitialGameStatePacket packet) {
        Player clientPlayer = new Player(ObjectType.PLAYER);
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        LinkedBlockingQueue<Player> deadPlayers = new LinkedBlockingQueue<Player>();
        ScoreBoard scoreboard = new ScoreBoard();
        ArrayList<Player> tempScoreboardPlayers = new ArrayList<Player>();

        for (int i = 0; i < packet.getIds().size(); i++) {
            if (packet.getIds().get(i) == clientID) {
                clientPlayer.setLocation(packet.getLocations().get(i));
                clientPlayer.setId(packet.getIds().get(i));
                objects.add(clientPlayer);
                tempScoreboardPlayers.add(clientPlayer);
            } else {
                Player enemy = new Player(ObjectType.ENEMY);
                enemy.setId(packet.getIds().get(i));
                enemy.setLocation(packet.getLocations().get(i));
                objects.add(enemy);
                tempScoreboardPlayers.add(enemy);

            }

        }
        scoreboard.initialise(tempScoreboardPlayers);

        this.gameState = new ClientGameState(clientPlayer, packet.getMap(), objects, deadPlayers,
                scoreboard, packet.getGameType());

    }

    protected void receiveLocationStateBroadcast(BroadCastLocationStatePacket packet) {
        // Only update locations of other players
        if (packet.getId() != this.gameState.getPlayer().getId()) {
            int id = packet.getId();

            Player foundPlayer = this.gameState.getAllPlayers().stream()
            .filter(p -> p.getTag().equals(ObjectType.ENEMY))
            .map(p -> (Player) p)
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);

            Player player;
            if (foundPlayer != null) {
                player = foundPlayer;
            } else {
                // Player id not found
                player = new Player(ObjectType.ENEMY, id);
                this.gameState.getAllPlayers().add(player);
                this.gameState.getObjects().add(player);
            }
            player.setLocation(packet.getX(), packet.getY());
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

	public void recievePlayerActionBroadCast(BroadcastActionPacket packet) {
		// TODO Auto-generated method stub
		 if (packet.getId() != this.gameState.getPlayer().getId()) {
	            int id = packet.getId();

	            Player foundPlayer = this.gameState.getAllPlayers().stream()
	            .filter(p -> p.getTag().equals(ObjectType.ENEMY))
	            .map(p -> (Player) p)
	            .filter(p -> p.getId() == id)
	            .findFirst()
	            .orElse(null);

	            Player player;
	            if (foundPlayer != null) {
	                player = foundPlayer;
	                player.doAction(packet.getPlayerActionState());
	            } else {
	            		System.out.println("Player does not exists");
	            }
	        }
	}

	public void recieveElementBroadcast(BroadCastElementStatePacket packet) {
		// TODO Auto-generated method stub
		if (packet.getId()!= this.gameState.getPlayer().getId()) {
            int id = packet.getId();

            Player foundPlayer = this.gameState.getAllPlayers().stream()
            .filter(p -> p.getTag().equals(ObjectType.ENEMY))
            .map(p -> (Player) p)
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);

            Player player;
            if (foundPlayer != null) {
                player = foundPlayer;
                player.setCurrentElement(packet.getPlayerElementState());
            }else {
            		System.out.println("Player does not exists");
            }
        }
    }

    private void send(Packet packet) {
        super.send(packet, serverAddr, Constants.SERVER_LISTENING_PORT);
    }

}
