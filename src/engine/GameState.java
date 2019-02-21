package engine;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class GameState {
    protected Rectangle map;
    /**
     * All Physics objects
     */
    protected ArrayList<PhysicsObject> objects;
    protected GameType gameType;
    protected long startTime;
    /**
     * The ScoreBoard
     */
    protected ScoreBoard scoreBoard;
    protected LinkedBlockingQueue deadPlayers;
    protected ArrayList<Player> allPlayers = new ArrayList<>();


    public GameState(Rectangle map, ArrayList<PhysicsObject> objects, LinkedBlockingQueue deadPlayers, ScoreBoard scoreboard, GameType gameType){
        this.objects = objects;
        this.gameType = gameType;
        for(PhysicsObject o: objects){
            if(Objects.equals(o.getClass(), Player.class)){
                allPlayers.add((Player) o);

            }
        }
//        System.out.println(allPlayers);
        this.map = map;
        this.deadPlayers = deadPlayers;
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

    public Rectangle getMap() {
        return map;
    }

    public void setMap(Rectangle map) {
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
    public ArrayList<Player> getOtherPlayers(Player player){
        ArrayList<Player> otherPlayers = new ArrayList<>();
        otherPlayers.addAll(allPlayers);
        otherPlayers.remove(player);
        return otherPlayers;
    }


    /**
     * @param object the object you want to excluded from the array list of players
     * @return an array list of other physics objects other than chosen object
     */
    public ArrayList<PhysicsObject> getOtherObjects(PhysicsObject object) {
        ArrayList<PhysicsObject> otherObjects = new ArrayList<>();
        otherObjects.addAll(objects);
        otherObjects.remove(object);
        return otherObjects;
    }
}
