package Entities;

import java.awt.*;

public class CollisionDetection implements Runnable{

    // The rectangles of the objects that can be collided with
    private Player[] otherPlayers;
    private Player player;


    public CollisionDetection(Player player){
        this.player = player;
    }


    @Override
    public void run() {
        while(true){
            player.updateHitbox();
            // Check hitbox against all other players
            for(Player p : otherPlayers){
                if(player.getHitBox().intersects(p.getHitBox().getBoundsInParent())){

                }
            }
        }
    }
}
