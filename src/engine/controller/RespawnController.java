package engine.controller;

import engine.GameState;
import engine.entities.Player;
import javafx.geometry.Point2D;

import java.util.concurrent.LinkedBlockingQueue;

public class RespawnController implements Runnable {
    GameState gameState;
    LinkedBlockingQueue<Player> deadPlayers;

    public RespawnController(GameState gameState) {
        this.gameState = gameState;
        this.deadPlayers = gameState.getDeadPlayers();
    }

    @Override
    public void run() {
        //TODO: change when game has ended etc...
        while (true) {
            //TODO: If statement based on game type to determine how respawns works based on the game mode
            normalRespawn();
        }

    }

    //Respawn every few seconds
    private void normalRespawn() {
        //Remove the dead player from the list
        try {
            Player player = deadPlayers.take();
            player.respawn();
            Thread.sleep(10000);
            //Adding health to player to resurrect them
            player.setLocation(new Point2D(20,20));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
