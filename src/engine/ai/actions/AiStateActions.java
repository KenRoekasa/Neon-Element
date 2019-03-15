package engine.ai.actions;

import java.util.Random;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiStates;
import engine.ai.enums.AiType;
import engine.entities.Player;
import engine.enums.PowerUpType;
//high level actions, based on ai states 
public abstract class AiStateActions {
	
	Player aiPlayer;
	Player player;
	AiController aiCon;
	AiCalculations calc;
	AiActions actions;
	boolean wandering = false;
	public AiStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {

		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.player = aiCon.getPlayer();
		this.calc = calc;
		this.actions = actions;
		
	}

	public void executeAction() {
		updateElement();
		updateWandering();
		
		switch (aiCon.getActiveState()) {
		case ATTACK:
			attack();
			break;
		case AGGRESSIVE_ATTACK:
			aggressiveAttack();
			break;
		case FIND_HEALTH:
			findHealth();
			break;
		case FIND_DAMAGE:
			findDamage();
			break;
		case FIND_SPEED:
			findSpeed();
			break;
		case ESCAPE:
			escape();
			break;
		case WANDER:
			wander();
			break;
		case ATTACK_WINNER:
			attackWinner();
			break;
		case IDLE:
			idle();
			break;
		default:
			break;
		}
		
	}
	
	protected void idle() {
		Player player = calc.getNearestPlayer();
		actions.attackIfInDistanceWithShield(player);
	}

	public void setWandering(boolean bool) {
		wandering = bool;
	}
	
	public boolean isWandering() {
		return wandering;
	}

	protected void wander() {
		aiPlayer.unShield();
		updateWandering();
		actions.startWandering();
	}
	
	protected void findSpeed() {
		actions.shieldWhenAlone();
		int index = calc.getNearestPowerUp(PowerUpType.SPEED);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	protected void findDamage() {
		actions.shieldWhenAlone();
		int index = calc.getNearestPowerUp(PowerUpType.DAMAGE);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	protected void findHealth() {
		actions.shieldWhenAlone();
		int index = calc.getNearestPowerUp(PowerUpType.HEAL);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	protected void aggressiveAttack() {
		aiPlayer.unShield();
		Player player = calc.getNearestPlayer();
		aiPlayer.chargeHeavyAttack();
		if(aiCon.getAiType().equals(AiType.HARD))
			actions.moveToAndKeepDistance(player);
		else
			actions.moveTo(player);
		actions.attackIfInDistance(player);
	}
	
	protected void escape() {
		actions.shieldWhenAlone();
		Player player = calc.getNearestPlayer();
		actions.moveAwayFromPlayer(player);
	}	
	
	protected void attack() {
		aiPlayer.unShield();
		Player player = calc.getNearestPlayer();
		actions.moveTo(player);
		actions.attackIfInDistance(player);
	}

	protected void attackWinner() {
		aiPlayer.unShield();
		Player player = calc.getWinningPlayer();
		aiPlayer.chargeHeavyAttack();
		actions.moveTo(player);
		
		actions.attackIfInDistance(player);
				
	}
	
	protected void updateElement() {
		if(aiCon.getAiType().equals(AiType.EASY))
			actions.changeToRandomElementAfter(15);
		else
			actions.changeToBefittingElement();
	}

	protected void updateWandering() {
		if(isWandering() && !aiCon.getActiveState().equals(AiStates.WANDER))
			setWandering(false);
		else if( (!isWandering() && aiCon.getActiveState().equals(AiStates.WANDER)) || calc.hasBeenWanderingFor(5) ) {
			setWandering(true);
			Random r = new Random();
			actions.wanderingDirection = r.nextInt(8);
		}
	}
	

}
