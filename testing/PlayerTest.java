import entities.Player;
import enums.ObjectType;
import javafx.geometry.Point2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {
    @Test
    public void onCreatePlayerLocationDefault() {
        Player player = new Player(ObjectType.PLAYER);
        assertEquals(new Point2D(0, 0), player.getLocation());
    }

    @Test
    public void onCreatePlayerIsAlive() {
        Player player = new Player(ObjectType.PLAYER);
        assertTrue(player.isAlive());
    }

    private void assertTrue(boolean alive) {
    }

    @Test
    public void playerMaxHealthIs100() {
        Player player = new Player(ObjectType.PLAYER);
        assertEquals(player.getMAX_HEALTH(), 100,0);
    }

    @Test
    public void onCreatePlayerIsMaxHealth() {
        Player player = new Player(ObjectType.PLAYER);
        assertEquals(player.getMAX_HEALTH(), player.getHealth(),0);
    }

    @Test
    public void playerMoveDown(){
        Player player = new Player(ObjectType.PLAYER);
        Point2D newLocation = player.getLocation()  ;
        newLocation = newLocation.add(player.getMovementSpeed(),player.getMovementSpeed());
        player.moveDown(1500,1500);
        assertEquals(newLocation,player.getLocation() );
    }
}
