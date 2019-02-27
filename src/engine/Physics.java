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
    private ClientGameState gameState;

    public Physics(ClientGameState gameState) {
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

    public void doUpdates() {
        synchronized (gameState.getObjects()) {
            // Call update function for all physics objects
            gameState.getPlayer().update();
            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }
    }

    public void deathHandler() {
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

    public void doCollisionDetection() {

        ArrayList<PhysicsObject> objects = gameState.getObjects();
        ArrayList<Player> allPlayers = gameState.getAllPlayers();

        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            ArrayList<Player> otherPlayers = gameState.getOtherPlayers(player);
            synchronized (objects) {
                // Collision detection code
                player.canUp = true;
                player.canLeft = true;
                player.canRight = true;
                player.canDown = true;
                player.canUpCart = true;
                player.canRightCart = true;
                player.canLeftCart = true;
                player.canDownCart = true;
                Point2D previousLocation = player.getLocation();
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

                        int collisionOffset = 1;
                        double x = player.getLocation().getX();
                        double y = player.getLocation().getY();
                        float movementSpeed = player.getMovementSpeed() * GameClient.deltaTime;
                        Point2D checkUp = new Point2D(x - movementSpeed, y - movementSpeed);
                        Point2D checkDown = new Point2D(x + movementSpeed, y + movementSpeed);
                        Point2D checkLeft = new Point2D(x - movementSpeed, y + movementSpeed);
                        Point2D checkRight = new Point2D(x + movementSpeed, y - movementSpeed);
                        Point2D checkUpCart = new Point2D(x, y - movementSpeed);
                        Point2D checkDownCart = new Point2D(x, y + movementSpeed);
                        Point2D checkLeftCart = new Point2D(x - movementSpeed, y);
                        Point2D checkRightCart = new Point2D(x + movementSpeed, y);

                        // Check for rare occasion the player is inside another player
                        if (CollisionDetection.checkCollision(player, e)) {
                            // This line of code seems to cause a bug
//                                                    gameState.getPlayer().setLocation(previousLocation);
                            if (player == e) {
                                //                                System.out.println(player + " Collided with " + e);
                                //                                System.out.println("Collided with itself");
                            }
                        }

                        switch (player.getCharacterDirection()) {
                            case UP:
                                projectedPlayer.setLocation(checkUp);

                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    Point2D newLocation = previousLocation;
                                    // if on the right hand side of the other player
                                    if (e.getBounds().getBoundsInParent().getMaxX() <= player.getLocation().getX() && e.getBounds().getBoundsInParent().getMaxY() >= player.getLocation().getY()) {
                                        double adjacent = player.getLocation().getX()
                                                - e.getBounds().getBoundsInParent().getMaxX();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(e.getBounds().getBoundsInParent().getMaxX(),
                                                player.getLocation().getY() - opposite);
                                        newLocation = newLocation.add(collisionOffset, collisionOffset);
                                    }
                                    // if on the left hand side of the other player
                                    if (e.getBounds().getBoundsInParent().getMaxX() > player.getLocation().getX() && e.getBounds().getBoundsInParent().getMaxY() < player.getLocation().getY()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMaxY()
                                                - player.getLocation().getY();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() - opposite,
                                                e.getBounds().getBoundsInParent().getMaxY());
                                        newLocation = newLocation.add(-collisionOffset, collisionOffset);

                                    }

                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canUp = false;
                                    player.canUpCart = false;
                                    player.canLeftCart = false;
                                }

                                break;
                            case DOWN:
                                projectedPlayer.setLocation(checkDown);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    Point2D newLocation = previousLocation;
                                    // if on the right hand side of the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() <= e.getBounds()
                                            .getBoundsInParent().getMinY() && player.getBounds().getBoundsInParent().getMaxX() <= e.getBounds()
                                            .getBoundsInParent().getMinX()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMinY()
                                                - player.getBounds().getBoundsInParent().getMaxY();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + opposite,
                                                player.getLocation().getY() + adjacent);
                                        newLocation = newLocation.add(-collisionOffset, -collisionOffset);
                                    }

                                    // if on the left hand side of the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() > e.getBounds()
                                            .getBoundsInParent().getMinY() && player.getBounds().getBoundsInParent().getMaxX() > e.getBounds()
                                            .getBoundsInParent().getMinX()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMinX()
                                                - player.getBounds().getBoundsInParent().getMaxX();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + adjacent,
                                                player.getLocation().getY() + opposite);
                                        newLocation = newLocation.add(-collisionOffset, -collisionOffset);

                                    }

                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canDown = false;
                                    player.canDownCart = false;
                                    player.canRightCart = false;
                                }
                                break;
                            case LEFT:
                                projectedPlayer.setLocation(checkLeft);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = player.getLocation();
                                    // if above the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() <= e.getLocation().getY() && player.getLocation().getX() >= e.getBounds().getBoundsInParent().getMaxX()) {
                                        double adjacent = e.getBounds().getBoundsInParent().getMinY()
                                                - player.getBounds().getBoundsInParent().getMaxY();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() - opposite,
                                                player.getLocation().getY() + adjacent);
                                        newLocation = newLocation.add(collisionOffset, -collisionOffset);
                                    }

                                    // if below the other player
                                    if (player.getBounds().getBoundsInParent().getMaxY() > e.getLocation().getY() && player.getLocation().getX() < e.getBounds().getBoundsInParent().getMaxX()) {

                                        double opposite = player.getLocation().getX()
                                                - e.getBounds().getBoundsInParent().getMaxX();
                                        double adjacent = (opposite * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() - opposite,
                                                player.getLocation().getY() + adjacent);
                                        newLocation = newLocation.add(collisionOffset, -collisionOffset);
                                    }
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canLeft = false;
                                    player.canDownCart = false;
                                    player.canLeftCart = false;
                                    player.canRightCart = false;

                                }
                                break;
                            case RIGHT:
                                projectedPlayer.setLocation(checkRight);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = player.getLocation();
                                    // if above the other player
                                    if (player.getBounds().getBoundsInParent().getMaxX() <= e.getLocation().getX() && player.getBounds().getBoundsInParent().getMaxY() <= e.getLocation().getY()) {
                                        double adjacent = e.getLocation().getX()
                                                - player.getBounds().getBoundsInParent().getMaxX();
                                        double opposite = (adjacent * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + adjacent,
                                                player.getLocation().getY() - opposite);
                                        newLocation = newLocation.add(-collisionOffset, collisionOffset);

                                    }

                                    // if below the other player
                                    if (player.getBounds().getBoundsInParent().getMaxX() > e.getLocation().getX() && player.getBounds().getBoundsInParent().getMaxY() > e.getLocation().getY()) {
                                        double opposite = player.getLocation().getY()
                                                - e.getBounds().getBoundsInParent().getMaxY();
                                        double adjacent = (opposite * Math.tan(Math.toRadians(45)));
                                        newLocation = new Point2D(player.getLocation().getX() + adjacent,
                                                player.getLocation().getY() - opposite);
                                        newLocation = newLocation.add(-collisionOffset, collisionOffset);
                                    }
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canRight = false;
                                    player.canDown = false;
                                    player.canUpCart = false;
                                    player.canRightCart = false;
                                }
                                break;
                            case UPCART:
                                projectedPlayer.setLocation(checkUpCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = new Point2D(player.getLocation().getX(),
                                            e.getBounds().getBoundsInParent().getMaxY());
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canUpCart = false;
                                    player.canUp = false;
                                    player.canRight = false;
                                    player.canLeftCart = false;
                                }
                                break;
                            case DOWNCART:
                                projectedPlayer.setLocation(checkDownCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = new Point2D(player.getLocation().getX(),
                                            e.getBounds().getBoundsInParent().getMinY() - player.getWidth());
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canDownCart = false;
                                    player.canDown = false;
                                    player.canLeft = false;
                                    player.canRightCart = false;
                                }
                                break;
                            case LEFTCART:
                                projectedPlayer.setLocation(checkLeftCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    //
                                    Point2D newLocation = new Point2D(e.getBounds().getBoundsInParent().getMaxX(),
                                            player.getLocation().getY());
                                    ;
                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canLeftCart = false;
                                    player.canUp = false;
                                    player.canLeft = false;
                                    player.canUpCart = false;
                                }
                                break;
                            case RIGHTCART:
                                projectedPlayer.setLocation(checkRightCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    // test every the most amount of movement before it collides
                                    Point2D newLocation = new Point2D(
                                            e.getBounds().getBoundsInParent().getMinX() - player.getWidth(),
                                            player.getLocation().getY());

                                    previousLocation = newLocation;
                                    player.setLocation(newLocation);
                                    player.canRightCart = false;
                                    player.canDown = false;
                                    player.canRight = false;
                                    player.canDownCart = false;
                                    player.canLeft = false;
                                }
                                break;
                        }
                    }
                }
            }


        }
    }

    public void doHitDetection() {
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            ArrayList<Player> otherPlayers = gameState.getOtherPlayers(player);
            // Loop through all enemies to detect hit detection
            for (Iterator<Player> itr1 = otherPlayers.iterator(); itr1.hasNext(); ) {
                PhysicsObject e = itr1.next();
                // Attack Collision
                // if player is light attacking
                if (player.getCurrentAction() == Action.LIGHT) {
                    if (CollisionDetection.checkCollision(player.getAttackHitbox().getBoundsInParent(),
                            e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        // this will have to change due to Player being other controlled player when
                        // Enemy is when the player is an ai
                        Player enemy = (Player) e;
                        enemy.removeHealth(DamageCalculation.calculateDealtDamage(player, enemy), enemy);
                        player.setCurrentAction(Action.IDLE);
                        //System.out.println("hit");
                        // Sends to server
                    }

                }
                if (player.getCurrentAction() == Action.HEAVY) {
                    if (CollisionDetection.checkCollision(player.getHeavyAttackHitbox().getBoundsInParent(),
                            e.getBounds().getBoundsInParent())) {
                        // e takes damage
                        Player enemy = (Player) e;
                        // TODO: For now its takes 10 damage, change later
                        enemy.removeHealth(DamageCalculation.calculateDealtDamage(player, enemy), enemy);
                        player.setCurrentAction(Action.IDLE);
                        //System.out.println("heavy hit");
                        // Sends to server
                    }
                }
            }
        }
    }
}