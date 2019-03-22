package engine.ai.calculations.statecalculations;

import engine.ScoreBoard;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

public class KillsCalculations extends AiCalculations{

	/**
	 * @param aiCon AI controller object
	 * @param map Map of game
	 * @param scoreboard score board object
	 * @param gameType Game type
	 */
	public KillsCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		super(aiCon, map, scoreboard, gameType);
	}

}
