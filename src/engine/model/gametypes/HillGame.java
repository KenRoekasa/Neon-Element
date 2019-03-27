package engine.model.gametypes;

import engine.model.GameType;
import javafx.scene.shape.Circle;

/**
 * A Gamemode where there's circular area a player must stay within so that they earn points
 * win condition stay in the hill as long as possible
 */
public class HillGame extends GameType {
    /**
     * The circular area where points are earned
     */
    private Circle hill;

    /**
     * The score you need to win
     */
    private int scoreNeeded;

    /**
     * Constructor
     *
     * @param hill        The circular are where points are earned
     * @param scoreNeeded the scored needed to win/end the game
     */
    public HillGame(Circle hill, int scoreNeeded) {
        super(Type.Hill);
        this.hill = hill;
        this.scoreNeeded = scoreNeeded;
    }

    public HillGame(double centerX, double centerY, double radius, int scoreNeeded) {
        this(new Circle(centerX, centerY, radius), scoreNeeded);
    }

    public int getScoreNeeded() {
        return scoreNeeded;
    }

    public Circle getHill() {
        return hill;
    }
}


