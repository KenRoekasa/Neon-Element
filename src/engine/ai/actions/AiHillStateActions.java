package engine.ai.actions;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
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
		case GO_TO_HILL:
			goToHill();
			break;
		case WANDER_ON_HILL:
			wanderOnHill();
			break;
		case ESCAPE_ON_HILL:
			escapeOnHill();
		case ATTACK_WINNER:
			attackWinner();
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
		aiPlayer.shield();
		Player player = calc.getNearestPlayer();
		if (calc.inAttackDistance(player) && player.getHealth()>0) {
			aiPlayer.unShield();
			aiPlayer.lightAttack();
		}
		if( aiPlayer.getLocation().distance(calc.getHillCentreLocation()) > 10 )
			actions.simpleMovement(aiPlayer.getLocation(), calc.getHillCentreLocation());
	}


}
