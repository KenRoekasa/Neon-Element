package engine;

import engine.entities.Player;
import javafx.util.Pair;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ScoreBoard {
    //Hash map , key = ID, value = pair type of  (number of kills, list of victims)
    private HashMap<Integer, Integer> board;
    private int totalKills;

    public ArrayList<Integer> getLeaderBoard() {
        return leaderBoard;
    }

    private ArrayList<Integer> leaderBoard;

    public ScoreBoard() {
        board = new HashMap<>();
        totalKills = 0;
        leaderBoard = new ArrayList<>();


    }

    /** Create the hashmap based off the players in the game.
     * Call this when the match is starting
     * @param playerList the list of all player
     */
    public void initialise(ArrayList<Player> playerList){
        for(Player p : playerList){
            board.put(p.getId(), 0);
            leaderBoard.add(p.getId());

        }
    }

    public void addKill(int killerID){
        board.replace(killerID, board.get(killerID)+1);
        totalKills ++;
        updateLeaderBoard();
    }


    private void updateLeaderBoard(){
        leaderBoard.sort((o1, o2) -> {
            if (board.get(o1) > board.get(o2)){
                return 1;
            } else if (board.get(o1).equals(board.get(o2))){
                return 0;
            } else {
                return  -1;
            }
        });
    }

    public Integer getPlayerKills(int playerID){
        return board.get(playerID);
    }

    public int getTotalKills() {
        return totalKills;
    }
}
