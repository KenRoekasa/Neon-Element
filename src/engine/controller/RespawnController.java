package engine.controller;

import engine.GameState;
import engine.entities.Player;
import engine.gameTypes.GameType;
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
        while (gameState.getRunning()) {
            //TODO: If statement based on game type to determine how respawns works based on the game mode
            if(gameState.getGameType().getType() == GameType.Type.FirstToXKills) {
                normalRespawn(5000);
            }else if(gameState.getGameType().getType() == GameType.Type.Timed){
                normalRespawn(2500);
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

                int x = (int) (Math.random() * 2000);
                int y = (int) (Math.random() * 2000);

                player.setLocation(new Point2D(x, y));
                player.respawn();
                // SO you don't respawn twice
                deadPlayers.take();
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
