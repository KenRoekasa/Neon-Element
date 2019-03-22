package engine.ai.actions.stateactions;

import engine.ai.actions.AiActions;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;

public class AiTimedStateActions extends AiStateActions {

	/**
	 * @param aiCon AI controller object
	 * @param calc AI calculations object
	 * @param actions AI actions used to make state actions
	 */
	public AiTimedStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		super(aiCon, calc, actions);
	}

}
