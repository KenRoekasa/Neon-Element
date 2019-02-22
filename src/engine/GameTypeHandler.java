package engine;

import engine.gameTypes.FirstToXKillsGame;
import engine.gameTypes.GameType;
import engine.gameTypes.TimedGame;

import java.util.ArrayList;

public class GameTypeHandler {


    public static boolean checkRunning(GameState currentGame) {

        GameType type = currentGame.gameType;


        if (type.getClass().equals(TimedGame.class)) {
            // check whether game time is less
            TimedGame t = (TimedGame) type;
            long duration = t.getDuration();

            return currentGame.startTime + duration < System.currentTimeMillis();

            // todo implement!
        } else if (type.getClass().equals(FirstToXKillsGame.class)) {
            FirstToXKillsGame typeObj = (FirstToXKillsGame) type;
            ArrayList<Integer> score = currentGame.getScoreBoard().getLeaderBoard();
            int topPlayerId = score.get(0);
//            System.out.println(topPlayerId);
//            System.out.println(currentGame.getScoreBoard().getPlayerKills(topPlayerId));

            if (currentGame.getScoreBoard().getPlayerKills(topPlayerId) >= typeObj.getKillsNeeded()) {
                return false;
            } else {
                return true;
            }
        }

        // return true to allow testing games to run infinitely
        return true;
    }


}
