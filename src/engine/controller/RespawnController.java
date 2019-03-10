package engine.controller;

import engine.GameState;
import engine.entities.Player;
import engine.gameTypes.GameType;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Random;
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
        while (gameState.getRunning()) {
            if(gameState.getGameType().getType() == GameType.Type.FirstToXKills) {
                normalRespawn(5000);
            }else if(gameState.getGameType().getType() == GameType.Type.Timed){
                normalRespawn(2500);
            }else if(gameState.getGameType().getType() == GameType.Type.Hill){
                normalRespawn(2500);
            }else if(gameState.getGameType().getType() == GameType.Type.Regicide){
                normalRespawn(1000);
            }
        }

    }

    //Respawn every few seconds
    private void normalRespawn(long respawnTime) {
        //Remove the dead player from the list
        try {
            if (!deadPlayers.isEmpty()) {
                Player player = deadPlayers.peek();
                Thread.sleep(respawnTime);
                //Adding health to player to resurrect them
                Random rand = new Random();
                ArrayList<Point2D> respawnPoints = gameState.getMap().getRespawnPoints();
                int index = rand.nextInt(3);
                player.setLocation(respawnPoints.get(index));
                player.respawn();
                // SO you don't respawn twice
                deadPlayers.take();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
