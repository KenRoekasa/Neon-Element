package client;

import engine.GameState;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.ScoreBoard;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientGameState extends GameState {


    private Player player;

    public ClientGameState(Player player, Rectangle map, ArrayList<PhysicsObject> objects, LinkedBlockingQueue deadPlayers, ScoreBoard scoreboard) {
        super(map, objects, deadPlayers, scoreboard);
        this.player = player;
    }

    public Rectangle getMap() {
        return map;
    }

    public void setMap(Rectangle map) {
        this.map = map;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }


}
