package engine.model;

import engine.ai.controller.AiControllersManager;
import engine.entities.PhysicsObject;
import engine.entities.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The current state of the game including the entities/objects/maps in the game
 */
public abstract class GameState {
    /**
     * The map of this current game
     */
    protected Map map;
    /**
     * All PhysicsController objects
     */
    protected ArrayList<PhysicsObject> objects;
    /**
     * The game mode of this match
     */
    protected GameType gameType;
    /**
     * The time when the game starts
     */
    protected long startTime;
    /**
     * A list of all players in the game
     */
    protected ArrayList<Player> allPlayers = new ArrayList<>();
    /**
     * Is the game on going or has it ended
     */
    private boolean isRunning;
    /**
     * The ScoreBoard
     */
    private ScoreBoard scoreBoard;
    /**
     * A queue of players that are dead
     */
    private LinkedBlockingQueue deadPlayers = new LinkedBlockingQueue();
    /**
     * The Ai controller manager in this game
     */
    private AiControllersManager aiConMan;

    /**
     * Constructor
     *
     * @param map        the map
     * @param objects    all the physics object in the game
     * @param scoreboard the scoreboard
     * @param gameType   the game mode
     * @param aiConMan   the ai controller manager
     **/
    public GameState(Map map, ArrayList<PhysicsObject> objects, ScoreBoard scoreboard, GameType gameType, AiControllersManager aiConMan) {
        this.objects = objects;
        this.gameType = gameType;
        this.aiConMan = aiConMan;
        for (PhysicsObject o : objects) {
            if (Objects.equals(o.getClass(), Player.class)) {
                allPlayers.add((Player) o);

            }
        }
        //        System.out.println(allPlayers);
        this.map = map;

        this.scoreBoard = scoreboard;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public LinkedBlockingQueue getDeadPlayers() {
        return deadPlayers;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public ArrayList<PhysicsObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<PhysicsObject> objects) {
        this.objects = objects;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }


    /**
     * @param player the player you want to excluded from the array list of players
     * @return an array list of other players other than chosen player
     */
    public ArrayList<Player> getOtherPlayers(Player player) {
        ArrayList<Player> otherPlayers = new ArrayList<>(allPlayers);
        otherPlayers.remove(player);
        return otherPlayers;
    }


    /**
     * @param object the object you want to excluded from the array list of players
     * @return an array list of other physics objects other than chosen object
     */
    public ArrayList<PhysicsObject> getOtherObjects(PhysicsObject object) {
        ArrayList<PhysicsObject> otherObjects = new ArrayList<>(objects);
        otherObjects.remove(object);
        return otherObjects;
    }

    /**
     * Start the game
     */
    public void start() {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    /**
     * End the game
     */
    public void stop() {
        isRunning = false;
    }

    public boolean getRunning() {
        return isRunning;
    }

    public AiControllersManager getAiConMan() {
        return aiConMan;
    }
}
