package client;

import engine.GameState;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.ScoreBoard;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientGameState extends GameState {


    private Player player;
    private int num_player;

    public ClientGameState(Player player, Rectangle map, ArrayList<PhysicsObject> objects, LinkedBlockingQueue deadPlayers, ScoreBoard scoreboard , GameType gameType) {
        super(map, objects, deadPlayers, scoreboard,gameType);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public int getNum_player() {
        return num_player;
    }

    public void setNum_player(int num_player) {
        this.num_player = num_player;
    }


}
