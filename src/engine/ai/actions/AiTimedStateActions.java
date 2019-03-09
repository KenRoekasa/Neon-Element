package engine.ai.actions;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;

public class AiTimedStateActions extends AiStateActions {

	public AiTimedStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		super(aiCon, calc, actions);
	}

}