package engine.ai.actions.stateActions;

import engine.ai.actions.AiActions;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;

public class AiKillsStateActions extends AiStateActions {
	
	/**
	 * @param aiCon AI controller object
	 * @param calc AI calculations object
	 * @param actions AI actions used to make state actions
	 */
	public AiKillsStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		super(aiCon, calc, actions);
	}

}
