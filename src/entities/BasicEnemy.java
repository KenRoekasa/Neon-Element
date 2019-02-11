package entities;


import ai.BasicEnemyFSM;
import ai.BasicEnemyStates;
import entities.Character;
import entities.PowerUp;
import enums.ObjectType;
import enums.PowerUpType;
import javafx.geometry.Point2D;

import java.util.Random;


public class BasicEnemy extends Character {

    BasicEnemyStates activeState;
    Character[] players;
    PowerUp[] powerUps;
    BasicEnemy enemy = this;
    int powerupIndex = -1;

    public BasicEnemy(Character[] players, PowerUp[] powerUps) {

        activeState = BasicEnemyStates.IDLE;
        assignRandomElement();
        this.players = players;
        this.powerUps = powerUps;
        tag = ObjectType.ENEMY;

    }

    public void start() {


        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                boolean bool = true;

                while (bool) {

                    BasicEnemyFSM.fetchAction(enemy, players, powerUps);

                    executeAction();
                    if (health <= 0)
                        bool = false;
                }
            }

        });
//
    }


    void assignRandomElement() {

        Random r = new Random();
        int rand = r.nextInt(4);
        switch (rand) {
            case 0:
                changeToEarth();
                break;
            case 1:
                changeToFire();
                break;
            case 2:
                changeToWater();
                break;
            case 3:
                changeToAir();
                break;
        }
    }


    public BasicEnemyStates getActiveState() {
        return activeState;
    }

    public void setState(BasicEnemyStates s) {
        activeState = s;
    }

    public void setPowerUpIndex(int i) {
        powerupIndex = i;
    }

    public void executeAction() {
        //TODO: implement this and then functions for each case
        BasicEnemyStates newState = null;
        switch (activeState) {
            case ATTACK:
                attack();
                break;
            case AGGRESSIVE_ATTACK:
                aggressiveAttack();
                break;
            case FIND_HEALTH:
                findHealth();
                break;
            case FIND_DAMAGE:
                findDamage();
                break;
            case FIND_SPEED:
                findSpeed();
                break;
        }
    }

    //basic enemy actions
    private void findSpeed() {
        PowerUp powerup = findNearestPowerUp(PowerUpType.SPEED);
        moveTo(powerup);
    }

    private void findDamage() {
        PowerUp powerup = findNearestPowerUp(PowerUpType.DAMAGE);
        moveTo(powerup);
    }

    private void findHealth() {
        PowerUp powerup = findNearestPowerUp(PowerUpType.HEAL);
        moveTo(powerup);
    }

    private void aggressiveAttack() {
        Character player = findNearestPlayer();
        moveTo(player);
        if (inAttackDistance(player)) {
            //if(canHeavyAttack() )
            //heavyAttack();
            lightAttack();
        }
    }

    private void attack() {
        Character player = findNearestPlayer();
        moveTo(player);
        if (inAttackDistance(player)) {
            lightAttack();
        }
    }

    private boolean inAttackDistance(Character player) {
        if (this.getLocation().distance(player.getLocation()) < getWidth())
            return true;
        return false;
    }

    private PowerUp findNearestPowerUp(PowerUpType pu) {
        Character player = findNearestPlayer();
        int index = -1;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < powerUps.length; i++) {
            if (powerUps[i].getType().equals(pu)) {
                double disToPU = calcDistance(powerUps[i].getLocation(), getLocation());
                if (disToPU < calcDistance(getLocation(), player.getLocation())) {
                    if (disToPU < distance) {
                        distance = disToPU;
                        index = i;
                    }
                }
            }
        }

        return powerUps[index];
    }

    private Character findNearestPlayer() {
        double minDis = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < players.length; i++) {
            double tempDis = calcDistance(players[i].getLocation(), getLocation());
            if (tempDis < minDis) {
                minDis = tempDis;
                index = i;
            }
        }
        return players[index];
    }

    private void moveTo(Character player) {

        Point2D playerLoc = player.getLocation();
        double distance = calcDistance(getLocation(), playerLoc);

        while ((int) distance - 1 > getWidth()) {

            if ((getLocation().getX() - playerLoc.getX()) > 0)
                setLocation(new Point2D(getLocation().getX() - getMovementSpeed(), getLocation().getY()));
            else if ((getLocation().getX() - playerLoc.getX()) < 0)
                setLocation(new Point2D(getLocation().getX() + getMovementSpeed(), getLocation().getY()));

            if ((getLocation().getY() - playerLoc.getY()) > 0)
                setLocation(new Point2D(getLocation().getX(), getLocation().getY() - getMovementSpeed()));

            else if ((getLocation().getY() - playerLoc.getY()) < 0)
                setLocation(new Point2D(getLocation().getX(), getLocation().getY() + getMovementSpeed()));

            distance = calcDistance(getLocation(), playerLoc);
        }
    }

    private void moveTo(PowerUp powerup) {

        Point2D powerupLoc = powerup.getLocation();
        double distance = calcDistance(getLocation(), powerupLoc);

        while ((int) distance - 1 > getWidth()) {

            if ((getLocation().getX() - powerupLoc.getX()) > 0)
                setLocation(new Point2D(getLocation().getX() - getMovementSpeed(), getLocation().getY()));
            else if ((getLocation().getX() - powerupLoc.getX()) < 0)
                setLocation(new Point2D(getLocation().getX() + getMovementSpeed(), getLocation().getY()));

            if ((getLocation().getY() - powerupLoc.getY()) > 0)
                setLocation(new Point2D(getLocation().getX(), getLocation().getY() - getMovementSpeed()));

            else if ((getLocation().getY() - powerupLoc.getY()) < 0)
                setLocation(new Point2D(getLocation().getX(), getLocation().getY() + getMovementSpeed()));

            distance = calcDistance(getLocation(), powerupLoc);
        }
    }

    private double calcDistance(Point2D x, Point2D y) {
        return x.distance(y);
    }

    @Override
    public void update() {

    }


}
