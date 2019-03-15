package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiStates;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class TimedFSM extends FSM{
	
	public TimedFSM(Player aiPlayer, AiController aiCon,AiCalculations calc) {
		super(aiPlayer, aiCon, calc);
	}
	
	/*
	 * easy AI features:
	 * changes element randomly every 15 seconds
	 * goes for a health power up if it is close
	 * escapes when losing or HP gap is more than 20
	 * attacks aggressively if a close by player is dying
	 * takes a power if it is close
	 * starts the game wandering around
	 */
	@Override
	public void easyAiFetchAction() {

		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take a heal power up
		if(  calc.powerupCloserThanPlayer() && aiPlayerHP<maxHP && calc.getNearestPowerUp(PowerUpType.HEAL) != -1 ) {
			aiCon.setState(AiStates.FIND_HEALTH);
		}
	
		//case 2, run for your life
		else if(aiPlayerHP < (maxHP/10) || playerHP - aiPlayerHP > 20) {
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, FINISH HIM
		else if (playerHP < (maxHP/3)) {
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}		
		
		//case 4, normal attacking
		else if ( calc.playerIsTooClose() || aiPlayerHP > playerHP  ) {
			aiCon.setState(AiStates.ATTACK);
		}
	
		//case5, take the power up on your way
		else if (calc.powerupIsTooClose()) {
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
		else {
			aiCon.setState(AiStates.WANDER);
		}
	}
	
	/*
	 * features added/changed in normal AI:
	 * element is changed depending on whether the AI is on the offensive or defensive
	 * prioritises taking power ups if they are close
	 * attacks winning player if score gap is high
	 */
	@Override
	public void normalAiFetchAction() {

		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take any type of power up
		if( ( calc.powerupIsTooClose() && calc.powerupCloserThanPlayer() ) || calc.powerupCloserThanPlayer() ) {
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
		else if(aiPlayerHP < (maxHP/3) && playerHP - aiPlayerHP > 20) {
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, attack aggressively when the nearest enemy's HP is less than 33%
		else if (playerHP < (maxHP/3)) {
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}
		
		else if(calc.scoreDifferenceIsMoreThan(2)) {
			aiCon.setState(AiStates.ATTACK_WINNER);
		}
		
		//case 4, attack when you got the advantage
		else if (calc.playerIsTooClose() || aiPlayerHP > playerHP) {
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 5, wander, or attack, when nothing else is triggered
		else {
			aiCon.setState(AiStates.WANDER);
		}
	}
	
	/*
	 *  features added/changed in hard AI:
	 *  goes for health power up if they exist and AI's HP is not full
	 *  escapes if someone close is charging a heavy attack
	 *  starts off the game attacking the nearest player
	 */
	@Override
	public void hardAiFetchAction() {

		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = calc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, a power up is closer than an enemy
		if( calc.powerupCloserThanPlayer() ) {
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
		
		//case 2, the ai player's hp is less than maxHP and a health power up is available
		else if (aiPlayerHP<(maxHP) && calc.powerUpExist(PowerUpType.HEAL)) {
			aiCon.setState(AiStates.FIND_HEALTH);
		}
		
		//case 5, there exist a damage power up
		else if(  calc.powerupCloserThanPlayer() && calc.getNearestPowerUp(PowerUpType.DAMAGE) != -1 ) {
			aiCon.setState(AiStates.FIND_DAMAGE);
		}
		
		//case 3, the ai player's hp is less than 33% and a health power up is not available
		else if( (aiPlayerHP < (maxHP/2) && playerHP - aiPlayerHP > 20) || calc.someoneCloseIsCharging() ||calc.isCharging(nearestPlayer) ) {
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 4, the nearest enemy's hp is less than 33%
		else if ( playerHP < (maxHP/2) || aiPlayer.activeDamagePowerup()) {
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}
		
		else if(calc.scoreDifferenceIsMoreThan(2)) {
			aiCon.setState(AiStates.ATTACK_WINNER);
		}
		
		//case 7, the nearest enemy's hp is less than ai player's hp
		else if (aiPlayerHP > playerHP) {
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 6, there exist a speed power up
		else if( calc.getNearestPowerUp(PowerUpType.SPEED) != -1 ) {
			aiCon.setState(AiStates.FIND_SPEED);
		}
		
		//else 'when the enemy's hp is more than the ai player's or equal to it"
		else {
			aiCon.setState(AiStates.ATTACK);
		}
	}

}
