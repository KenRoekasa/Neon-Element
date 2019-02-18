package engine.ai;

import engine.entities.Character;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class AiFSM {

	public static void basicAiFetchAction(Player aiPlayer, AiController aiCon) {

		float maxHP = aiPlayer.getMAX_HEALTH();
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = aiCon.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
//		System.out.println("decide what case");
		
		//case 1, a power up is closer than an enemy
		
		if( aiCon.powerupCloserThanPlayer() ) {
		//	System.out.println("case 1");
			switch(aiCon.getPowerups().get(aiCon.findNearestPowerUp()).getType()) {
			case DAMAGE:
				aiCon.setState(AiStates.FIND_DAMAGE);
				break;
			case HEAL:
				aiCon.setState(AiStates.FIND_HEALTH);
				break;
			case SPEED:
				aiCon.setState(AiStates.FIND_SPEED);
				break;
			default:
				break;
			}
		}
		
		//case 2, the engine.ai player's hp is less than 33% and a health power up is available
		
		else if (aiPlayerHP<(maxHP/3) && aiCon.findNearestPowerUp(PowerUpType.HEAL) != -1) {
		//	System.out.println("case 2");
			aiCon.setState(AiStates.FIND_HEALTH);
		}
		
		//case 3, the engine.ai player's hp is less than 33% and a health power up is not available
		
		else if(aiPlayerHP < (maxHP/2) ) {
		//	System.out.println("case 3");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 4, the nearest enemy's hp is less than 33%
		
		else if (playerHP < (maxHP/3)) {
		//	System.out.println("case 4");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}
		
		//case 5, there exist a damage power up
		
		else if( aiCon.findNearestPowerUp(PowerUpType.DAMAGE) != -1 ) {
		//	System.out.println("case 5");
			aiCon.setState(AiStates.FIND_DAMAGE);
		}
		
		//case 6, there exist a speed power up
		
		else if( aiCon.findNearestPowerUp(PowerUpType.SPEED) != -1 ) {
		//	System.out.println("case 6");
			aiCon.setState(AiStates.FIND_SPEED);
		}
		
		//case 7, the nearest enemy's hp is less than engine.ai player's hp
		
		else if (aiPlayerHP > playerHP) {
		//	System.out.println("case 7");
			aiCon.setState(AiStates.ATTACK);
		}
		
		//else 'when the enemy's hp is more than the engine.ai player's or equal to it"
		
		else {
		// System.out.println("case 8");
			aiCon.setState(AiStates.ATTACK);
		}
		//System.out.println("engine.ai health: "+aiPlayer.getHealth());
		aiCon.changeToBefittingElement();

	}
	
//	public static void advancedEnemyFetchAction(Player enemy, Character [] players, ArrayList<PowerUp> powerups) {
//
//		float maxHP = enemy.getMAX_HEALTH();
//		float enemyHP = enemy.getHealth();
//		Character nearestPlayer = enemy.findNearestPlayer();
//		float playerHP = nearestPlayer.getHealth();
//
//		if(enemyHP< (maxHP/4) || (enemy.findNearestPowerUp(PowerUpType.DAMAGE) !=-1 && enemyHP<maxHP) ) {
//			if(!enemy.isShielded())
//				enemy.shield();
//			enemy.setState(EnemyStates.FIND_HEALTH);
//		}
//		else if( enemyHP>playerHP || playerHP< (maxHP/2) ) {
//			enemy.setState(EnemyStates.ATTACK);
//		}
//		else if(playerHP< (maxHP/4) ) {
//			enemy.setState(EnemyStates.AGGRESSIVE_ATTACK);
//		}
//		else if( enemy.findNearestPowerUp(PowerUpType.DAMAGE) !=-1 ) {
//			enemy.setState(EnemyStates.FIND_DAMAGE);
//		}
//		else if( enemy.findNearestPowerUp(PowerUpType.SPEED) !=-1 ) {
//			enemy.setState(EnemyStates.FIND_SPEED);
//		}
//		else {
//			enemy.setState(EnemyStates.ATTACK);
//		}
//
//	}
	
}
