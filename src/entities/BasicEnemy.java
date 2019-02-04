package entities;


import ai.EnemyFSM;
import entities.Character;
import entities.PowerUp;


public class BasicEnemy extends Enemy {

    public BasicEnemy(Character[] players, PowerUp[] powerUps) {
		super(players, powerUps);
	}

	public void start() {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                boolean bool = true;

                while (bool) {

                	EnemyFSM.basicEnemyFetchAction(enemy, players, powerups);

                	executeAction();
                	
                    if (health <= 0)
                        bool = false;
                }
            }

        });
    }

    public void executeAction() {
    	switch(activeState) {
    	case ATTACK :attack(); break;
    	case AGGRESSIVE_ATTACK: aggressiveAttack(); break;
    	case FIND_HEALTH: findHealth(); break;
    	case FIND_DAMAGE: findDamage(); break;
    	case FIND_SPEED: findSpeed(); break;
    	}
    }

}
