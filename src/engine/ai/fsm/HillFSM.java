package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.statecalculations.HillCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiStates;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class HillFSM extends FSM{
	
	HillCalculations calc;
	
	public HillFSM(Player aiPlayer, AiController aiCon,AiCalculations calc) {
		super(aiPlayer, aiCon, calc);
		this.calc = (HillCalculations)calc;
	}
	
	//easy AI:
	/**
	 *prioritise taking power ups
	 *attacks players that get close to the hill
	 *when HP is low, it wanders around the map, looking for health power up
	 *if the closest player's HP is less than a third, it attacks aggressively
	 */
	@Override
	public void easyAiFetchAction() {
		
		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = playerCalc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		boolean debug = false;
		
		//if there is a power up take it
		if (puCalc.powerupIsTooClose()) {
			if(debug)
				System.out.println("case 1");
			switch(puCalc.getPowerups().get(puCalc.getNearestPowerUp()).getType()) {
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
		else if ( playerCalc.playerIsTooClose() && aiPlayerHP > playerHP && calc.closeToHill()) {
			if(debug)
				System.out.println("case 2");
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 3, go to hill
		else if(playerCalc.playerIsTooClose() && calc.onHillEdge() && aiPlayerHP > (maxHP/3)) {
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
			if(puCalc.powerUpExist(PowerUpType.HEAL)) {
				if(debug)
					System.out.println("\tfind health");
				aiCon.setState(AiStates.FIND_HEALTH);
			}
			else if(playerCalc.playerIsTooClose()) {
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
	/**normal AI:
	* runs away from heavy attacks, 
	* runs away if HP gap with nearest player is more than 50
	* prioritises going to hill on going to pick power ups
	* changes to appropriate elements when attacking and defending
	* wanders close to hill area when HP is low, looking for health power up
	*/
	@Override
	public void normalAiFetchAction() {
		
		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = playerCalc.getNearestPlayer();
		float nearestPlayerHP = nearestPlayer.getHealth();
		boolean debug = false;
	
		//case 1, go to hill
		if(playerCalc.playerIsTooClose() && calc.onHillEdge() && aiPlayerHP > (maxHP/3) &&
				!( nearestPlayerHP - aiPlayerHP > 50 ) && !playerCalc.someoneCloseIsCharging() &&
				!(nearestPlayerHP < (maxHP/3) && aiPlayerHP > maxHP/3)) {
			if(debug)
				System.out.println("case 1");
			if(calc.onHill(aiPlayer.getLocation()))
				aiCon.setState(AiStates.IDLE);
			else 
				aiCon.setState(AiStates.GO_TO_HILL);
		}
		
		//case 2, run from heavy attack
		else if (playerCalc.isCharging(nearestPlayer)) {
			if(debug)
				System.out.println("case 2");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, escape when dying
		else if( (aiPlayerHP < (maxHP/3) &&  aiPlayerHP < nearestPlayerHP) || nearestPlayerHP - aiPlayerHP > 50) {
			if(debug)
				System.out.println("case 3");
			if(puCalc.powerUpExist(PowerUpType.HEAL)) {
				if(debug)
					System.out.println("\tfind health");
				aiCon.setState(AiStates.FIND_HEALTH);
			}
			else if(playerCalc.playerIsTooClose() && (calc.onHill(nearestPlayer.getLocation()) || calc.closeToHill(nearestPlayer)) ) {
				if(debug)
					System.out.println("\twander");
				aiCon.setState(AiStates.WANDER);
			}
			else if(playerCalc.playerIsTooClose()) {
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
		else if (puCalc.powerupIsTooClose()) {
			if(debug)
				System.out.println("case 4");
			switch(puCalc.getPowerups().get(puCalc.getNearestPowerUp()).getType()) {
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
		else if ( playerCalc.playerIsTooClose() && aiPlayerHP > nearestPlayerHP && calc.closeToHill()) {
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
	
	//difference between normal and hard AI
	/* hard AI:
	 * does not leave the hill to kill players with less than 1/3 HP
	 * runs away if HP gap with nearest player is more than 30
	 * does not leave the hill to scare players off
	 * wanders very close to hill area when HP is low; looks for health power up
	 * attacks the one with higher score if score difference is more than 3000
	 * does not run away when its health is low only but rather when HP is low and HP difference compared to nearest player is high
	 * keeps distance, 150, from opponents when charge a heavy attack, so they receive damage but cannot reach it to attack while charging
	 */
	@Override
	public void hardAiFetchAction() {
		
		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = playerCalc.getNearestPlayer();
		Player winningPlayer = playerCalc.getWinningPlayer();
		float nearestPlayerHP = nearestPlayer.getHealth();
		boolean debug = false;
		
		//case 1, go to hill
		if(playerCalc.playerIsTooClose() && calc.onHillEdge() && !( aiPlayerHP < (maxHP/3) && nearestPlayerHP - aiPlayerHP > 50) &&
				playerCalc.someoneCloseIsCharging() && !(playerCalc.scoreDifferenceIsMoreThan(3000) && playerCalc.isNearestPlayer(winningPlayer)) ) {
			if(debug)
				System.out.println("case 1");
			if(calc.onHill(aiPlayer.getLocation()))
				aiCon.setState(AiStates.IDLE);
			else 
				aiCon.setState(AiStates.GO_TO_HILL);
		}
		
		//case 2, run from heavy attack
		else if (playerCalc.someoneCloseIsCharging()) {
			if(debug)
				System.out.println("case 2");
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, escape when dying
		else if( aiPlayerHP < (maxHP/3) && nearestPlayerHP - aiPlayerHP > 50) {
			if(debug)
				System.out.println("case 3");
			if(puCalc.powerUpExist(PowerUpType.HEAL)) {
				if(debug)
					System.out.println("\tfind health");
				aiCon.setState(AiStates.FIND_HEALTH);
			}
			else if(playerCalc.playerIsTooClose() && (calc.onHill(nearestPlayer.getLocation()) || calc.closeToHill(nearestPlayer)) ) {
				if(debug)
					System.out.println("\twander");
				aiCon.setState(AiStates.WANDER);
			}
			else if(playerCalc.playerIsTooClose()) {
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
		else if (puCalc.powerupIsTooClose()) {
			if(debug)
				System.out.println("case 4");
			switch(puCalc.getPowerups().get(puCalc.getNearestPowerUp()).getType()) {
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
		else if( !calc.onHill(aiPlayer.getLocation()) && !(aiPlayerHP < (maxHP/3) && nearestPlayerHP - aiPlayerHP > 50) && !(playerCalc.scoreDifferenceIsMoreThan(3000) && playerCalc.isNearestPlayer(winningPlayer)) ) {
			if(debug)
				System.out.println("case 5");
			aiCon.setState(AiStates.GO_TO_HILL);
		}

		//case 6, attack winning player
		else if (playerCalc.scoreDifferenceIsMoreThan(3000)) {
			if(debug)
				System.out.println("case 6");
			aiCon.setState(AiStates.ATTACK_WINNER);
		}
		
		//case 7, normal attacking
		else if ( playerCalc.playerIsTooClose() && aiPlayerHP > nearestPlayerHP && calc.closeToHill()) {
			if(debug)
				System.out.println("case 7");
			aiCon.setState(AiStates.ATTACK);
		}

	}

}
