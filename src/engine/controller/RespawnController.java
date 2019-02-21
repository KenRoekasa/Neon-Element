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
            System.out.println("running" +
                    "");
        }

    }

    //Respawn every few seconds
    private void normalRespawn() {
        //Remove the dead player from the list
        try {
            Player player = deadPlayers.take();
            Thread.sleep(5000);
            //Adding health to player to resurrect them

            int x = (int) (Math.random() * 2000);
            int y = (int) (Math.random() * 2000);

            player.setLocation(new Point2D(x,y));
            player.respawn();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
