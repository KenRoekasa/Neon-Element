package engine;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class GameState {
    protected Rectangle map;
    protected ArrayList<PhysicsObject> objects;
    protected GameType gameType;
    protected long startTime;



    protected ScoreBoard scoreBoard;
    protected ArrayList<Player> deadPlayers;

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
    public ArrayList<Player> getDeadPlayers() {
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


}
