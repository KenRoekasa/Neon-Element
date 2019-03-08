package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiStates;
import engine.entities.Character;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class HillFSM extends FSM{
	
	public HillFSM(Player aiPlayer, AiController aiCon,AiCalculations calc) {
		super(aiPlayer, aiCon, calc);
	}
	
	@Override
	public void easyAiFetchAction() {

		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//if there is a power up take it
		if (calc.powerupIsTooClose()) {//System.out.println("case 5");
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
		
		//case 2, stay on hill
		
		else if( !calc.onHill(aiPlayer.getLocation()) ) {//System.out.println("case 1");
			aiCon.setState(AiStates.GO_TO_HILL);
		}
	
		//case 3, look for health power up 
		else if(aiPlayerHP < (maxHP/2)) {//System.out.println("case 2");
			if(calc.getNearestPowerUp(PowerUpType.HEAL) != -1)
				aiCon.setState(AiStates.FIND_HEALTH);
			else
				aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 4, FINISH HIM
		else if (playerHP < (maxHP/3)) {//System.out.println("case 3");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}		
		
		//case 5, normal attacking
		else if ( calc.playerIsTooClose() || aiPlayerHP > playerHP  ) {
			//System.out.println("case 4\nplayer is too close: "+aiCon.playerIsTooClose()+"\naiHP > playerHP "+(aiPlayerHP>playerHP));
			aiCon.setState(AiStates.ATTACK);
		}
	
		//case 6, take the power up on your way
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
		
		//case 7, wander for 5 seconds
		else {//System.out.println("case 6");
			aiCon.setState(AiStates.WANDER_ON_HILL);
		}
		//element gets changed randomly every 15 seconds

	}
	
	@Override
	public void normalAiFetchAction() {
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, stay on hill
		
		if( !calc.onHill(aiPlayer.getLocation()) ) {//System.out.println("case 1");
			aiCon.setState(AiStates.GO_TO_HILL);
		}
	
		//case 2, look for health power up 
		else if(aiPlayerHP < (maxHP/4)) {//System.out.println("case 2");
			if(calc.getNearestPowerUp(PowerUpType.HEAL) != -1)
				aiCon.setState(AiStates.FIND_HEALTH);
			else
				aiCon.setState(AiStates.ESCAPE_ON_HILL);
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
			aiCon.setState(AiStates.WANDER_ON_HILL);
		}
		
		//switches elements to maximize damage given and minimize damage received

	}
	
	@Override
	public void hardAiFetchAction() {
		float aiPlayerHP = aiPlayer.getHealth();
		Character nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, stay on hill
		
		if( !calc.onHill(aiPlayer.getLocation()) ) {//System.out.println("case 1");
			aiCon.setState(AiStates.GO_TO_HILL);
		}
	
		//case 2, look for health power up 
		else if(aiPlayerHP < (maxHP/4)) {//System.out.println("case 2");
			if(calc.getNearestPowerUp(PowerUpType.HEAL) != -1)
				aiCon.setState(AiStates.FIND_HEALTH);
			else
				aiCon.setState(AiStates.ESCAPE_ON_HILL);
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
			aiCon.setState(AiStates.WANDER_ON_HILL);
		}
		//System.out.println("ai health: "+aiPlayer.getHealth());

	}

}
