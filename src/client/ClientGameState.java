package client;

import engine.GameState;
import engine.Map;
import engine.ai.controller.AiControllersManager;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.ScoreBoard;
import engine.gameTypes.GameType;


import java.util.ArrayList;

public class ClientGameState extends GameState {


    private Player player;
    private int num_player;
    private Boolean paused;

    public ClientGameState(Player player, Map map, ArrayList<PhysicsObject> objects, ScoreBoard scoreboard, GameType gameType, AiControllersManager aiConMan) {
        super(map, objects, scoreboard,gameType,aiConMan);
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
