package engine;

import engine.gameTypes.FirstToXKillsGame;
import engine.gameTypes.GameType;
import engine.gameTypes.TimedGame;

public class GameTypeHandler {

    public boolean checkRunning(GameType type, GameState currentGame) {

        if(type.getClass().equals(TimedGame.class)) {
            // check whether game time is less
            TimedGame t = (TimedGame)type;
            return currentGame.getCurrentRunTime() < t.getDuration();

            // todo implement!
        } else if (type.getClass().equals(FirstToXKillsGame.class)) {

        }

        return false;
    }



}
