package engine;

import client.ClientGameState;
import client.GameClient;
import engine.calculations.DamageCalculation;
import engine.entities.CollisionDetection;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class Physics {
    private GameState gameState;

    public Physics(GameState gameState) {
        this.gameState = gameState;
    }

    public void clientLoop() {
        doCollisionDetection();
        doHitDetection();
        doUpdates();
        deathHandler();
        if (!GameTypeHandler.checkRunning(gameState)) {
            gameState.stop();
        }
    }

    private void doUpdates() {
        synchronized (gameState.getObjects()) {
            // Call update function for all physics objects

            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }
    }

    private void deathHandler() {
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        LinkedBlockingQueue deadPlayers = gameState.getDeadPlayers();
        ScoreBoard scoreBoard = gameState.getScoreBoard();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            //If not already dead
            if (!deadPlayers.contains(player) && !player.isAlive()) {
                // Add to dead list

                deadPlayers.offer(player);

                // Add kills to scoreboard
                scoreBoard.addKill(player.getLastAttacker().getId());
                //if dead teleport player off screen
                player.setLocation(new Point2D(5000, 5000));

            }

        }


    }

    private void doCollisionDetection() {

        ArrayList<PhysicsObject> objects = gameState.getObjects();
        ArrayList<Player> allPlayers = gameState.getAllPlayers();

        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            synchronized (objects) {
                // Collision detection code
                Player projectedPlayer = new Player(ObjectType.PLAYER);

                ArrayList<PhysicsObject> otherObjects = gameState.getOtherObjects(player);
                for (Iterator<PhysicsObject> itr1 = otherObjects.iterator(); itr1.hasNext(); ) {
                    PhysicsObject e = itr1.next();
                    // Check if the moving in a certain direction will cause a collision
                    // The player has collided with e do something
                    if (e.getTag() == ObjectType.POWERUP) {
                        if (CollisionDetection.checkCollision(player, e)) {
                            PowerUp powerUp = (PowerUp) e;
                            powerUp.activatePowerUp(player);
                            // remove power up from objects array list
                            itr1.remove();
                            objects.remove(e);
                        }
                    } else {

                        float movementSpeed = player.getMovementSpeed() * GameClient.deltaTime;

                        Point2D checkUp = player.getLocation().add(-movementSpeed, -movementSpeed);

                        Point2D checkDown = player.getLocation().add(movementSpeed, movementSpeed);

                        Point2D checkLeft = player.getLocation().add(-movementSpeed, +movementSpeed);

                        Point2D checkRight = player.getLocation().add(movementSpeed, -movementSpeed);

                        Point2D checkUpCart = player.getLocation().add(0, -movementSpeed);

                        Point2D checkDownCart = player.getLocation().add(0, movementSpeed);

                        Point2D checkLeftCart = player.getLocation().add(-movementSpeed, 0);

                        Point2D checkRightCart = player.getLocation().add(movementSpeed, 0);

                        switch (player.getCharacterDirection()) {
                            case UP:
                                projectedPlayer.setLocation(checkUp);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;
                            case DOWN:
                                projectedPlayer.setLocation(checkDown);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;
                            case LEFT:
                                projectedPlayer.setLocation(checkLeft);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;

                            case RIGHT:
                                projectedPlayer.setLocation(checkRight);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;
                            case UPCART:
                                projectedPlayer.setLocation(checkUpCart);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;
                            case DOWNCART:
                                projectedPlayer.setLocation(checkDownCart);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;
                            case LEFTCART:
                                projectedPlayer.setLocation(checkLeftCart);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;
                            case RIGHTCART:
                                projectedPlayer.setLocation(checkRightCart);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.setVerticalMove(0);
                                    player.setHorizontalMove(0);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private void doHitDetection() {
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            ArrayList<Player> otherPlayers = gameState.getOtherPlayers(player);
            ArrayList<Player> lightHittablePlayers = new ArrayList<>();
            ArrayList<Player> heavyHittablePlayer = new ArrayList<>();
            // Loop through all enemies to detect hit detection
            for (Iterator<Player> itr1 = otherPlayers.iterator(); itr1.hasNext(); ) {
                Player e = itr1.next();
                // Check light attack
                if (CollisionDetection.checkCollision(player.getAttackHitbox().getBoundsInParent(), e.getBounds().getBoundsInParent())) {
                    lightHittablePlayers.add(e);
                }
                //Check heavy attack
                if (CollisionDetection.checkCollision(player.getHeavyAttackHitbox().getBoundsInParent(),
                        e.getBounds().getBoundsInParent())) {
                    heavyHittablePlayer.add(e);
                }
            }
            // Attack Collision
            // if player is light attacking
            if (player.getCurrentAction() == Action.LIGHT) {
                for (Player e : lightHittablePlayers) {
                    if (e.getLastAttacker() != null) {
                        // If the player isn't invulnerable and attack by the same person
                        if (e.getIframes() <= 0 || e.getLastAttacker().getId() != player.getId()) {
                            float damage = DamageCalculation.calculateDealtDamage(player, e);
                            e.takeDamage(damage, player);
                        }
                    } else {
                        float damage = DamageCalculation.calculateDealtDamage(player, e);
                        e.takeDamage(damage, player);
                    }
                }
            }

            // if player is heavy attacking
            if (player.getCurrentAction() == Action.HEAVY) {
                for (Player e : heavyHittablePlayer) {
                    if (e.getLastAttacker() != null) {
                        // If the player isn't invulnerable and attack by the same person
                        if (e.getIframes() <= 0 || e.getLastAttacker().getId() != player.getId()) {
                            float damage = DamageCalculation.calculateDealtDamage(player, e);
                            e.takeDamage(damage, player);
                        }
                    } else {
                        float damage = DamageCalculation.calculateDealtDamage(player, e);
                        e.takeDamage(damage, player);
                    }
                }
            }
        }
    }
}