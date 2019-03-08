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
import engine.gameTypes.GameType;
import engine.gameTypes.HillGame;
import engine.gameTypes.Regicide;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

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
        new Thread(() -> doHitDetection()).start();
        doUpdates();
        deathHandler();
        if (gameState.getGameType().getType().equals(GameType.Type.Hill)) {
            kingOfHillHandler();
        }
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

    private void kingOfHillHandler() {
        HillGame hillGame = (HillGame) gameState.getGameType();
        Circle hill = hillGame.getHill();
//        System.out.println("player " + gameState.getPlayer().getBounds().getBoundsInParent().getMaxX());
//        System.out.println("hill : " + hill);
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        ArrayList<Player> playersInside = new ArrayList<>();
        ScoreBoard scoreBoard = gameState.getScoreBoard();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            if (CollisionDetection.checkCollision(hill, player.getBounds())) {
                playersInside.add(player);
            }
        }
        // if only one player is inside the circle/hill count score
        if (playersInside.size() == 1) {
            Player onlyPlayer = playersInside.get(0);
            int onlyPlayerId = onlyPlayer.getId();
            scoreBoard.addScore(onlyPlayerId, (int) (1 * GameClient.deltaTime));
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
                // For any game mode add kills to scoreboard
                scoreBoard.addKill(player.getLastAttacker().getId(), player.getId());

                if (gameState.getGameType().getType() == GameType.Type.FirstToXKills) {
                    scoreBoard.addScore(player.getLastAttacker().getId(), 1);
                } else if (gameState.getGameType().getType() == GameType.Type.Regicide) {
                    Regicide regicide = (Regicide) gameState.getGameType();
                    int baseScore = 5;
                    // if the player dead is the king the killer gets more points
                    if (regicide.getKingId() == player.getId()) {
                        scoreBoard.addScore(player.getLastAttacker().getId(), baseScore * 2);
                        // Make the attacker the king now
                        regicide.setKingId(player.getLastAttacker().getId());
                    } else {
                        scoreBoard.addScore(player.getLastAttacker().getId(), baseScore);
                    }
                }
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
            synchronized (itr) {
                Player player = itr.next();
                ArrayList<Player> otherPlayers = gameState.getOtherPlayers(player);
                ArrayList<Player> lightHittablePlayers = new ArrayList<>();
                ArrayList<Player> heavyHittablePlayer = new ArrayList<>();
                // Loop through all enemies to detect hit detection
                for (Iterator<Player> itr1 = otherPlayers.iterator(); itr1.hasNext(); ) {
                    Player e = itr1.next();
                    // Check light attack
                    if (CollisionDetection.checkCollision(player.getAttackHitbox(), e.getBounds())) {
                        lightHittablePlayers.add(e);
                    }
                    //Check heavy attack
                    if (CollisionDetection.checkCollision(player.getHeavyAttackHitbox(),
                            e.getBounds())) {
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
}