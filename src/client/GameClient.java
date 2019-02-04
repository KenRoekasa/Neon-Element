package client;

import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

import java.util.Objects;

import client.ClientGameState;
import entities.CollisionDetection;
import entities.PhysicsObject;
import entities.PowerUp;
import javafx.geometry.Point2D;

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
        // Collision detection code
        for (PhysicsObject e : gameState.getObjects()) {
            if (CollisionDetection.checkCollision(gameState.getPlayer(), e)) {
                //If the object is a power up
                if (Objects.equals(e.getClass(), PowerUp.class)) {
                    PowerUp powerUp = (PowerUp) e;
                    ((PowerUp) e).activatePowerUp(gameState.getPlayer());
                }else{
                    //The player has collided with e do something
                    gameState.getPlayer().getBounds().getBoundsInParent().getMaxX();
//                    System.out.println("x diff " + xDiff);
//                    System.out.println("y diff " + yDiff);
                    gameState.getPlayer().isColliding(e);
                }
            }else{
                gameState.getPlayer().isColliding = false;
            }
        }
        //Call update function for all physics objects
        gameState.getPlayer().update();
        for (PhysicsObject o : gameState.getObjects()) {
            o.update();
        }

        // Power up creation thread
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
