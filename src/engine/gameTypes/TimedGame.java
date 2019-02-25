package engine.gameTypes;

public class TimedGame extends GameType {
    private long duration;


    public TimedGame(long duration) {
        super(Type.Timed);
        this.duration = duration;
    }


    public long getDuration() {
        return duration;
    }

}
