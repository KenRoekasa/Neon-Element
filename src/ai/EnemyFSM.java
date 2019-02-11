package ai;

import java.util.ArrayList;

import entities.Character;
import entities.Enemy;
import entities.PhysicsObject;
import entities.PowerUp;
import enums.PowerUpType;

public class EnemyFSM {

	public static void basicEnemyFetchAction(Enemy enemy, Character [] players, ArrayList<PowerUp> powerups) {

		float maxHP = enemy.getMAX_HEALTH();
		float enemyHP = enemy.getHealth();
		Character nearestPlayer = enemy.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();

		if(enemyHP< (maxHP/4) || (enemy.findNearestPowerUp(PowerUpType.DAMAGE) !=null && enemyHP<maxHP) ) {
			if(!enemy.isShielded())
				enemy.shield();
			enemy.setState(EnemyStates.FIND_HEALTH);
		}
		else if( enemyHP>playerHP || playerHP< (maxHP/2) ) {
			enemy.setState(EnemyStates.ATTACK);
		}
		else if(playerHP< (maxHP/4) ) {
			enemy.setState(EnemyStates.AGGRESSIVE_ATTACK);
		}
		else if( enemy.findNearestPowerUp(PowerUpType.DAMAGE) != null ) {
			enemy.setState(EnemyStates.FIND_DAMAGE);
		}
		else if( enemy.findNearestPowerUp(PowerUpType.SPEED) != null ) {
			enemy.setState(EnemyStates.FIND_SPEED);
		}
		else {
			//enemy.setState(EnemyStates.ATTACK);
		}

	}
	
	public static void advancedEnemyFetchAction(Enemy enemy, Character [] players, ArrayList<PowerUp> powerups) {

		float maxHP = enemy.getMAX_HEALTH();
		float enemyHP = enemy.getHealth();
		Character nearestPlayer = enemy.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();

		if(enemyHP< (maxHP/4) || (enemy.findNearestPowerUp(PowerUpType.DAMAGE) !=null && enemyHP<maxHP) ) {
			if(!enemy.isShielded())
				enemy.shield();
			enemy.setState(EnemyStates.FIND_HEALTH);
		}
		else if( enemyHP>playerHP || playerHP< (maxHP/2) ) {
			enemy.setState(EnemyStates.ATTACK);
		}
		else if(playerHP< (maxHP/4) ) {
			enemy.setState(EnemyStates.AGGRESSIVE_ATTACK);
		}
		else if( enemy.findNearestPowerUp(PowerUpType.DAMAGE) !=null ) {
			enemy.setState(EnemyStates.FIND_DAMAGE);
		}
		else if( enemy.findNearestPowerUp(PowerUpType.SPEED) !=null ) {
			enemy.setState(EnemyStates.FIND_SPEED);
		}
		else {
			enemy.setState(EnemyStates.ATTACK);
		}

	}
	
}
