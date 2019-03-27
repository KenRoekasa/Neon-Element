package client;

import engine.model.GameState;
import engine.model.Map;
import engine.model.ScoreBoard;
import engine.ai.controller.AiControllersManager;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.model.GameType;

import java.util.ArrayList;

/**
 * The game state on the client side
 */
public class ClientGameState extends GameState {


    /** The unique player identifier of this client */
    private int clientId;

    private Player player;

    /**
     * The state of paused or not in the current game
     */
    private Boolean paused;



    /**
     * The selected mode
     * */

    private GameType.Type mode;


    /**
     * The state of show leaderboard using tab
     */
    private boolean tab;

    /**
     * Constructor
     *
     * @param player     the player that this client controls
     * @param map        the map of the current match
     * @param objects    all array list of all objects
     * @param scoreboard the scoreboard of the current game
     * @param gameType   the game mode of the current game
     * @param aiConMan   the ai controller manager for the ais in this current game
     */
    public ClientGameState(Player player, Map map, ArrayList<PhysicsObject> objects, ScoreBoard scoreboard, GameType gameType, AiControllersManager aiConMan,GameType.Type mode) {
        super(map, objects, scoreboard, gameType, aiConMan);
        this.player = player;
        this.mode = mode;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    /**
     * Set the mode of the game
     * @param mode mode of the game
     */
    public void setMode(GameType.Type mode) {
        this.mode = mode;
    }

    public GameType.Type getMode() {
        return mode;
    }


    public int getClientId() {
        return this.clientId;
    }

    public void setClientId(int id) {
        this.clientId = id;
    }




    /**
     * Resume the game state so its no longer paused
     */
    public void resume() {
        paused = false;
    }

    /**
     * Pause the game
     */
    public void pause() {
        paused = true;
    }

    public Boolean getPaused() {
        return paused;
    }

    /**
     * get the state of leaderboard
     * @return
     */
    public boolean isTab() {
        return tab;
    }

    /**
     *
     */
    public void setTab(boolean tab) {
        this.tab = tab;
    }



}
