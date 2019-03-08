package engine;

import engine.entities.Player;

import java.util.ArrayList;
import java.util.HashMap;

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

    public void addKill(int killerID, int victimID) {
        int oldKills = board.get(killerID).getKills();
        //Add kill to the killer
        board.get(killerID).setKills(oldKills+1);
        //Add death to the victim
        int oldDeaths = board.get(victimID).getDeaths();
        board.get(killerID).setDeaths(oldDeaths++);
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

    public Integer getPlayerDeaths(int playerID){
        return board.get(playerID).getDeaths();
    }

    public int getTotalKills() {
        return totalScore;
    }

    public String toString() {
        return board.toString();
    }
}
