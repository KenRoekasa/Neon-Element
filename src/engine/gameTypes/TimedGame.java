package engine.gameTypes;

public class TimedGame extends GameType {
    private long duration;


    /**
     * @param duration Duration of how long the match will last in milliseconds
     */
    public TimedGame(long duration) {
        super(Type.Timed);
        this.duration = duration;
    }


    public long getDuration() {
        return duration;
    }

}
