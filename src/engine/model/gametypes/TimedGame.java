package engine.model.gametypes;

import engine.model.GameType;

/**
 * A gamemode when the time limit is reach the game ends
 */
public class TimedGame extends GameType {
    /**
     * How long the game last
     */
    private static long duration;


    /** Constructor
     * @param duration Duration of how long the match will last in milliseconds
     */
    public TimedGame(long duration) {
        super(Type.Timed);
        this.duration = duration;
    }


    public static long getDuration() {
        return duration;
    }

}
