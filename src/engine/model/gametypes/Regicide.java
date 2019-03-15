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
    private int scoreNeeded;
    private int kingId;

    /**
     * Constructor
     *
     * @param kingId        the first king
     * @param scoreNeeded the score needed for a player to win
     */
    public Regicide(int kingId, int scoreNeeded) {
        super(Type.Regicide);
        this.kingId = kingId;
        this.scoreNeeded = scoreNeeded;
    }


    public int getScoreNeeded() {
        return scoreNeeded;
    }

    public int getKingId() {
        return kingId;
    }

    public void setKingId(int kingId) {
        this.kingId = kingId;
    }
}
