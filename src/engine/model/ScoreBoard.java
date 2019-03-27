package engine.model;

import engine.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ScoreBoard {
    //Hash map , key = ID, value = pair type of  (number of kills, list of victims)
    private HashMap<Integer, Score> board;
    private int totalScore;
    private ArrayList<Integer> leaderBoard;

    public ScoreBoard() {
        board = new HashMap<>();
        totalScore = 0;
        leaderBoard = new ArrayList<>();
    }

    public ArrayList<Integer> getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * Create the hashmap based off the players in the game.
     * Call this when the match is starting
     *
     * @param playerList the list of all player
     */
    public void initialise(ArrayList<Player> playerList) {
        for (Player p : playerList) {
            board.put(p.getId(), new Score());
            leaderBoard.add(p.getId());

        }
    }

    /** Add 1 kill to the killers score and add 1 death to the victim score
     * @param killerID
     * @param victimID
     */
    public void addKill(int killerID, int victimID) {
        int oldKills = board.get(killerID).getKills();
        //Add kill to the killer
        board.get(killerID).setKills(oldKills + 1);
        //Add death to the victim
        int oldDeaths = board.get(victimID).getDeaths();
        board.get(victimID).setDeaths(oldDeaths+1);
        totalScore++;
        updateLeaderBoard();
    }

    public void addScore(int scorer, int score) {
        int oldScore = board.get(scorer).getScore();
        board.get(scorer).setScore(oldScore + score);
        //TODO: What do i do instead of total kills
        totalScore++;
        updateLeaderBoard();
    }

    /**
     * Updates the leaderboard so its in the order of the score high to low
     */
    private void updateLeaderBoard() {
        leaderBoard.sort((o1, o2) -> {
            if (board.get(o1).getScore() > board.get(o2).getScore()) {
                return -1;
            } else if (board.get(o1).getScore() < (board.get(o2).getScore())) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    public Integer getPlayerKills(int playerID) {
        return board.get(playerID).getKills();
    }

    public Integer getPlayerScore(int playerID) {
        return board.get(playerID).getScore();
    }

    public Integer getPlayerDeaths(int playerID) {
        return board.get(playerID).getDeaths();
    }

    public int getTotalKills() {
        return totalScore;
    }

    @Override
    public String toString() {
        String string = "";
        Iterator it = board.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry pair = ( java.util.Map.Entry)it.next();
            string += pair.getKey() + " = " + pair.getValue();
//            it.remove(); // avoids a ConcurrentModificationException
        }
        return string;
    }
}

/**
 * The class that scores all the information about the player stats in the current game e.g. score, kills, deaths
 */
class Score {
    private int score = 0;
    private int kills = 0;
    private int deaths = 0;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    @Override
    public String toString() {
        return score + " " + kills + " " + deaths;
    }
}
