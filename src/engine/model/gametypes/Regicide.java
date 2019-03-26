package engine.model.gametypes;

import engine.entities.Player;
import engine.model.GameType;

/**
 * A game mode where there's is a King
 * The King earn more points for a kill.
 * If a non-king kills a King he gets more points for killing the king.
 * Killing a normal player gives 1 point.
 */
public class Regicide extends GameType {
    private static int scoreNeeded;
    private Player king;

    /**
     * Constructor
     *
     * @param king        the first king
     * @param scoreNeeded the score needed for a player to win
     */
    public Regicide(Player king, int scoreNeeded) {
        super(Type.Regicide);
        this.king = king;
        this.scoreNeeded = scoreNeeded;
    }


    public static int getScoreNeeded() {
        return scoreNeeded;
    }

    public Player getKing() {
        return king;
    }

    public void setKing(Player king) {
        this.king = king;
    }
}
