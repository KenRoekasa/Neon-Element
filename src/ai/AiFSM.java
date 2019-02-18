package ai;

import entities.Character;
import entities.Player;
import enums.PowerUpType;

public class AiFSM {

	public static void easyAiFetchAction(Player aiPlayer, AiController aiCon) {

		float maxHP = aiPlayer.getMAX_HEALTH();
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = aiCon.findNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, find a power up
		if( aiCon.powerupCloserThanPlayer() ) {
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
		
		//case 2, run for your life
		else if(aiPlayerHP < (maxHP/4) ) {
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
		
		//case 2, the ai player's hp is less than 33% and a health power up is available
		
		else if (aiPlayerHP<(maxHP/3) && aiCon.findNearestPowerUp(PowerUpType.HEAL) != -1) {
		//	System.out.println("case 2");
			aiCon.setState(AiStates.FIND_HEALTH);
		}
		
		//case 3, the ai player's hp is less than 33% and a health power up is not available 
		
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
		
		//case 7, the nearest enemy's hp is less than ai player's hp
		
		else if (aiPlayerHP > playerHP) {
		//	System.out.println("case 7");
			aiCon.setState(AiStates.ATTACK);
		}
		
		//else 'when the enemy's hp is more than the ai player's or equal to it"
		
		else {
		// System.out.println("case 8");
			aiCon.setState(AiStates.ATTACK);
		}
		//System.out.println("ai health: "+aiPlayer.getHealth());
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
		
		//case 2, the ai player's hp is less than 33% and a health power up is available
		
		else if (aiPlayerHP<(maxHP/3) && aiCon.findNearestPowerUp(PowerUpType.HEAL) != -1) {
		//	System.out.println("case 2");
			aiCon.setState(AiStates.FIND_HEALTH);
		}
		
		//case 3, the ai player's hp is less than 33% and a health power up is not available 
		
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
		
		//case 7, the nearest enemy's hp is less than ai player's hp
		
		else if (aiPlayerHP > playerHP) {
		//	System.out.println("case 7");
			aiCon.setState(AiStates.ATTACK);
		}
		
		//else 'when the enemy's hp is more than the ai player's or equal to it"
		
		else {
		// System.out.println("case 8");
			aiCon.setState(AiStates.ATTACK);
		}
		//System.out.println("ai health: "+aiPlayer.getHealth());
		aiCon.changeToBefittingElement();

	}
	
}
