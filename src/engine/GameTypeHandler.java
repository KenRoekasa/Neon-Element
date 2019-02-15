package engine;

import engine.gameTypes.FirstToXKillsGame;
import engine.gameTypes.GameType;
import engine.gameTypes.TimedGame;

public class GameTypeHandler {


    public static boolean checkRunning(GameState currentGame) {

        GameType type = currentGame.gameType;


        if(type.getClass().equals(TimedGame.class)) {
            // check whether game time is less
            TimedGame t = (TimedGame)type;
            long duration = t.getDuration();

            return currentGame.startTime + duration < System.currentTimeMillis();

            // todo implement!
        } else if (type.getClass().equals(FirstToXKillsGame.class)) {
            return true;
        }

        // return true to allow testing games to run infinitely
        return true;
    }


}
