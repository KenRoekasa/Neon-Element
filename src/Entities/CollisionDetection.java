package Entities;

import java.util.ArrayList;

public class CollisionDetection extends Thread{

    // The rectangles of the objects that can be collided with
    private ArrayList<Player> players;
    private PowerUp[] powerUps = new PowerUp[5];
    private ArrayList<Player> player;


    public CollisionDetection(Player player, ArrayList<Player> players){
        this.player = players;
    }



    @Override
    public void run() {
        while(true){
            // Check hitbox against all other players
            for(Player p : enemies){
                if(player.getHitBox().intersects(p.getHitBox().getBoundsInParent())){
                    //Todo: What happens when the two players collide
                    System.out.println("Collision");

                }
            }
        }
    }
}
