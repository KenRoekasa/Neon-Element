package engine.gameTypes;

import javafx.scene.shape.Circle;

public class HillGame extends GameType {
    private Circle hill;
    private int scoreNeeded;

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
