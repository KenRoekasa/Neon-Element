package engine.ai.actions;

import java.util.Random;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiType;
import engine.entities.Player;

public class AiHillStateActions extends AiStateActions {
	
	public AiHillStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		super(aiCon, calc, actions);
	}
	
	@Override
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
		case GO_TO_HILL:
			goToHill();
			break;
		case WANDER_ON_HILL:
			wanderOnHill();
			break;
//		case ESCAPE_ON_HILL:
//			escapeOnHill();
//			break;
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
	
	@Override
	protected void attack() {
		aiPlayer.unShield();
		Player player;
		if(aiCon.getAiType().equals(AiType.HARD))
			player = calc.getOnHillPlayer();
		else
			player = calc.getNearestPlayer();
		
		actions.moveTo(player);

		actions.attackIfInDistance(player);
	}
	
	@Override
	protected void aggressiveAttack() {
		aiPlayer.unShield();
		Player player;
		if(aiCon.getAiType().equals(AiType.HARD))
			player = calc.getOnHillPlayer();
		else
			player = calc.getNearestPlayer();
		aiPlayer.chargeHeavyAttack();
		actions.moveTo(player);
		
		if (calc.inAttackDistance(player) && player.getHealth()>0 && !calc.isCharging(aiPlayer)) {
			aiPlayer.lightAttack();
		}
	}
	
	private void wanderOnHill() {
		if(!calc.onHill(aiPlayer.getLocation()))
			goToHill();
		else
			wander();
	}

	private void goToHill() {
		actions.shieldWhenAlone();
		Player player = calc.getNearestPlayer();
		actions.attackIfInDistanceWithShield(player);
		if( !calc.onHill(aiPlayer.getLocation()))
			actions.simpleMovement(aiPlayer.getLocation(), calc.getHillCentreLocation());
		else 
			idle();
	}


}
