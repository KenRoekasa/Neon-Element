package engine.ai;

import java.util.Random;

import engine.calculations.AiCalculations;
import engine.entities.Player;
import engine.enums.AiStates;
import engine.enums.AiType;
import engine.enums.PowerUpType;
//high level actions, based on ai states 
public class AiStateActions {
	
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
		switch(aiCon.getAiType()) {
		case EASY:
			easyAIExecuteAction();
			break;
		case NORMAL:
			normalAIExecuteAction();
			break;
		case HARD:
			hardAIExecuteAction();
			break;
		}
		
	}
	
	public void setWandering(boolean bool) {
		wandering = bool;
	}
	
	public boolean isWandering() {
		return wandering;
	}
	
	private void easyAIExecuteAction() {
		
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
		case GO_TO_HILL:
			goToHill();
			break;
		case WANDER_ON_HILL:
			wanderOnHill();
			break;
		case ESCAPE_ON_HILL:
			escapeOnHill();
		case IDLE:
			break;
		default:
			break;
		}
	}
	
	private void escapeOnHill() {
		if(!calc.onHill(aiPlayer.getLocation()))
			goToHill();
		else
			escape();
	}

	private void wanderOnHill() {
		if(!calc.onHill(aiPlayer.getLocation()))
			goToHill();
		else
			wander();
	}

	private void goToHill() {
		actions.simpleMovement(aiPlayer.getLocation(), calc.getHillCentreLocation());
	}

	private void normalAIExecuteAction() {
		
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
			aiPlayer.shield();
			escape();
			break;
		case WANDER:
			aiPlayer.unShield();
			wander();
		case IDLE:
			break;
		default:
			break;
		}
	}

	private void hardAIExecuteAction() {
		
		updateElement();
		updateWandering();
		
		switch (aiCon.getActiveState()) {
		case ATTACK:
			aiPlayer.unShield();
			attack();
			break;
		case AGGRESSIVE_ATTACK:
			aiPlayer.unShield();
			aggressiveAttack();
			break;
		case FIND_HEALTH:
			aiPlayer.shield();
			findHealth();
			break;
		case FIND_DAMAGE:
			aiPlayer.shield();
			findDamage();
			break;
		case FIND_SPEED:
			aiPlayer.shield();
			findSpeed();
			break;
		case ESCAPE:
			aiPlayer.shield();
			escape();
			break;
		case WANDER:
			aiPlayer.unShield();
			wander();
		case ATTACK_WINNER:
			aiPlayer.unShield();
			attackWinner();
		case IDLE:
			break;
		default:
			break;
		}
	}

	private void wander() {
		aiPlayer.unShield();
		updateWandering();
		actions.startWandering();
	}
	
	private void findSpeed() {
		aiPlayer.shield();
		int index = calc.getNearestPowerUp(PowerUpType.SPEED);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	private void findDamage() {
		aiPlayer.shield();
		int index = calc.getNearestPowerUp(PowerUpType.DAMAGE);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	private void findHealth() {
		aiPlayer.shield();
		int index = calc.getNearestPowerUp(PowerUpType.HEAL);
		if (index != -1)
			actions.moveTo(index, calc.getPowerups().get(index).getLocation());
	}

	private void aggressiveAttack() {
		aiPlayer.unShield();
		Player player = calc.getNearestPlayer();
		aiPlayer.chargeHeavyAttack();
		actions.moveTo(player);
		if (calc.inAttackDistance(player) && player.getHealth()>0 && !calc.isCharging(aiPlayer)) {
			aiPlayer.lightAttack();
		}
	}
	
	private void escape() {
		aiPlayer.shield();
		Player player = calc.getNearestPlayer();
		actions.moveAway(player);
	}	
	
	private void attack() {
		aiPlayer.unShield();
		Player player = calc.getNearestPlayer();
		actions.moveTo(player);

		if (calc.inAttackDistance(player) && player.getHealth()>0) {
			aiPlayer.lightAttack();
		}
	}

	private void attackWinner() {
		Player player = calc.getWinningPlayer();
		actions.moveTo(player);
		aiPlayer.chargeHeavyAttack();
		actions.moveTo(player);
		if (calc.inAttackDistance(player) && player.getHealth()>0 && !calc.isCharging(aiPlayer)) {
			aiPlayer.lightAttack();
		}
				
	}
	
	private void updateElement() {
		if(aiCon.getAiType().equals(AiType.EASY))
			actions.changeToRandomElementAfter(15);
		else
			actions.changeToBefittingElement();
	}

	private void updateWandering() {
		if(isWandering() && !aiCon.getActiveState().equals(AiStates.WANDER))
			setWandering(false);
		else if(!isWandering() && aiCon.getActiveState().equals(AiStates.WANDER)) {
			setWandering(true);
			Random r = new Random();
			actions.wanderingDirection = r.nextInt(8);
		}
	}
	

}
