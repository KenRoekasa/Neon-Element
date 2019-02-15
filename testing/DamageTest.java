import entities.CollisionDetection;
import entities.Player;
import enums.Action;
import enums.ObjectType;
import javafx.geometry.Point2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DamageTest {
    @Test
    public void lightAttackDamageTest() {
        Player player = new Player(ObjectType.PLAYER);
        player.setLocation(new Point2D(250, 250));
        Player enemy = new Player(ObjectType.ENEMY);
        enemy.setLocation(new Point2D(250, 220));
        // if I don't have these print statements the attack fails
        player.getAttackHitbox();
        enemy.getBounds().getBoundsInParent();
//        System.out.println(player.getAttackHitbox());
//        System.out.println(enemy.getBounds().getBoundsInParent());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (enemy) {
                        //Attack Collision
                        //if player is light attacking
                        if (player.getCurrentAction() == Action.LIGHT) {
                            if (CollisionDetection.checkCollision(player.getAttackHitbox().getBoundsInParent(), enemy.getBounds().getBoundsInParent())) {
                                // e takes damage
                                Player enemy1 = (Player) enemy;
                                // TODO: For now its takes 3 damage, change later
                                enemy1.removeHealth(3);
                                player.setCurrentAction(Action.IDLE);
                                System.out.println("hit");
                                // Sends to server
                            }

                        }
                        if (player.getCurrentAction() == Action.HEAVY) {
                            if (CollisionDetection.checkCollision(player.getHeavyAttackHitbox().getBoundsInParent(), enemy.getBounds().getBoundsInParent())) {
                                // e takes damage
                                Player enemy1 = (Player) enemy;
                                // TODO: For now its takes 10 damage, change later
                                enemy1.removeHealth(10);
                                System.out.println("heavy hit");
                                // Sends to server
                            }
                        }
                    }
                }
            }
        })).start();
        player.lightAttack();
        assertEquals(97, enemy.getHealth(), 0);
    }

    @Test
    public void heavyAttackDamageTest() {
        Player player = new Player(ObjectType.PLAYER);
        player.setLocation(new Point2D(250, 250));
        Player enemy = new Player(ObjectType.ENEMY);
        enemy.setLocation(new Point2D(250, 230));
        // if I don't have these print statements the attack fails
        player.getHeavyAttackHitbox();
        enemy.getBounds();
//        System.out.println(player.getAttackHitbox());
//        System.out.println(enemy.getBounds().getBoundsInParent());
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //Attack Collision
                    //if player is light attacking
                    synchronized (enemy) {
                        if (player.getCurrentAction() == Action.LIGHT) {
                            if (CollisionDetection.checkCollision(player.getAttackHitbox().getBoundsInParent(), enemy.getBounds().getBoundsInParent())) {
                                // e takes damage
                                Player enemy1 = (Player) enemy;
                                // TODO: For now its takes 3 damage, change later
                                enemy1.removeHealth(3);
                                player.setCurrentAction(Action.IDLE);
                                System.out.println("hit");
                                // Sends to server
                            }

                        }
                        if (player.getCurrentAction() == Action.HEAVY) {
                            if (CollisionDetection.checkCollision(player.getHeavyAttackHitbox().getBoundsInParent(), enemy.getBounds().getBoundsInParent())) {
                                // e takes damage
                                Player enemy1 = (Player) enemy;
                                // TODO: For now its takes 10 damage, change later
                                enemy1.removeHealth(10);
                                player.setCurrentAction(Action.IDLE);
                                System.out.println("heavy hit");
                                // Sends to server
                            }
                        }
                    }
                }
            }
        })).start();
        player.chargeHeavyAttack();
        assertEquals(90, enemy.getHealth(), 0);
    }

}
