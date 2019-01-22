package Entities;

import java.awt.*;

public class CollisionDetection extends Thread{

    // The rectangles of the objects that can be collided with
    private Player[] otherPlayers;
    private PowerUp[] powerUps;
    private Player player;


    public CollisionDetection(Player player){
        this.player = player;
    }

    @Override
    public void run() {
        while(true){
            //keep changing the position of the hitbox
            player.updateHitbox();
            // Check hitbox against all other players
            for(Player p : otherPlayers){
                if(player.getHitBox().intersects(p.getHitBox().getBoundsInParent())){
                        //Todo: What happens when the two players collide
                }
            }
            // Check if you are picking up power ups
            for (PowerUp pickedUp : powerUps){
                player.addPowerup(pickedUp);
            }
        }
    }
}
