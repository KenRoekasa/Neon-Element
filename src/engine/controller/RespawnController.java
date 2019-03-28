package engine.controller;


import client.GameClient;
import engine.entities.Player;
import engine.model.GameState;
import engine.model.GameType;
import javafx.geometry.Point2D;
import networking.server.ServerNetworkDispatcher;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Controls how the players in the game respawns
 */
public class RespawnController {
    private GameState gameState;
    private ServerNetworkDispatcher dispatcher;

    /**
     * A queue of the dead players in the current game
     */
    private LinkedBlockingQueue<Player> deadPlayers;
    private long lastTime;

    /**
     * Constructor
     *
     * @param gameState the game state of the current game
     */
    public RespawnController(GameState gameState) {
        this.gameState = gameState;
        this.deadPlayers = gameState.getDeadPlayers();
    }

    public RespawnController(GameState gameState, ServerNetworkDispatcher dispatcher) {
        this(gameState);
        this.dispatcher = dispatcher;
    }

    public void update() {
        if (gameState.getGameType().getType() == GameType.Type.FirstToXKills) {
            normalRespawn(5000);
        } else if (gameState.getGameType().getType() == GameType.Type.Timed) {
            normalRespawn(2500);
        } else if (gameState.getGameType().getType() == GameType.Type.Hill) {
            normalRespawn(2500);
        } else if (gameState.getGameType().getType() == GameType.Type.Regicide) {
            normalRespawn(1000);
        }
    }

    /**
     * Causes dead players to respawn after a certain amount of time
     *
     * @param respawnTime the duration a character is dead for before respawning in milli
     */
    private void normalRespawn(long respawnTime) {
        //Remove the dead player from the list
        try {
            if (!deadPlayers.isEmpty()) {
                Player player = deadPlayers.peek();
                long currentTime = GameClient.timeElapsed;
                long playerDeathTime = player.getDeathTime();

                if (currentTime - playerDeathTime >= respawnTime) {
                    Random rand = new Random();
                    ArrayList<Point2D> respawnPoints = gameState.getMap().getRespawnPoints();
                    int index = rand.nextInt(3);
                    Point2D respawnPoint = respawnPoints.get(index);
                    player.setLocation(respawnPoint);
                    if (this.dispatcher != null) {
                        this.dispatcher.broadcastRespawn(player.getId(), respawnPoint.getX(), respawnPoint.getY());
                    }
                    player.respawn();
                    // SO you don't respawn twice
                    deadPlayers.take();
                    System.out.println("spawn");

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
