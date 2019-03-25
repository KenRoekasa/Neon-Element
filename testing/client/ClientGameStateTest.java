package client;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.model.generator.GameStateGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ClientGameStateTest {
    private ClientGameState gamestate;

    @Before
    public void setUp() throws Exception {
        gamestate = GameStateGenerator.createDemoGamestateSample(0, new ArrayList<String>(), "test");
    }

    @Test
    public void pause() {
        gamestate.resume();
        gamestate.pause();
        assertTrue(gamestate.getPaused());
    }

    @Test
    public void resume() {
        gamestate.pause();
        gamestate.resume();
        assertFalse(gamestate.getPaused());
    }


    @Test
    public void getOtherPlayers() {
        Player player = gamestate.getPlayer();
        ArrayList<Player> emptyPlayer = new ArrayList<>();
        assertEquals(emptyPlayer, gamestate.getOtherPlayers(player));
    }

    @Test
    public void getOtherObjects() {
        PhysicsObject object = gamestate.getPlayer();
        ArrayList<PhysicsObject> emptyObject = new ArrayList<>();
        assertEquals(emptyObject, gamestate.getOtherObjects(object));
    }

    @Test
    public void startChangesGetRunning() {
        gamestate.start();


        assertTrue(gamestate.getRunning());

    }

    @Test
    public void startSetStartTime() {
        gamestate.start();
        long accStartTime = System.nanoTime()/1000000;
        assertEquals(accStartTime, gamestate.getStartTime());
    }


    @Test
    public void stop() {
        gamestate.stop();
        assertFalse(gamestate.getRunning());
    }
}