package engine.model;

import engine.entities.Player;
import engine.model.enums.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ScoreBoardTest {

    private ScoreBoard scoreBoard;

    @Before
    public void setUp() throws Exception {
        scoreBoard = new ScoreBoard();
    }

    @Test
    public void scoreboardSetupLeaderBoardCorrectly() {
        Player player = new Player(ObjectType.PLAYER, 1);

        Player player1 = new Player(ObjectType.PLAYER, 2);


        Player player2 = new Player(ObjectType.PLAYER, 3);


        Player player3 = new Player(ObjectType.PLAYER, 4);


        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player1);
        players.add(player2);
        players.add(player3);


        ArrayList<Integer> leaderboard = new ArrayList<>();
        leaderboard.add(1);
        leaderboard.add(2);
        leaderboard.add(3);
        leaderboard.add(4);
        scoreBoard.initialise(players);
        assertEquals(leaderboard, scoreBoard.getLeaderBoard());

    }

    @Test
    public void leaderboardSortsProperly() {
        Player player = new Player(ObjectType.PLAYER, 1);

        Player player1 = new Player(ObjectType.PLAYER, 2);


        Player player2 = new Player(ObjectType.PLAYER, 3);


        Player player3 = new Player(ObjectType.PLAYER, 4);


        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        scoreBoard.initialise(players);

        scoreBoard.addScore(player2.getId(), 1000);
        scoreBoard.addScore(player1.getId(), 500);
        scoreBoard.addScore(player3.getId(), 2);

        ArrayList<Integer> leaderboard = new ArrayList<>();
        leaderboard.add(player2.getId());
        leaderboard.add(player1.getId());
        leaderboard.add(player3.getId());
        leaderboard.add(player.getId());


        assertEquals(leaderboard, scoreBoard.getLeaderBoard());

    }

    @Test
    public void addKillChangesKillCount() {
        Player player = new Player(ObjectType.PLAYER, 1);

        Player player1 = new Player(ObjectType.PLAYER, 2);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player1);

        scoreBoard.initialise(players);

        scoreBoard.addKill(player.getId(), player1.getId());

        assertEquals(1,(int) scoreBoard.getPlayerKills(player.getId()));

    }
    @Test
    public void addKillChangesDeathCount() {
        Player player = new Player(ObjectType.PLAYER, 1);

        Player player1 = new Player(ObjectType.PLAYER, 2);

        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        players.add(player1);

        scoreBoard.initialise(players);

        scoreBoard.addKill(player.getId(), player1.getId());

        assertEquals(1,(int) scoreBoard.getPlayerDeaths(player1.getId()));

    }

    @Test
    public void addScore() {
        Player player = new Player(ObjectType.PLAYER, 1);


        ArrayList<Player> players = new ArrayList<>();
        players.add(player);


        scoreBoard.initialise(players);

        scoreBoard.addScore(player.getId(),1000);

        assertEquals(1000,(int) scoreBoard.getPlayerScore(player.getId()));
    }
}