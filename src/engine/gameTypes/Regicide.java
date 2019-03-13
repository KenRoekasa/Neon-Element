package engine.gameTypes;

import engine.entities.Player;

/**
 * A game mode where there's is a King
 * The King earn more points for a kill.
 * If a non-king kills a King he gets more points for killing the king.
 * Killing a normal player gives 1 point.
 */
public class Regicide extends GameType {
    private int scoreNeeded;
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


    public int getScoreNeeded() {
        return scoreNeeded;
    }

    public Player getKing() {
        return king;
    }

    public void setKing(Player king) {
        this.king = king;
    }
}
