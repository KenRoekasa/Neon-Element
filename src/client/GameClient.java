package client;

import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

import java.util.Iterator;
import java.util.Objects;

import client.ClientGameState;
import entities.CollisionDetection;
import entities.PhysicsObject;
import entities.PowerUp;
import enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class GameClient extends Thread {

    private ClientGameState gameState;
    private ClientNetwork network;

    private boolean running;

    public GameClient(ClientGameState gameState) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState);
    }

    public void run() {
        this.running = true;

        while(this.running) {
            this.doCollisionDetection();
            this.doLocationState();
            
            Thread.yield();
        }

        this.network.close();
    }
    
    private void doCollisionDetection() {
        synchronized (gameState.getObjects()) {
            // Collision detection code
            synchronized (gameState.getObjects()) {
                for (Iterator<PhysicsObject> itr = gameState.getObjects().iterator(); itr.hasNext(); ) {
                    PhysicsObject e = itr.next();
                    if (CollisionDetection.checkCollision(gameState.getPlayer(), e)) {
                        //If the object is a power up
                        if (e.getTag() == ObjectType.POWERUP) {
                            PowerUp powerUp = (PowerUp) e;
                            ((PowerUp) e).activatePowerUp(gameState.getPlayer());
                            // remove power up from objects array list
                            itr.remove();
                        } else {
                            //The player has collided with e do something
                            gameState.getPlayer().getBounds().getBoundsInParent().getMaxX();
                            gameState.getPlayer().isColliding(e);
                        }
                    } else {
                        gameState.getPlayer().isColliding = false;
                    }
                    //Attack Collision
                    //if player is attacking check
                    Rectangle attackHitbox = new Rectangle(gameState.getPlayer().getLocation().getX(), gameState.getPlayer().getLocation().getY()+gameState.getPlayer().getWidth(), gameState.getPlayer().getWidth(), gameState.getPlayer().getWidth());
                    Rotate rotate = (Rotate) Rotate.rotate(gameState.getPlayer().getPlayerAngle().getAngle(), gameState.getPlayer().getLocation().getX(), gameState.getPlayer().getLocation().getY());
                    attackHitbox.getTransforms().addAll(rotate);
                    if(CollisionDetection.checkCollision(attackHitbox.getBoundsInParent(),e.getBounds().getBoundsInParent())){
                        // e takes damage
                    }

                }
            }


            //Call update function for all physics objects
            gameState.getPlayer().update();
            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }
    }
    
    private void doLocationState() {
        Point2D location = this.gameState.getPlayer().getLocation();
        double x = location.getX();
        double y = location.getY();

        this.getDispatcher().sendLocationState(x, y);
    }
    
    private ClientNetworkDispatcher getDispatcher() {
        return this.network.getDispatcher();
    }


}
