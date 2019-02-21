package engine;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ScoreBoard {
    //Hash map , key = ID, value = pair type of  (number of kills, list of victims)
    private HashMap<Integer, Pair<Integer, ArrayList<Integer>>> board;
    private int totalKills;

    public ArrayList<Integer> getLeaderBoard() {
        return leaderBoard;
    }

    private ArrayList<Integer> leaderBoard;

    public ScoreBoard(int numberOfPlayers) {

        board = new HashMap<>();
        totalKills = 0;
        leaderBoard = new ArrayList<>();

        for(int i = 0; i < numberOfPlayers; i++){
            board.put(i, new Pair<>(0, new ArrayList<>()));
            leaderBoard.add(i);

        }
    }

    public void addKill(int killerID, int recipientID){
        int newTotal = board.get(killerID).getKey() + 1;
        ArrayList<Integer> newLog = board.get(killerID).getValue();
        newLog.add(recipientID);
        Pair<Integer, ArrayList<Integer>> newPair = new Pair<>(newTotal, newLog);

        board.put(killerID, newPair);
        totalKills ++;
        updateLeaderBoard();
    }


    private void updateLeaderBoard(){
        leaderBoard.sort((o1, o2) -> {
            if (board.get(o1).getKey() > board.get(o2).getKey()){
                return 1;
            } else if (board.get(o1).getKey().equals(board.get(o2).getKey())){
                return 0;
            } else {
                return  -1;
            }
        });
    }

    public Pair<Integer, ArrayList<Integer>> getPlayerKills(int playerID){
        return board.get(playerID);
    }

    public int getTotalKills() {
        return totalKills;
    }
}
