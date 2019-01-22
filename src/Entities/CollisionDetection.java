package Entities;

import java.util.ArrayList;

public class CollisionDetection extends Thread{

    // The rectangles of the objects that can be collided with
    private ArrayList<Player> enemies;
    private PowerUp[] powerUps = new PowerUp[5];
    private Player player;


    public CollisionDetection(Player player, ArrayList<Player> enemies){
        this.player = player;
        this.enemies = enemies;
    }

    @Override
    public void run() {
        while(true){

            // Check hitbox against all other players
            for(Player p : enemies){
                if(player.getHitBox().intersects(p.getHitBox().getBoundsInParent())){
                        //Todo: What happens when the two players collide
                    System.out.println("Collsion");

                }
            }
            // Check if you are picking up power ups
//            for (PowerUp pickedUp : powerUps){
            ////                player.addPowerup(pickedUp);
            ////            }
        }
    }
}
