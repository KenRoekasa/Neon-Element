package engine.entities;

import engine.model.enums.Action;
import engine.model.enums.Elements;
import engine.model.enums.ObjectType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;
    Player enemy;

    @Before
    public void setUp() throws Exception {
        player = new Player(ObjectType.PLAYER);
        enemy = new Player(ObjectType.ENEMY);

    }

    @Test
    public void onCreatePlayerIsAlive() {
        assertTrue(player.isAlive());
    }

    @Test
    public void playerHealthCantExceedMaxHealth() {
        player.addHealth(100);
        player.addHealth(104560);
        player.addHealth(104560);
        player.addHealth(104650);
        player.addHealth(1004564);
        player.addHealth(100256565);
        assertEquals(100, player.health, 0);
    }

    @Test
    public void takeDamageRemovesHealth() {
        player.takeDamage(10, enemy);
        assertEquals(90, player.getHealth(), 0);

    }

    @Test
    public void takeDamageStoresLastAttacker() {
        player.takeDamage(10, enemy);
        assertEquals(90, player.getHealth(), 0);
        assertEquals(enemy, player.lastAttacker);

    }

    @Test
    public void takeDamageIncreasesIframes() {
        player.takeDamage(10, enemy);
        assertEquals(15, player.iframes);

    }


    @Test
    public void lightAttack() {
        player.lightAttack();
        assertEquals(Action.LIGHT, player.currentAction);
    }

    @Test
    public void removeHealth() {
        player.removeHealth(82);
        assertEquals(18, player.health, 0);
    }

    @Test
    public void chargeHeavyAttack() {
        player.chargeHeavyAttack();
        assertEquals(Action.CHARGE, player.currentAction);
    }

    @Test
    public void shield() {
        player.shield();
        assertEquals(Action.BLOCK, player.currentAction);
    }

    @Test
    public void unShield() {
        player.unShield();
        assertEquals(Action.IDLE, player.currentAction);
    }

    @Test
    public void changeToFire() {
        player.changeToFire();
        assertEquals(Elements.FIRE, player.currentElement);
    }

    @Test
    public void changeToWater() {
        player.changeToWater();
        assertEquals(Elements.WATER, player.currentElement);
    }

    @Test
    public void changeToEarth() {
        player.changeToEarth();
        assertEquals(Elements.EARTH, player.currentElement);
    }

    @Test
    public void changeToAir() {
        player.changeToAir();
        assertEquals(Elements.AIR, player.currentElement);
    }

    @Test
    public void playerHealthBelow0IsDead() {
        player.removeHealth(1000000);
        player.update();
        assertFalse(player.isAlive);
    }


    @Test
    public void respawnCausesPlayerToBeAlive() {
        //player needs to die first
        player.removeHealth(1000000);
        player.update();

        player.respawn();
        assertTrue(player.isAlive);

    }

    @Test
    public void respawnCausesPlayerToSpawnWithIframes() {
        //player needs to die first
        player.removeHealth(1000000);
        player.update();

        player.respawn();
        assertEquals(120, player.iframes);
    }


    @Test
    public void addHealth() {
        player.removeHealth(98);
        player.addHealth(5);
        assertEquals(7, player.health, 0);
    }

    @Test
    public void speedBoost() {
        player.speedBoost();
        assertEquals(player.DEFAULT_MOVEMENT_SPEED * 2, player.movementSpeed, 0);
    }

    @Test
    public void damageBoostCauseDamagePowerToBeActivated() {
        player.damageBoost();
        assertTrue(player.damagePowerup);
    }

    @Test
    public void damageBoostSetsDamageMultiplierToTwo() {
        player.damageBoost();
        assertEquals(2, player.damageMultiplier, 0);

    }

}