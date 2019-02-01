package entities;


import ai.BasicEnemyFSM;
import ai.BasicEnemyStates;
import entities.Character;
import entities.PowerUp;
import java.util.Random;


public class BasicEnemy extends Character {
/*
    BasicEnemyStates activeState;
    Character [] players;
    PowerUp [] powerUps;
    BasicEnemy enemy  = this;
    int powerupIndex = -1;
    
    public BasicEnemy(Character [] players, PowerUp [] powerUps) {

        activeState = BasicEnemyStates.IDLE;
        assignRandomElement();
        this.players = players;
        this.powerUps = powerUps;

    }

    public void start() {


        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                boolean bool = true;

                while (bool) {
                	
                	BasicEnemyFSM.fetchAction(enemy, players, powerUps);
                	
                	executeAction();
                    if (health <= 0)
                        bool = false;
                }
            }

        });

    }


    void assignRandomElement() {

        Random r = new Random();
        int rand = r.nextInt(4);
        switch (rand) {
            case 0:
                changeToEarth();
                break;
            case 1:
                changeToFire();
                break;
            case 2:
                changeToWater();
                break;
            case 3:
                changeToAir();
                break;
        }
    }


    public BasicEnemyStates getActiveState() {
        return activeState;
    }

    public void setState(BasicEnemyStates s) {
        activeState = s;
    }
    public void setPowerUpIndex(int i) {
    	powerupIndex = i;
    }
    public void executeAction() {
    	//TODO: implement this and then functions for each case
    	BasicEnemyStates newState = null;
    	switch(activeState) {
    	case ATTACK : break;
    	case AGGRESSIVE_ATTACK: break;
    	case FIND_HEALTH: break;
    	case FIND_DAMAGE: break;
    	case FIND_SPEED: break;
    	}
    }
    //basic enemy actions
    */
@Override
public void update() {

}


}
