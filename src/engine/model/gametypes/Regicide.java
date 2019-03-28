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
    private int kingID;
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
        this.kingID = king.getId();
    }

    /**
     * Constructor
     *
     * @param kingID        the first king
     * @param scoreNeeded the score needed for a player to win
     */
    public Regicide(int kingID, int scoreNeeded) {
        super(Type.Regicide);
        this.kingID = kingID;
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

    public void setKingId(int kingId) {
        this.kingID = kingId;
    }

    public int getKingId() {
        return kingID;
    }
}
