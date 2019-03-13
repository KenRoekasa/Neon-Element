package engine;

import engine.gameTypes.*;

import java.util.ArrayList;

/**
 * Handles the different game types to end game based on win condition
 */
public class GameTypeHandler {


    /**
     * Check if the game has to end or not based of the win condition
     *
     * @param currentGame the game state of the game you want to check
     * @return True if a player hasn't reached the win condition; false otherwise
     */
    public static boolean checkRunning(GameState currentGame) {

        GameType gameType = currentGame.gameType;


        if (gameType.getType().equals(GameType.Type.Timed)) {
            // check whether game time is less
            TimedGame t = (TimedGame) gameType;
            long duration = t.getDuration();
            return currentGame.startTime + duration > System.currentTimeMillis();
        } else if (gameType.getType().equals(GameType.Type.FirstToXKills)) {
            FirstToXKillsGame typeObj = (FirstToXKillsGame) gameType;
            ArrayList<Integer> score = currentGame.getScoreBoard().getLeaderBoard();
            int topPlayerId = score.get(0);
            return currentGame.getScoreBoard().getPlayerScore(topPlayerId) < typeObj.getKillsNeeded();
        } else if (gameType.getType().equals(GameType.Type.Hill)) {
            HillGame h = (HillGame) gameType;
            ArrayList<Integer> score = currentGame.getScoreBoard().getLeaderBoard();
            int topPlayerId = score.get(0);
            return currentGame.getScoreBoard().getPlayerScore(topPlayerId) < h.getScoreNeeded();
        } else if (gameType.getType().equals(GameType.Type.Regicide)) {
            Regicide r = (Regicide) gameType;
            ArrayList<Integer> score = currentGame.getScoreBoard().getLeaderBoard();
            int topPlayerId = score.get(0);
            return currentGame.getScoreBoard().getPlayerScore(topPlayerId) < r.getScoreNeeded();
        }

        // return true to allow testing games to run infinitely
        return true;
    }


}
