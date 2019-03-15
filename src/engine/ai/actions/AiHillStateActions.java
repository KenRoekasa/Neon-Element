package engine.ai.actions;

import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.HillCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiType;
import engine.entities.Player;

public class AiHillStateActions extends AiStateActions {
	HillCalculations calc;
	public AiHillStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		super(aiCon, calc, actions);
		this.calc = (HillCalculations)calc;
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
			player = calc.getPlayerCalc().getNearestPlayer();
		
		actions.moveTo(player);

		actions.attackIfInDistance(player);
	}
	
	@Override
	protected void aggressiveAttack() {
		aiPlayer.unShield();
		Player player;

		player = calc.getPlayerCalc().getNearestPlayer();
		aiPlayer.chargeHeavyAttack();
		if(aiCon.getAiType().equals(AiType.HARD))
			actions.moveToAndKeepDistance(player);
		else
			actions.moveTo(player);
		
		if (calc.getPlayerCalc().inAttackDistance(player) && player.getHealth()>0 && !calc.getPlayerCalc().isCharging(aiPlayer)) {
			aiPlayer.lightAttack();
		}
	}
	
	private void wanderOnHill() {
		if(aiCon.getAiType().equals(AiType.HARD)) {
			if(!calc.onHill(aiPlayer.getLocation()))
				goToHill();
			else
				wander();
		}
		else if(aiCon.getAiType().equals(AiType.NORMAL)) {
			if(!calc.closeToHill())
				goToHill();
			else
				wander();
		}
		
	}

	private void goToHill() {
		actions.shieldWhenAlone();
		Player player = calc.getPlayerCalc().getNearestPlayer();
		actions.attackIfInDistanceWithShield(player);
		if( !calc.onHill(aiPlayer.getLocation()))
			actions.simpleMovement(aiPlayer.getLocation(), calc.getHillCentreLocation());
		else 
			idle();
	}
	
}
