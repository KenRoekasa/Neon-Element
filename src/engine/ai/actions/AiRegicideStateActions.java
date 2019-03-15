package engine.ai.actions;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;

public class AiRegicideStateActions extends AiStateActions {

	public AiRegicideStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
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
		case ATTACK_WINNER:
			attackWinner();
			break;
		case IDLE:
			idle();
			break;
		case ATTACK_KING:
			attackKing();
			break;
		default:
			break;
		}
		
	}

	private void attackKing() {
		aiPlayer.unShield();
		Player king = calc.getKing();
		actions.moveTo(king);
		actions.attackIfInDistance(king);
	}
}
