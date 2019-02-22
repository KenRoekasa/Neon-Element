package engine.ai;

import engine.entities.Character;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class AiFSM {

	public static void easyAiFetchAction(Player aiPlayer, AiController aiCon,AiCalculations calc) {

		float maxHP = aiPlayer.getMAX_HEALTH();
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take a heal power up
		
		if(  calc.powerupCloserThanPlayer() && aiPlayerHP<maxHP && calc.getNearestPowerUp(PowerUpType.HEAL) != -1 ) {//System.out.println("case 1");
			aiCon.setState(AiStates.FIND_HEALTH);
		}
	
		//case 2, run for your life
		else if(aiPlayerHP < (maxHP/10) || aiPlayerHP < playerHP) {//System.out.println("case 2");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, FINISH HIM
		else if (playerHP < (maxHP/3)) {//System.out.println("case 3");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}		
		
		//case 4, normal attacking
		else if ( calc.playerIsTooClose() || aiPlayerHP > playerHP  ) {
			//System.out.println("case 4\nplayer is too close: "+aiCon.playerIsTooClose()+"\naiHP > playerHP "+(aiPlayerHP>playerHP));
			aiCon.setState(AiStates.ATTACK);
		}
	
		//case5, take the power up on your way
		else if (calc.powerupIsTooClose()) {//System.out.println("case 5");
			switch(calc.getPowerups().get(calc.getNearestPowerUp()).getType()) {
			case DAMAGE:
				aiCon.setState(AiStates.FIND_DAMAGE);
				break;
			case HEAL:
				aiCon.setState(AiStates.FIND_HEALTH);
				break;
			case SPEED:
				aiCon.setState(AiStates.FIND_SPEED);
				break;
			}
		}
		
		//case 6, 'random action', either fix on one player and attack, or wander for 5 seconds
		else {//System.out.println("case 6");
			aiCon.setState(AiStates.WANDER);
		}
		//element gets changed randomly every 15 seconds

	}
	
	public static void normalAiFetchAction(Player aiPlayer, AiController aiCon, AiCalculations calc) {

		float maxHP = aiPlayer.getMAX_HEALTH();
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take any type of power up
		if( calc.powerupIsTooClose() || calc.powerupCloserThanPlayer() ) {
		//	System.out.println("case 1");
			switch(calc.getPowerups().get(calc.getNearestPowerUp()).getType()) {
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

	}
	
	public static void hardAiFetchAction(Player aiPlayer, AiController aiCon, AiCalculations calc) {

		float maxHP = aiPlayer.getMAX_HEALTH();
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
//		System.out.println("decide what case");
		
		//case 1, a power up is closer than an enemy
		
		if( calc.powerupCloserThanPlayer() ) {
		//	System.out.println("case 1");
			switch(calc.getPowerups().get(calc.getNearestPowerUp()).getType()) {
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
		
		else if (aiPlayerHP<(maxHP/3) && calc.getNearestPowerUp(PowerUpType.HEAL) != -1) {
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
		
		else if( calc.getNearestPowerUp(PowerUpType.DAMAGE) != -1 ) {
		//	System.out.println("case 5");
			aiCon.setState(AiStates.FIND_DAMAGE);
		}
		
		//case 6, there exist a speed power up
		
		else if( calc.getNearestPowerUp(PowerUpType.SPEED) != -1 ) {
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

	}
	
}
