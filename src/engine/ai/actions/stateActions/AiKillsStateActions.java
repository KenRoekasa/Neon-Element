package engine.ai.actions.stateActions;

import engine.ai.actions.AiActions;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;

public class AiKillsStateActions extends AiStateActions {
	
	/**
	 * @param aiCon AI controller object
	 * @param calc AI calculations object
	 * @param actions AI actions used to make state actions
	 */
	public AiKillsStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		super(aiCon, calc, actions);
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
		case ATTACK_LOSING:
			AttackLosing();
		default:
			break;
		}
		
	}

	//Attacks losing player to gain kills
	private void AttackLosing() {
		aiPlayer.unShield();
		Player player = playerCalc.getPlayerWithLowestHealth();
		actions.moveTo(player);
		actions.attackIfInDistance(player);
	}

}
