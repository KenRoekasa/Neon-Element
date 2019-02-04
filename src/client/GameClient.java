package client;

import networking.client.ClientNetwork;

import java.util.Objects;

import client.ClientGameState;
import entities.CollisionDetection;
import entities.PhysicsObject;
import entities.PowerUp;

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
                    ((PowerUp) e).activatePowerUp();
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
    }


}
