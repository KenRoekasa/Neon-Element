package client;

import engine.GameState;
import engine.ai.controller.AiControllersManager;
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
    private Boolean paused;

    public ClientGameState(Player player, Rectangle map, ArrayList<PhysicsObject> objects, LinkedBlockingQueue deadPlayers, ScoreBoard scoreboard , GameType gameType, AiControllersManager aiConMan) {
        super(map, objects, deadPlayers, scoreboard,gameType,aiConMan);
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


    public void resume() {
        paused = false;
    }

    public void pause(){
        paused = true;
    }

    public Boolean getPaused() {
        return paused;
    }
}
