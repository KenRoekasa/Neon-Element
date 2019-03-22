package engine.ai.calculations.statecalculations;

import engine.ScoreBoard;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;
import engine.gameTypes.Regicide;
import javafx.scene.shape.Rectangle;

public class RegicideCalculations extends AiCalculations{

	//Regicide game object
	protected Regicide regicide;
	
	/**
	 * @param aiCon AI controller object
	 * @param map Map of game
	 * @param scoreboard score board object
	 * @param gameType Game type
	 */
	public RegicideCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		super(aiCon, map, scoreboard, gameType);
		setRegicideGame(gameType);
	}

	/**
	 * Sets regicide to given object
	 * @param gameType Game type to extract regicide object from
	 */
	private void setRegicideGame(GameType gameType) {
		regicide = (Regicide) gameType;
	}
	
	/**
	 * @return Player object of current king
	 */
	public Player getKing() {
		return regicide.getKing();
	}
	
	/**
	 * Calculates distance between this AI player's location and that of the king
	 * @return True if distance between this AI player's location and that of the king is less than 0.2 of map's width, false if not or if this AI player is the king
	 */
	public boolean kingIsClose() {
		if(aiPlayer.equals(getKing()))
			return false;
		return getPlayerCalc().isTooClose(getKing().getLocation());
	}
}
