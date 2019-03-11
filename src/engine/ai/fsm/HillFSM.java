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
			else if(calc.playerIsTooClose()) {
				if(debug)
					System.out.println("\tplayer is too close");
				aiCon.setState(AiStates.ESCAPE);
			}
			else {
				if(debug)
					System.out.println("\tescape on hill");
				aiCon.setState(AiStates.WANDER);
			}
		}

		//case 6, FINISH HIM
		else if (playerHP < (maxHP/3)) {
			if(debug)
				System.out.println("case 6");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}		

	}
	
	
	//difference between normal and easy AI, 
	/*normal ai:
	runs away from heavy attacks, 
	runs away if hp gap with nearest player is more than 50
	does not prioritize powerups on going to hill
	changes to appropriate elements when attacking and defending
	wanders close to hill area when hp is low
	*/
	@Override
	public void normalAiFetchAction() {
		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		Player onHillPlayer =  calc.getOnHillPlayer();
		float nearestPlayerHP = nearestPlayer.getHealth();
		
		boolean debug = true;
		
		
		
		//case 1, go to hill
		if(calc.playerIsTooClose() && calc.onHillEdge() && aiPlayerHP > (maxHP/3) &&
				!( nearestPlayerHP - aiPlayerHP > 50 ) && !(calc.isCharging(nearestPlayer) || !calc.isCharging(onHillPlayer)) &&
				!(nearestPlayerHP < (maxHP/3) && aiPlayerHP > maxHP/3)) {
			if(debug)
				System.out.println("case 1");
			if(calc.onHill(aiPlayer.getLocation()))
				aiCon.setState(AiStates.IDLE);
			else 
				aiCon.setState(AiStates.GO_TO_HILL);
		}
		
		//case 2, run from heavy attack
		else if (calc.isCharging(nearestPlayer)) {
			if(debug)
				System.out.println("case 2");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, escape when dying
		else if( (aiPlayerHP < (maxHP/3) &&  aiPlayerHP < nearestPlayerHP) || nearestPlayerHP - aiPlayerHP > 50) {
			if(debug)
				System.out.println("case 3");
			if(calc.powerUpExist(PowerUpType.HEAL)) {
				if(debug)
					System.out.println("\tfind health");
				aiCon.setState(AiStates.FIND_HEALTH);
			}
			else if(calc.playerIsTooClose() && (calc.onHill(nearestPlayer.getLocation()) || calc.closeToHill(nearestPlayer)) ) {
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
		
		//case 4, take a power up
		else if (calc.powerupIsTooClose()) {
			if(debug)
				System.out.println("case 4");
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
		
		//case 5, normal attacking
		else if ( calc.playerIsTooClose() && aiPlayerHP > nearestPlayerHP && calc.closeToHill()) {
			if(debug)
				System.out.println("case 5");
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 6, stay on hill
		else if( !calc.onHill(aiPlayer.getLocation()) && aiPlayerHP > maxHP/3 && !(nearestPlayerHP < (maxHP/3)) ) {
			if(debug)
				System.out.println("case 6");
			aiCon.setState(AiStates.GO_TO_HILL);
		}
		
		//case 7, FINISH HIM
		else if (nearestPlayerHP < (maxHP/3) && aiPlayerHP > maxHP/3) {
			if(debug)
				System.out.println("case 7");
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}
	}
	
	//difference between norma and hard AI
	/* hard ai:
	 * does not leave the hill to kill players with less than 1/3 hp
	 * runs away if hp gap with nearest player is more than 30
	 * does not leave the hill to scare players off
	 * wanders even closer to hill area when hp is low
	 * attacks the one with higher score if score difference is more than 3000
	 * does not run away when its health is low only but rather when hp is low and hp difference compared to nearest player is high
	 */
	@Override
	public void hardAiFetchAction() {
		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		Player onHillPlayer =  calc.getOnHillPlayer();
		Player winningPlayer = calc.getWinningPlayer();
		float nearestPlayerHP = nearestPlayer.getHealth();
		
		boolean debug = true;
		
		
		
		//case 1, go to hill
		if(calc.playerIsTooClose() && calc.onHillEdge() && !( aiPlayerHP < (maxHP/3) && nearestPlayerHP - aiPlayerHP > 50) &&
				calc.someoneCloseIsCharging() && !(calc.scoreDifferenceIsMoreThan(3000) && calc.isNearestPlayer(winningPlayer)) ) {
			if(debug)
				System.out.println("case 1");
			if(calc.onHill(aiPlayer.getLocation()))
				aiCon.setState(AiStates.IDLE);
			else 
				aiCon.setState(AiStates.GO_TO_HILL);
		}
		
		//case 2, run from heavy attack
		else if (calc.someoneCloseIsCharging()) {
			if(debug)
				System.out.println("case 2");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, escape when dying
		else if( aiPlayerHP < (maxHP/3) && nearestPlayerHP - aiPlayerHP > 50) {
			if(debug)
				System.out.println("case 3");
			if(calc.powerUpExist(PowerUpType.HEAL)) {
				if(debug)
					System.out.println("\tfind health");
				aiCon.setState(AiStates.FIND_HEALTH);
			}
			else if(calc.playerIsTooClose() && (calc.onHill(nearestPlayer.getLocation()) || calc.closeToHill(nearestPlayer)) ) {
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
		
		//case 4, take a power up
		else if (calc.powerupIsTooClose()) {
			if(debug)
				System.out.println("case 4");
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
		
		//case 5, stay on hill
		else if( !calc.onHill(aiPlayer.getLocation()) && !(aiPlayerHP < (maxHP/3) && nearestPlayerHP - aiPlayerHP > 50) && !(calc.scoreDifferenceIsMoreThan(3000) && calc.isNearestPlayer(winningPlayer)) ) {
			if(debug)
				System.out.println("case 5");
			aiCon.setState(AiStates.GO_TO_HILL);
		}

		//case 6, attack winning player
		else if (calc.scoreDifferenceIsMoreThan(3000)) {
			if(debug)
				System.out.println("case 6");
			aiCon.setState(AiStates.ATTACK_WINNER);
		}
		
		//case 7, normal attacking
		else if ( calc.playerIsTooClose() && aiPlayerHP > nearestPlayerHP && calc.closeToHill()) {
			if(debug)
				System.out.println("case 7");
			aiCon.setState(AiStates.ATTACK);
		}

	}

}
