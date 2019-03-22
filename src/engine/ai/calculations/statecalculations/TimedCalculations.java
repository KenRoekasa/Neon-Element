package engine.ai.calculations.statecalculations;

import engine.ScoreBoard;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

public class TimedCalculations extends AiCalculations {

	public TimedCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		super(aiCon, map, scoreboard, gameType);
	}
}
