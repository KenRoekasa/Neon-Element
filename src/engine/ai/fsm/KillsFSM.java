package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiStates;
import engine.entities.Player;
import engine.enums.PowerUpType;

public class KillsFSM extends FSM {

	public KillsFSM(Player aiPlayer, AiController aiCon, AiCalculations calc) {
		super(aiPlayer, aiCon, calc);
	}

	/** Sets AI state given everything going on in the game. 
	 * easy AI features:
	 * random element every 15 seconds
	 * prioritise killing over evading death
	 * escapes when losing or HP gap is more than 20
	 * takes a power up if it is close to it
	 * starts the game wandering around
	 * attacks aggressively if nearest player's HP is less than a third
	 */
	@Override
	protected void easyAiFetchAction() {

		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = playerCalc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take a heal power up
		if(  puCalc.powerupCloserThanPlayer() && aiPlayerHP<maxHP && puCalc.getNearestPowerUp(PowerUpType.HEAL) != -1 ) {
			aiCon.setState(AiStates.FIND_HEALTH);
		}
		
		//case 2, FINISH HIM
		else if (playerHP < (maxHP/3) && aiPlayerHP > playerHP ) {
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}	
	
		//case 3, run for your life
		else if(aiPlayerHP < (maxHP/10) || playerHP - aiPlayerHP > 20) {
			aiCon.setState(AiStates.ESCAPE);
		}	
		
		//case 4, normal attacking
		else if ( playerCalc.playerIsTooClose() || aiPlayerHP > playerHP  ) {
			aiCon.setState(AiStates.ATTACK);
		}
	
		//case5, take the power up on your way
		else if (puCalc.powerupIsTooClose()) {
			switch(puCalc.getPowerups().get(puCalc.getNearestPowerUp()).getType()) {
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
	
	
	/** Sets AI state given everything going on in the game. 
	 *  features added/changed in normal AI compared to easy:
	 * element is changed based on whether it is on an offensive or a defensive mode
	 * prioritises taking a power up if it is close or if it is the closest object
	 * prioritises escaping when health is low than attack
	 */
	@Override
	protected void normalAiFetchAction() {

		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = playerCalc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, take any type of power up
		if( ( puCalc.powerupIsTooClose() && puCalc.powerupCloserThanPlayer() ) || puCalc.powerupCloserThanPlayer() ) {
			switch(puCalc.getPowerups().get(puCalc.getNearestPowerUp()).getType()) {
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
		else if(aiPlayerHP < (maxHP/3) && playerHP - aiPlayerHP > 20 ) {
			aiCon.setState(AiStates.ESCAPE);
		}
		
		//case 3, attack aggressively when the nearest enemy's hp is less than 33%
		else if (playerHP < (maxHP/3)) {
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}
		
		//case 4, attack when you got the advantage
		else if (playerCalc.playerIsTooClose() || aiPlayerHP > playerHP) {
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 5, wander, or attack, when nothing else is triggered
		else {
			aiCon.setState(AiStates.WANDER);
		}
	}
	
	/** Sets AI state given everything going on in the game. 
	 *  features added/changed in hard AI compared to normal:
	 * prioritises taking a health power up even if an enemy is close
	 * escapes when
	 * 	 health is less than half 
	 * 	 or if a player close by is charging 
	 * 	 or if it is winning and the closest player's HP is more than the AI's
	 * attacks aggressively while damage power up is active 
	 * attacks winning player if score gap is high
	 * starts the game attacking nearest player
	 */
	@Override
	protected void hardAiFetchAction() {

		float aiPlayerHP = aiPlayer.getHealth();
		Player nearestPlayer = playerCalc.getNearestPlayer();
		float playerHP = nearestPlayer.getHealth();
		
		//case 1, a power up is closer than an enemy
		if( puCalc.powerupCloserThanPlayer() ) {
			switch(puCalc.getPowerups().get(puCalc.getNearestPowerUp()).getType()) {
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
		else if (aiPlayerHP<(maxHP) && puCalc.powerUpExist(PowerUpType.HEAL) ) {
			aiCon.setState(AiStates.FIND_HEALTH);
		}
		
		//case 3, there exist a damage power up
		else if(  puCalc.powerupCloserThanPlayer() && puCalc.getNearestPowerUp(PowerUpType.DAMAGE) != -1 ) {
			aiCon.setState(AiStates.FIND_DAMAGE);
		}
		
		//case 4, the engine.ai player's hp is less than 33% and a health power up is not available
		
		else if( (aiPlayerHP < (maxHP/2) && aiPlayerHP<playerHP) || playerCalc.someoneCloseIsCharging() || playerCalc.isCharging(nearestPlayer) || (playerCalc.getWinningPlayer().equals(aiPlayer) && playerHP-aiPlayerHP > 20)  ) {
			aiCon.setState(AiStates.ESCAPE);
		}
		
		
		else if(playerCalc.getPlayerWithLowestHealth().getHealth()<20 && playerCalc.getPlayerWithLowestHealth().getHealth()>0 ) {
			aiCon.setState(AiStates.ATTACK_LOSING);
		}
		
		//case 5, the nearest enemy's hp is less than 33%
		else if ( playerHP < (maxHP/2) || aiPlayer.activeDamagePowerup()) {
			aiCon.setState(AiStates.AGGRESSIVE_ATTACK);
		}
		
		//case 6, attacks winner if score gap is higher than 2
		else if(playerCalc.scoreDifferenceIsMoreThan(2)) {
			aiCon.setState(AiStates.ATTACK_WINNER);
		}
		
		//case 7, the nearest enemy's hp is less than ai player's hp
		
		else if (aiPlayerHP > playerHP) {
			aiCon.setState(AiStates.ATTACK);
		}
		
		//case 8, there exist a speed power up
		else if( puCalc.getNearestPowerUp(PowerUpType.SPEED) != -1 ) {
			aiCon.setState(AiStates.FIND_SPEED);
		}
		
		//else 'when the enemy's hp is more than the ai player's or equal to it"
		else {
			aiCon.setState(AiStates.ATTACK);
		}
	}

}
