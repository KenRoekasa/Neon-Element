package engine.gameTypes;

public class Regicide extends GameType {
    int scoreNeeded;
    int kingId;

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
