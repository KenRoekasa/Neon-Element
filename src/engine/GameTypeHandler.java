package engine;

import engine.gameTypes.FirstToXKillsGame;
import engine.gameTypes.GameType;
import engine.gameTypes.TimedGame;

import java.util.ArrayList;

public class GameTypeHandler {


    public static boolean checkRunning(GameState currentGame) {

        GameType gameType = currentGame.gameType;


        if (gameType.getType().equals(GameType.Type.Timed)) {
            // check whether game time is less
            TimedGame t = (TimedGame) gameType;
            long duration = t.getDuration();

//            System.out.println("start Time: " + currentGame.startTime + "duration: " + duration + "current time:" + System.currentTimeMillis()
//            );
            return currentGame.startTime + duration > System.currentTimeMillis();

            // todo implement!
        } else if (gameType.getType().equals(GameType.Type.FirstToXKills)) {
            FirstToXKillsGame typeObj = (FirstToXKillsGame) gameType;
            ArrayList<Integer> score = currentGame.getScoreBoard().getLeaderBoard();
            int topPlayerId = score.get(0);
            return currentGame.getScoreBoard().getPlayerKills(topPlayerId) < typeObj.getKillsNeeded();
        }

        // return true to allow testing games to run infinitely
        return true;
    }


}
