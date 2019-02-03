import entities.CollisionDetection;
import entities.Player;
import entities.PowerUp;
import javafx.geometry.Point2D;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollisionDectectionTest {
    @Test
    public void playerCollidingWithEnemy(){
        Player player1 = new Player();
        player1.setLocation(new Point2D(100,100));
        Player player2 = new Player();
        player2.setLocation(new Point2D(100,100));
        assertTrue(CollisionDetection.checkCollision(player1,player2));
    }
    @Test
    public void playerCollidingWithPowerUp(){
        Player player = new Player();
        player.setLocation(new Point2D(250,250));
        PowerUp powerUp= new PowerUp();
        powerUp.setLocation(new Point2D(250,250));
        assertTrue(CollisionDetection.checkCollision(player,powerUp));
    }
}
