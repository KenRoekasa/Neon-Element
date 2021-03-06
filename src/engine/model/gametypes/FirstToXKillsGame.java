package engine.model.gametypes;

import engine.model.GameType;

/**
 * A game mode; win condition: get X number of kills
 */
public class FirstToXKillsGame extends GameType {
    private static int killsNeeded;

    /**
     * Constructor
     *
     * @param killsNeeded The kills needed to win/end the game
     */
    public FirstToXKillsGame(int killsNeeded) {
        super(Type.FirstToXKills);
        this.killsNeeded = killsNeeded;
    }

    public static int getKillsNeeded() {
        return killsNeeded;
    }
}
