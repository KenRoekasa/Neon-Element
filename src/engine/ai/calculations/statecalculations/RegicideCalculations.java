package engine.ai.calculations.statecalculations;

import engine.ScoreBoard;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;
import engine.gameTypes.Regicide;
import javafx.scene.shape.Rectangle;

public class RegicideCalculations extends AiCalculations{

	protected Regicide regicide;
	
	
	public RegicideCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		super(aiCon, map, scoreboard, gameType);
		setRegicideGame(gameType);
	}

	private void setRegicideGame(GameType gameType) {
		regicide = (Regicide) gameType;
	}
	
	public Player getKing() {
		return regicide.getKing();
	}
	
	public boolean kingIsClose() {
		if(aiPlayer.equals(getKing()))
			return false;
		return getPlayerCalc().isTooClose(getKing().getLocation());
	}
}
