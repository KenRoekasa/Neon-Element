package engine.controller;

import client.ClientGameState;
import engine.ai.controller.AiControllersManager;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.model.Map;
import engine.model.ScoreBoard;
import engine.model.enums.ObjectType;
import engine.model.gametypes.FirstToXKillsGame;
import engine.model.gametypes.HillGame;
import engine.model.gametypes.Regicide;
import engine.model.gametypes.TimedGame;
import engine.model.generator.MapGenerator;
import javafx.scene.shape.Circle;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTypeHandlerTest {


    @Test
    public void checkRunningFirstToXKills() {
        Player player = new Player(ObjectType.PLAYER);
        player.setId(1);
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        objects.add(player);

        FirstToXKillsGame gametype = new FirstToXKillsGame(10);

        ScoreBoard scoreboard = new ScoreBoard();
        Map map = MapGenerator.createEmptyMap();
        ClientGameState gameState = new ClientGameState(player, map, objects, scoreboard, gametype, new AiControllersManager(objects, map.getGround(), player, scoreboard, gametype));
        scoreboard.initialise(gameState.getAllPlayers());

        assertTrue(GameTypeHandler.checkRunning(gameState));

        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);
        scoreboard.addScore(1, 1);

        assertFalse(GameTypeHandler.checkRunning(gameState));


    }
    @Test
    public void checkRunningHillGame() {
        Player player = new Player(ObjectType.PLAYER);
        player.setId(1);
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        objects.add(player);

        HillGame gametype = new HillGame(new Circle(0, 0, 10), 10000);

        ScoreBoard scoreboard = new ScoreBoard();
        Map map = MapGenerator.createEmptyMap();
        ClientGameState gameState = new ClientGameState(player, map, objects, scoreboard, gametype, new AiControllersManager(objects, map.getGround(), player, scoreboard, gametype));
        scoreboard.initialise(gameState.getAllPlayers());

        assertTrue(GameTypeHandler.checkRunning(gameState));

        scoreboard.addScore(1, 10000);

        assertFalse(GameTypeHandler.checkRunning(gameState));


    }
    @Test
    public void checkRunningRegicide() {
        Player player = new Player(ObjectType.PLAYER);
        player.setId(1);
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        objects.add(player);

        Regicide gametype = new Regicide(player, 5000);

        ScoreBoard scoreboard = new ScoreBoard();
        Map map = MapGenerator.createEmptyMap();
        ClientGameState gameState = new ClientGameState(player, map, objects, scoreboard, gametype, new AiControllersManager(objects, map.getGround(), player, scoreboard, gametype));
        scoreboard.initialise(gameState.getAllPlayers());

        assertTrue(GameTypeHandler.checkRunning(gameState));

        scoreboard.addScore(1, 5000);

        assertFalse(GameTypeHandler.checkRunning(gameState));


    }

    @Test
    public void checkRunningTimeGame() {
        Player player = new Player(ObjectType.PLAYER);
        player.setId(1);
        ArrayList<PhysicsObject> objects = new ArrayList<>();
        objects.add(player);

        TimedGame gametype = new TimedGame(1000);

        ScoreBoard scoreboard = new ScoreBoard();
        Map map = MapGenerator.createEmptyMap();
        ClientGameState gameState = new ClientGameState(player, map, objects, scoreboard, gametype, new AiControllersManager(objects, map.getGround(), player, scoreboard, gametype));
        scoreboard.initialise(gameState.getAllPlayers());
        gameState.start();
        assertTrue(GameTypeHandler.checkRunning(gameState));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(GameTypeHandler.checkRunning(gameState));


    }


}