package engine.gameTypes;

import engine.entities.Player;

public class Regicide extends GameType {
    int scoreNeeded;
    Player king;

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
