package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import networking.packets.*;
import server.ServerGameState;
import networking.NetworkDispatcher;

public class ServerNetworkDispatcher extends NetworkDispatcher {
    
    private ServerGameState gameState;

    private ArrayList<PlayerConnection> connections;

	protected ServerNetworkDispatcher(DatagramSocket socket, /*MulticastSocket multicastSocket, InetAddress groupAddress,*/ ServerGameState gameState) {
		super(socket/*, multicastSocket, groupAddress*/);
		this.gameState = gameState;
        this.connections = new ArrayList<>();
	}

	protected void receiveHello(HelloPacket packet) {
		// TODO - integrate and get these values from somewhere
//		int players = this.gameState.getAllPlayers().size();
//		int maxPlayers = this.gameState.getMaxPlayers();
//
//		Packet response = new HelloAckPacket(players, maxPlayers, packet.getIpAddress(), packet.getPort());
//		System.out.println("respond");
//		this.send(response);
	}

	protected void receiveConnect(ConnectPacket packet) {
	    boolean isStarted = this.gameState.isStarted();
	    boolean hasSpace = this.gameState.getAllPlayers().size() < this.gameState.getMaxPlayers();
	    System.out.println("Does the game have space: "+hasSpace);
	    System.out.println("has the game Started: "+isStarted);

	    // Allow connection if the game has not started yet and we have space for more players
	    ConnectAckPacket.Status status;
	    int playerId = 0;
	    if (isStarted) {
	        status = ConnectAckPacket.Status.ERR_GAME_STARTED;
	    } else if(!hasSpace) {
            status = ConnectAckPacket.Status.ERR_MAX_PLAYERS;
	    } else {
	        status = ConnectAckPacket.Status.SUC_CONNECTED;

	        Player player = new Player(ObjectType.PLAYER);
	        playerId = player.getId();
            PlayerConnection playerConn = new PlayerConnection(player, packet.getIpAddress(), packet.getPort());

            this.connections.add(playerConn);
            this.gameState.getAllPlayers().add(player);
            this.gameState.getObjects().add(player);

            System.out.println("New player connection. P: " + playerId + " from: " + packet.getIpAddress());

	    }

        Packet response = new ConnectAckPacket(playerId, status, packet.getIpAddress(), packet.getPort());
        this.send(response);
        
        if (status == ConnectAckPacket.Status.SUC_CONNECTED) {
            Packet connect = new BroadCastConnectedUserPacket(playerId);
            this.broadcast(connect);

            if(this.connections.size() == 2) {
                this.gameState.setStarted(true);
                this.broadcastGameStarted();
            }
        }
	}
	
	protected void broadcastGameStarted() {
	    Packet packet = new BroadCastGameStartPacket(true, this.gameState.getAllPlayers().size());
	    this.broadcast(packet);
	}

	protected void receiveLocationState(LocationStatePacket packet) {
	    PlayerConnection playerConn = getPlayerConnection(packet);

	    if (playerConn != null) {
	        Player player = playerConn.getPlayer();

            // Just update the location for now
            // TODO - validate if the location
            player.setLocation(packet.getX(), packet.getY());
	    } else {
	        // Player connection not found
	    }
	}

	protected void broadCastNewConnectedUser() {
		//Packet response = new BroadCastConnectedUserPacket(new Buffer());
	//	this.send(response);
	}
    
    public void broadcastNewPowerUp(PowerUp powerUp) {
        double x = powerUp.getLocation().getX();
        double y = powerUp.getLocation().getY();
        Packet packet = new BroadCastPowerUpPacket(powerUp.getId(), x, y);
        this.broadcast(packet);
    }
    
    public void broadcastLocationState(int playerId, double x, double y) {
        Packet packet = new BroadCastLocationStatePacket(playerId, x, y);
        this.broadcast(packet);
    }

	protected void receiveSpellCast(CastSpellPacket packet) {
		// Packet response = new SpellCas
	}

	public void broadCastDisconnectedUser(DisconnectAckPacket packet) {
		// TODO Auto-generated method stub
		Packet response = new DisconnectAckPacket(packet.getIpAddress(), packet.getPort(), true);
		this.send(response);
	}
	
	private PlayerConnection getPlayerConnection(Packet packet) {
        return this.connections.stream()
            .filter(c -> c.is(packet.getIpAddress(), packet.getPort()))
            .findFirst()
            .orElse(null);
	}
	
    private void broadcast(Packet packet) {
        if (packet.getDirection() == Packet.PacketDirection.OUTGOING) {
            byte[] data = packet.getRawBytes();
           
            for (PlayerConnection conn : this.connections) {
                DatagramPacket datagram = new DatagramPacket(data, data.length, conn.getIpAddress(), conn.getPort());
                
                if (!packet.getType().equals(Packet.PacketType.LOCATION_STATE_BCAST)) {
                		
                    System.out.println("sent " +packet.getType() +" Packet ==> "+" Player with id: "+conn.getId());
            
                }

                try {
                    this.socket.send(datagram);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Attempted to send a recived packet.");
        }
    }
}
