package engine.ai.calculations.stateCalculations;


import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.model.GameType;
import engine.model.ScoreBoard;
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
