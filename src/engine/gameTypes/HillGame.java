package engine.gameTypes;

import javafx.scene.shape.Circle;

public class HillGame extends GameType {
    private Circle hill;
    private float scoreNeeded;

    public HillGame(Circle hill, float scoreNeeded) {
        super(Type.Hill);
        this.hill = hill;
        this.scoreNeeded = scoreNeeded;
    }

    public float getScoreNeeded() {
        return scoreNeeded;
    }

    public Circle getHill() {
        return hill;
    }
}
