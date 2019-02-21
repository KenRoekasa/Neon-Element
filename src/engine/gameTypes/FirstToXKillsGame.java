package engine.gameTypes;

public class FirstToXKillsGame extends GameType{
    private int killsNeeded;

    public FirstToXKillsGame(int killsNeeded) {
        super(Type.FirstToXKills);
        this.killsNeeded = killsNeeded;
    }

    public int getKillsNeeded() {
        return killsNeeded;
    }
}
