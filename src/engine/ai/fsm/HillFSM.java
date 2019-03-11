package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiStates;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class HillFSM extends FSM{
	
	public HillFSM(Player aiPlayer, AiController aiCon,AiCalculations calc) {
		super(aiPlayer, aiCon, calc);
	}
	
	@Override
	public void easyAiFetchAction() {
		
		boolean debug = false;

		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//if there is a power up take it
		if (calc.powerupIsTooClose()) {
			if(debug)
				System.out.println("case 1");
			switch(calc.getPowerups().get(calc.getNearestPowerUp()).getType()) {
			case DAMAGE:
				aiCon.setState(AiStates.FIND_DAMAGE);
				break;
			case HEAL:
				if(aiPlayerHP<maxHP)
					aiCon.setState(AiStates.FIND_HEALTH);
				else
					aiCon.setState(AiStates.GO_TO_HILL);
				break;
			case SPEED:
				aiCon.setState(AiStates.FIND_SPEED);
				break;
			}
		}
		
		//case 2, normal attacking
		else if ( calc.playerIsTooClose() && aiPlayerHP > playerHP && calc.closeToHill()) {
			if(debug)
				System.out.println("case 2");
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 3, go to hill
		else if(calc.playerIsTooClose() && calc.onHillEdge() && aiPlayerHP > (maxHP/3)) {
			if(debug)
				System.out.println("case 3");
			if(calc.onHill(aiPlayer.getLocation()))
				aiCon.setState(AiStates.IDLE);
			else 
				aiCon.setState(AiStates.GO_TO_HILL);
		}
		
		//case 4, stay on hill
		else if( !calc.onHill(aiPlayer.getLocation()) && aiPlayerHP > maxHP/3 && !(playerHP < (maxHP/3)) ) {
			if(debug)
				System.out.println("case 4");
			aiCon.setState(AiStates.GO_TO_HILL);
		}
	
		//case 5, look for health power up 
		else if(aiPlayerHP < (maxHP/3) &&  aiPlayerHP < playerHP ) {
			if(debug)
				System.out.println("case 5");
			if(calc.powerUpExist(PowerUpType.HEAL)) {
				if(debug)
					System.out.println("\tfind health");
				aiCon.setState(AiStates.FIND_HEALTH);
			}
			else if(calc.playerIsTooClose() && calc.onHill(nearestPlayer.getLocation())) {
				if(debug)
					System.out.println("\twander");
				aiCon.setState(AiStates.WANDER);
			}
			else if(calc.playerIsTooClose()) {
				if(debug)
					System.out.println("\tplayer is too close");
				aiCon.setState(AiStates.ESCAPE);
			}
			else {
				if(debug)
					System.out.println("\tescape on hill");
				aiCon.setState(AiStates.WANDER_ON_HILL);
			}
		}

		//case 6, FINISH HIM
		else if (playerHP < (maxHP/3)) {
			if(debug)
				System.out.println("case 6");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}		

	}
	
	@Override
	public void normalAiFetchAction() {
		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		Player onHillPlayer =  calc.getOnHillPlayer();
		float nearestPlayerHP = nearestPlayer.getHealth();
		
		boolean debug = true;
		
		//case 1, stay on hill	
		if( !calc.onHill(aiPlayer.getLocation()) && !calc.isCharging(nearestPlayer) && !calc.isCharging(onHillPlayer) &&
				aiPlayerHP > (maxHP/4) && !calc.powerupIsTooClose() && !(aiPlayerHP > nearestPlayerHP &&
						calc.playerIsTooClose()) && ((!(calc.playerIsTooClose() || aiPlayerHP > nearestPlayerHP )) && !calc.closeToHill() ) ) {
			if(debug)
				System.out.println("case 1");
			aiCon.setState(AiStates.GO_TO_HILL);
		}
	
		//case 2, look for health power up 
		else if(aiPlayerHP < (maxHP/4) ) {
			if(debug)
				System.out.println("case 2");
			if(calc.powerUpExist(PowerUpType.HEAL))
				aiCon.setState(AiStates.FIND_HEALTH);
			else
				aiCon.setState(AiStates.ESCAPE_ON_HILL);
		}
		
		//case 3, 
		else if (calc.isCharging(nearestPlayer)) {
			if(debug)
				System.out.println("case 3");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 4, FINISH HIM
		else if (nearestPlayerHP < (maxHP/3) && aiPlayerHP > maxHP/3) {
			if(debug)
				System.out.println("case 4");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}	
	
		//case 5, take the power up on your way
		else if (calc.powerupIsTooClose()) {
			if(debug)
				System.out.println("case 5");
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
		
		//case 6, 
		else if (aiPlayerHP < nearestPlayerHP ) {
			if(debug)
				System.out.println("case 6");
			aiCon.setState(AiStates.ESCAPE_ON_HILL);
		}
		
		//case 7, 'scaring off' normal attacking
		else if ( calc.playerIsTooClose() || aiPlayerHP > nearestPlayerHP  ) {
			if(debug)
				System.out.println("case 7");
			aiCon.setState(AiStates.ATTACK);
		}

		//case 8, 
		else if(calc.playerIsTooClose() && calc.onHillEdge() && calc.onHill(aiPlayer.getLocation())) {
			if(debug)
				System.out.println("case 8");
			aiCon.setState(AiStates.IDLE);
		}	
	}
	
	@Override
	public void hardAiFetchAction() {
		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		Player onHillPlayer =  calc.getOnHillPlayer();
		float onHillPlayerHP = onHillPlayer.getHealth();
		float nearestPlayerHP = nearestPlayer.getHealth();
		
		//case 1, stay on hill
		
		if( !calc.onHill(aiPlayer.getLocation()) && !calc.isCharging(nearestPlayer) && !calc.isCharging(onHillPlayer) && 
				aiPlayerHP > (maxHP/4) && !calc.powerupIsTooClose() && !(aiPlayerHP > nearestPlayerHP &&
						calc.playerIsTooClose()) ){//System.out.println("case 1");
			aiCon.setState(AiStates.GO_TO_HILL);
		}

		//case 2, look for health power up 
		else if(aiPlayerHP < (maxHP/4)) {//System.out.println("case 2");
			if(calc.getNearestPowerUp(PowerUpType.HEAL) != -1)
				aiCon.setState(AiStates.FIND_HEALTH);
			else
				aiCon.setState(AiStates.ESCAPE_ON_HILL);
		}
		
		else if (calc.isCharging(nearestPlayer)) {
			aiCon.setState(AiStates.ESCAPE);
		}
		
		else if(calc.playerIsTooClose() && calc.onHillEdge() && calc.onHill(aiPlayer.getLocation()) && !calc.powerupIsTooClose() && !(onHillPlayerHP < (maxHP/3) && aiPlayerHP > maxHP/3)) {
			aiCon.setState(AiStates.IDLE);
		}	
		
		//case 3, FINISH HIM
		else if (onHillPlayerHP < (maxHP/3) && aiPlayerHP > maxHP/3) {//System.out.println("case 3");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}		
		
		//case 4, normal attacking
		else if ( onHillPlayerHP - aiPlayerHP < maxHP/5  ) {
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
		
//		//case 6, wander for 5 seconds
//		else {//System.out.println("case 6");
//			aiCon.setState(AiStates.WANDER_ON_HILL);
//		}

	}

}
