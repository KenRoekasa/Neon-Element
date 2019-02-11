package server;

import java.util.Iterator;

import entities.CollisionDetection;
import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import enums.ObjectType;
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
