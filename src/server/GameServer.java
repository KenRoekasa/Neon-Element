package server;

import static enums.Directions.LEFTCART;

import java.util.ArrayList;
import java.util.Iterator;

import entities.CollisionDetection;
import entities.Enemy;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import enums.Action;
import enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import networking.server.ServerNetwork;
import server.ServerGameState;
import server.controllers.PowerUpController;

public class GameServer extends Thread {

    private ServerGameState gameState;
    private ServerNetwork network;

    private boolean running;

    public GameServer(ServerGameState gameState) {
        this.gameState = gameState;
        this.network = new ServerNetwork(this.gameState);
    }

    public void run() {
        this.running = true;

        Thread powerUpController = new Thread(new PowerUpController(gameState.getObjects(), this.network.getDispatcher()));
        powerUpController.start();

        while(this.running) {
            // Server logic
            this.doCollisionDetection();
        }

        this.network.close();
    }

    private void doCollisionDetection() {

        ArrayList<PhysicsObject> objects = gameState.getObjects();
        synchronized (objects) {
            // Collision detection code
            Player projectedPlayer = new Player(ObjectType.PLAYER);
            for (Iterator<Player> playerItr = gameState.getPlayers().iterator(); playerItr.hasNext(); ) {
                Player player = playerItr.next();
                player.canUp = true;
                player.canLeft = true;
                player.canRight = true;
                player.canDown = true;
                player.canUpCart = true;
                player.canRightCart = true;
                player.canLeftCart = true;
                player.canDownCart = true;
                for (Iterator<PhysicsObject> itr = objects.iterator(); itr.hasNext(); ) {
                    PhysicsObject e = itr.next();
                    // Check if the moving in a certain direction will cause a collision
                    // The player has collided with e do something
                    if (e.getTag() == ObjectType.POWERUP) {
                        if (CollisionDetection.checkCollision(player, e)) {
                            PowerUp powerUp = (PowerUp) e;
                            powerUp.activatePowerUp(player);
                            // remove power up from objects array list
                            itr.remove();
    
                        }
                    } else {
                        double x = player.getLocation().getX();
                        double y = player.getLocation().getY();
                        int movementSpeed = player.getMovementSpeed();
                        Point2D checkUp = new Point2D(x - movementSpeed, y - movementSpeed);
                        Point2D checkDown = new Point2D(x + movementSpeed, y + movementSpeed);
                        Point2D checkLeft = new Point2D(x - movementSpeed, y + movementSpeed);
                        Point2D checkRight = new Point2D(x + movementSpeed, y - movementSpeed);
                        Point2D checkUpCart = new Point2D(x, y - movementSpeed);
                        Point2D checkDownCart = new Point2D(x, y + movementSpeed);
                        Point2D checkLeftCart = new Point2D(x - movementSpeed, y);
                        Point2D checkRightCart = new Point2D(x + movementSpeed, y);
                        Point2D[] projectedLocations = {checkUp, checkDown, checkLeft, checkRight, checkUpCart, checkDownCart, checkLeftCart, checkRightCart};
    
                        System.out.println(player.getCharacterDirection());
    
                        switch (player.getCharacterDirection()) {
                            case UP:
                                projectedPlayer.setLocation(checkUp);
                                if (CollisionDetection.checkCollision(projectedPlayer, e)) {
                                    player.canUp = false;
                                    player.canUpCart = false;
                                    player.canLeftCart = false;
                                }
    
                                break;
                            case DOWN:
                                projectedPlayer.setLocation(checkDown);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    player.canDown = false;
                                    player.canDownCart = false;
                                    player.canRightCart = false;
                                }
                                break;
                            case LEFT:
                                projectedPlayer.setLocation(checkLeft);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    player.canLeft = false;
                                    player.canDownCart = false;
                                    player.canLeftCart = false;
                                    player.canRightCart = false;
    
                                }
    
                                break;
                            case UPCART:
                                projectedPlayer.setLocation(checkUpCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    player.canUpCart = false;
                                    player.canUp = false;
                                    player.canRight = false;
                                    player.canLeftCart = false;
                                }
                                break;
                            case DOWNCART:
                                projectedPlayer.setLocation(checkDownCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    player.canDownCart = false;
                                    player.canDown = false;
                                    player.canLeft = false;
                                    player.canRightCart= false;
                                }
                                break;
                            case LEFTCART:
                                projectedPlayer.setLocation(checkLeftCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    player.canLeftCart = false;
                                    player.canUp = false;
                                    player.canLeft = false;
                                    player.canUpCart=false;
                                }
                                break;
                            case RIGHTCART:
                                projectedPlayer.setLocation(checkRightCart);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    player.canRightCart = false;
                                    player.canDown = false;
                                    player.canRight = false;
                                    player.canDownCart = false;
                                    player.canLeft=false;
                                }
                                break;
                            case RIGHT:
                                projectedPlayer.setLocation(checkRight);
                                if ((CollisionDetection.checkCollision(projectedPlayer, e))) {
                                    player.canRight = false;
                                    player.canDown = false;
                                    player.canUpCart = false;
                                    player.canRightCart = false;
                                }
                                break;
                        }
                    }
                }


                // Loop through all enemies to detect hit detection
                ArrayList<Enemy> enemies = gameState.getAis();
                synchronized (enemies) {
                    for (Iterator<Enemy> itr = enemies.iterator(); itr.hasNext(); ) {
                        PhysicsObject e = itr.next();
                        //Attack Collision
                        //if player is light attacking
                        if (player.getCurrentAction() == Action.LIGHT) {
                            if (CollisionDetection.checkCollision(player.getAttackHitbox().getBoundsInParent(), e.getBounds().getBoundsInParent())) {
                                // e takes damage
                                // this will have to change due to Player being other controlled player when Enemy is when the player is an ai
                                Enemy enemy = (Enemy) e;
                                // TODO: For now its takes 3 damage, change later
                                enemy.removeHealth(3);
                                player.setCurrentAction(Action.IDLE);
                                System.out.println("hit");
                                // Sends to server
                            }

                        }
                        if (player.getCurrentAction() == Action.HEAVY) {
                            if (CollisionDetection.checkCollision(player.getHeavyAttackHitbox().getBoundsInParent(), e.getBounds().getBoundsInParent())) {
                                // e takes damage
                                Enemy enemy = (Enemy) e;
                                // TODO: For now its takes 10 damage, change later
                                enemy.removeHealth(10);
                                player.setCurrentAction(Action.IDLE);
                                System.out.println("heavy hit");
                                // Sends to server
                            }
                        }
                    }
                }
            }
        }
    }

    private void doUpdates() {
        synchronized (gameState.getObjects()) {
            //Call update function for all physics objects
            for (Player p : gameState.getPlayers()) {
                p.update();
            }
            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }
    }
    
    private void doCollisionDetection2() {
        synchronized (gameState.getObjects()) {
            // Collision detection code
            synchronized (gameState.getObjects()) {
                for (Iterator<Player> playerItr = gameState.getPlayers().iterator(); playerItr.hasNext(); ) {
                    Player player = playerItr.next();
                    for (Iterator<PhysicsObject> itr = gameState.getObjects().iterator(); itr.hasNext(); ) {
                        PhysicsObject e = itr.next();
                        if (CollisionDetection.checkCollision(player, e)) {
                            //If the object is a power up
                            if (e.getTag() == ObjectType.POWERUP) {
                                PowerUp powerUp = (PowerUp) e;
                                ((PowerUp) e).activatePowerUp(player);
                                // remove power up from objects array list
                                itr.remove();
                            } else {
                                //The player has collided with e do something
                                player.getBounds().getBoundsInParent().getMaxX();
                                player.isColliding(e);
                            }
                        } else {
                            player.isColliding = false;
                        }
                        //Attack Collision
                        //if player is attacking check
                        Rectangle attackHitbox = new Rectangle(player.getLocation().getX(), player.getLocation().getY()+player.getWidth(), player.getWidth(), player.getWidth());
                        Rotate rotate = (Rotate) Rotate.rotate(player.getPlayerAngle().getAngle(), player.getLocation().getX(), player.getLocation().getY());
                        attackHitbox.getTransforms().addAll(rotate);
                        if(CollisionDetection.checkCollision(attackHitbox.getBoundsInParent(),e.getBounds().getBoundsInParent())){
                            // e takes damage
                        }
                    }
                }
            }


            //Call update function for all physics objects
            for (Player p : gameState.getPlayers()) {
                p.update();
            }
            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }
    }

}
