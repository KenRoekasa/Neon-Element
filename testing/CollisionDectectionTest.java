import engine.entities.CollisionDetection;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CollisionDectectionTest {
    @Test
    public void playerCollidingWithEnemy() {
        Player player1 = new Player(ObjectType.PLAYER);
        player1.setLocation(new Point2D(100, 100));
        Player player2 = new Player(ObjectType.PLAYER);
        player2.setLocation(new Point2D(100, 100));
        assertTrue(CollisionDetection.checkCollision(player1, player2));
    }

    @Test
    public void playerCollidingWithPowerUp() {
        Player player = new Player(ObjectType.PLAYER);
        player.setLocation(new Point2D(250, 250));
        PowerUp powerUp = new PowerUp();
        powerUp.setLocation(new Point2D(250, 250));
        assertTrue(CollisionDetection.checkCollision(player, powerUp));
    }

    @Test
    public void playerLightAttackHitDetected() {
        Player player = new Player(ObjectType.PLAYER);
        player.setLocation(new Point2D(250, 250));
        Player enemy = new Player(ObjectType.ENEMY);
        enemy.setLocation(new Point2D(250, 230));
        Rectangle attackHitbox = new Rectangle(player.getLocation().getX(), player.getLocation().getY() - player.getWidth(), player.getWidth(), player.getWidth());
        // Rotation with axis on centre of Player
        Rotate rotate = new Rotate(player.getPlayerAngle().getAngle(), player.getLocation().getX(), player.getLocation().getY());
        attackHitbox.getTransforms().addAll(rotate);
        assertTrue(CollisionDetection.checkCollision(attackHitbox.getBoundsInParent(), enemy.getBounds().getBoundsInParent()));
        player.setPlayerAngle(new Rotate(45));
        enemy.setLocation(new Point2D(270, 230));
        attackHitbox.setX(player.getLocation().getX());
        attackHitbox.setY(player.getLocation().getY()-player.getWidth());
        rotate = new Rotate(player.getPlayerAngle().getAngle(), player.getLocation().getX()+(player.getWidth()/2), player.getLocation().getY()+(player.getWidth()/2));
        attackHitbox.getTransforms().add(rotate);
        assertTrue(CollisionDetection.checkCollision(attackHitbox.getBoundsInParent(), enemy.getBounds().getBoundsInParent()));
    }


}
