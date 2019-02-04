package entities;

import ai.EnemyFSM;

public class AdvancedEnemy extends Enemy{

	public AdvancedEnemy(Character[] players, PowerUp[] powerUps) {
		super(players, powerUps);
	}
	
	@Override
	public void start() {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                boolean bool = true;

                while (bool) {

                	EnemyFSM.advancedEnemyFetchAction(enemy, players, powerups);

                	executeAction();
                	
                    if (health <= 0)
                        bool = false;
                }
            }

        });
		
	}

	@Override
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
