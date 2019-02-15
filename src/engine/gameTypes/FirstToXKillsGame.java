package engine.gameTypes;

public class FirstToXKillsGame extends GameType{
    private int killsNeeded;

    public FirstToXKillsGame(int killsNeeded) {
        this.killsNeeded = killsNeeded;
    }

    public int getKillsNeeded() {
        return killsNeeded;
    }
}