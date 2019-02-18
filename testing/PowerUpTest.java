import engine.entities.CollisionDetection;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import engine.enums.PowerUpType;
import javafx.geometry.Point2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PowerUpTest {
    @Test
    public void powerUpActivatePowerup() {
        Player player = new Player(ObjectType.PLAYER);
        player.setLocation(new Point2D(250, 250));
        PowerUp powerUp = new PowerUp();
        powerUp.setLocation(new Point2D(250, 250));
        if (CollisionDetection.checkCollision(player, powerUp)) {
            powerUp.activatePowerUp(player);
            if (powerUp.getType() == PowerUpType.SPEED) {
                assertEquals(8, player.getMovementSpeed());

            }
        }

    }
}
