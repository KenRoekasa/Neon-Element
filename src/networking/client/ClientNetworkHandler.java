package networking.client;

import java.util.ArrayList;

import client.ClientGameState;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.model.enums.ObjectType;
import engine.model.GameType;
import javafx.scene.transform.Rotate;
import networking.packets.*;

public class ClientNetworkHandler {

    private ClientGameState gameState;

    ClientNetworkHandler(ClientGameState gameState) {
        this.gameState = gameState;
    }

    public void receiveHelloAck(HelloAckPacket packet) {
        GameType gameType = packet.getGameType();
        this.gameState.setGameType(gameType);
    }

    public void receiveGameStart(GameStartBroadcast packet) {
        // todo include gamestate in this packet
        this.gameState.start();

    }

    public void receiveConnectAck(ConnectAckPacket packet) {
        switch (packet.getStatus()) {
            case ERR_GAME_STARTED:
            case ERR_MAX_PLAYERS:
            case ERR_UNKOWN:
                System.out.println("Error connecting");
                break;
            case SUC_CONNECTED:
                System.out.println("Successfully connected.  My id: " + packet.getId());
                this.gameState.setClientId(packet.getId());
                break;
        }
    }

    public void receiveConnectBroadcast(ConnectBroadcast packet) {
        Player player = new Player(ObjectType.PLAYER, packet.getId());
        this.gameState.getAllPlayers().add(player);
    }

    public void receivePowerUpBroadcast(PowerUpBroadcast packet) {
        PowerUp powerUp = new PowerUp(packet.getPowerUpId(), packet.getX(), packet.getY(), packet.getPowerUpType());
        this.gameState.getObjects().add(powerUp);
    }

    public void receiveInitialGameStartStateBroadcast(InitialGameStateBroadcast packet) {
        int clientId = this.gameState.getClientId();
        Player clientPlayer = new Player(ObjectType.PLAYER, clientId);
        ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
        ArrayList<Player> tempScoreboardPlayers = new ArrayList<Player>();

        for (int i = 0; i < packet.getIds().size(); i++) {
            if (packet.getIds().get(i) == clientId) {
                clientPlayer.setLocation(packet.getLocations().get(i));
                objects.add(clientPlayer);
                tempScoreboardPlayers.add(clientPlayer);
            } else {
                Player enemy = new Player(ObjectType.ENEMY, packet.getIds().get(i));
                enemy.setLocation(packet.getLocations().get(i));
                objects.add(enemy);
                tempScoreboardPlayers.add(enemy);
            }
        }

        this.gameState.setPlayer(clientPlayer);
        this.gameState.setMap(packet.getMap());
        this.gameState.setObjects(objects);
        this.gameState.getScoreBoard().initialise(tempScoreboardPlayers);
    }

    public void receiveLocationStateBroadcast(LocationStateBroadcast packet) {

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
            player.setPlayerAngle(new Rotate(packet.getPlayerAngle()));
//            System.out.println("before my client health added ");
//            System.out.println(player.getHealth());
          //  player.setHealth(packet.getPlayerHealth());
//            System.out.println("after my client health added ");
//            System.out.println(player.getHealth());
        }
    }

    public void  recieveHealthStateBroadcast(HealthStateBroadcast packet){
        if (packet.getId() != this.gameState.getPlayer().getId()) {
            int id = packet.getId();

            Player foundPlayer = this.gameState.getAllPlayers().stream()
                    .filter(p -> p.getTag().equals(ObjectType.ENEMY))
                    .map(p -> (Player) p)
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (foundPlayer != null) {
                foundPlayer.setHealth(packet.getPlayerHealth());
            } else {
                System.out.println("Player does not exists");
            }
        }else{
            this.gameState.getPlayer().setHealth(packet.getPlayerHealth());
        }


}
	public void receivePlayerActionBroadCast(ActionStateBroadcast packet) {
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
	                foundPlayer.doAction(packet.getPlayerActionState());
	            } else {
	            		System.out.println("Player does not exists");
	            }
	        }
	}

	public void receiveElementBroadcast(ElementStateBroadcast packet) {
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
                foundPlayer.setCurrentElement(packet.getPlayerElementState());
            }else {
            		System.out.println("Player does not exists");
            }
        }
    }

    public void receiveGameEnd() {
        this.gameState.stop();
    }

    public void recieveScoreBroadcast(ScoreBroadcast packet) {

        gameState.getScoreBoard().addScore(packet.getId(), packet.getPlayerScore());
    }
}