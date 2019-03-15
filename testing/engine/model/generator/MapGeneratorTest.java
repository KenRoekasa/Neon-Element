package engine.model.generator;

import engine.entities.Wall;
import engine.model.Map;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MapGeneratorTest {


    @Test
    public void createEmptyMapIsSize2000x2000() {
        Map map = MapGenerator.createEmptyMap();
        assertEquals(2000,map.getGround().getWidth(),0);
        assertEquals(2000,map.getGround().getHeight(),0);
    }

    @Test
    public void createEmptyMapHasNoWalls() {
        Map map = MapGenerator.createEmptyMap();
        ArrayList<Wall> walls = new ArrayList<>();
        assertEquals(walls,map.getWalls());
    }


}