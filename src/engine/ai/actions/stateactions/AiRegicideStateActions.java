package engine.ai.actions.stateactions;

import engine.ai.actions.AiActions;
import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.statecalculations.RegicideCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;

public class AiRegicideStateActions extends AiStateActions {

	//AI regicide calculations object
	private RegicideCalculations calc;

	/**
	 * @param aiCon AI controller object
	 * @param calc AI calculations object
	 * @param actions AI actions used to make state actions
	 */
	public AiRegicideStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		super(aiCon, calc, actions);
		this.calc = (RegicideCalculations) calc;
	}

	/**
	 * Executes a state action relevant to current AI state. Also updates AI element in an AI type relevant matter.
	 */
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
	
	/**
	 * Implements actions for attack king AI state 
	 */
	private void attackKing() {
		aiPlayer.unShield();
		Player king = calc.getKing();
		actions.moveTo(king);
		actions.attackIfInDistance(king);
	}
}
