package engine.ai;

import engine.entities.Character;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class AiFSM {

	public static void easyAiFetchAction(Player aiPlayer, AiController aiCon) {

		float maxHP = aiPlayer.getMAX_HEALTH();
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = aiCon.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take a heal power up
		if(  aiCon.powerupCloserThanPlayer() && aiPlayerHP<maxHP && aiCon.findNearestPowerUp(PowerUpType.HEAL) != -1 ) {
			aiCon.setState(AiStates.FIND_HEALTH);
		}
		
		//case 2, run for your life
		else if(aiPlayerHP < (maxHP/10) ) {
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, FINISH HIM
		else if (playerHP < (maxHP/3)) {
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}		
		
		//case 4, normal attacking
		else if (aiPlayerHP > playerHP) {
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 5, 'random action', either fix on one player and attack, or wander for 5 seconds
		else {
			aiCon.setState(AiStates.WANDER);
		}
		//element gets changed randomly every 15 seconds

	}
	
	public static void mediumAiFetchAction(Player aiPlayer, AiController aiCon) {

		float maxHP = aiPlayer.getMAX_HEALTH();
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = aiCon.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take any type of power up
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
		
		//case 2, escape when health is less than third
		else if(aiPlayerHP < (maxHP/3) ) {
		//	System.out.println("case 2");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, attack aggressively when the nearest enemy's hp is less than 33%
		else if (playerHP < (maxHP/3)) {
		//	System.out.println("case 3");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}
		
		//case 4, attack when you got the advantage
		else if (aiPlayerHP > playerHP) {
		//	System.out.println("case 4");
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 5, wander, or attack, when nothing else is triggered
		else {
		// System.out.println("case 5");
			aiCon.setState(AiStates.WANDER);
		}
		
		//switches elements to maximize damage given and minimize damage received
		aiCon.changeToBefittingElement();

	}
	
	public static void hardAiFetchAction(Player aiPlayer, AiController aiCon) {

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
	
}
