package engine;

import engine.gameTypes.GameType;
import entities.PhysicsObject;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class GameState {
    protected Rectangle map;
    protected ArrayList<PhysicsObject> objects;
    protected GameType gameType;


    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
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
}
