package ai;

import java.util.ArrayList;

import entities.Character;
import entities.Enemy;
import entities.PowerUp;
import enums.PowerUpType;

public class EnemyFSM {

	public static void basicEnemyFetchAction(Enemy enemy, Character [] players, ArrayList<PowerUp> powerups) {

		float maxHP = enemy.getMAX_HEALTH();
		float enemyHP = enemy.getHealth();
		Character nearestPlayer = enemy.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();

		if(enemyHP< (maxHP/4) || (enemy.findNearestPowerUp(PowerUpType.HEAL) !=-1 && enemyHP<maxHP) ) {
			System.out.println("State find health");
			if(!enemy.isShielded())
				enemy.shield();
			enemy.setState(EnemyStates.FIND_HEALTH);
		}
		else if( enemyHP>playerHP || playerHP< (maxHP/2) ) {
			System.out.println("state Attack");
			enemy.setState(EnemyStates.ATTACK);
		}
		else if(playerHP< (maxHP/4) ) {
			//System.out.println("state aggressive attack");
			enemy.setState(EnemyStates.AGGRESSIVE_ATTACK);
		}
		else if( enemy.findNearestPowerUp(PowerUpType.DAMAGE) != -1 ) {
			System.out.println("state find damage");
			enemy.setState(EnemyStates.FIND_DAMAGE);
		}
		else if( enemy.findNearestPowerUp(PowerUpType.SPEED) != -1 ) {
			//System.out.println("state find speed");
			enemy.setState(EnemyStates.FIND_SPEED);
		}
		else {
			//System.out.println("ELSE , state attack");
			enemy.setState(EnemyStates.ATTACK);
		}

	}
	
	public static void advancedEnemyFetchAction(Enemy enemy, Character [] players, ArrayList<PowerUp> powerups) {

		float maxHP = enemy.getMAX_HEALTH();
		float enemyHP = enemy.getHealth();
		Character nearestPlayer = enemy.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();

		if(enemyHP< (maxHP/4) || (enemy.findNearestPowerUp(PowerUpType.DAMAGE) !=-1 && enemyHP<maxHP) ) {
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
		else if( enemy.findNearestPowerUp(PowerUpType.DAMAGE) !=-1 ) {
			enemy.setState(EnemyStates.FIND_DAMAGE);
		}
		else if( enemy.findNearestPowerUp(PowerUpType.SPEED) !=-1 ) {
			enemy.setState(EnemyStates.FIND_SPEED);
		}
		else {
			enemy.setState(EnemyStates.ATTACK);
		}

	}
	
}
