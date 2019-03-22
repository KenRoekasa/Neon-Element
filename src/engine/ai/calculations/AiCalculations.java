package engine.ai.calculations;


import engine.ScoreBoard;
import engine.ai.calculations.statecalculations.HillCalculations;
import engine.ai.calculations.statecalculations.KillsCalculations;
import engine.ai.calculations.statecalculations.RegicideCalculations;
import engine.ai.calculations.statecalculations.TimedCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

public abstract class AiCalculations {
	
	protected GameType gameType;
	protected ScoreBoard scoreboard;
	protected Player aiPlayer;
	protected Rectangle map;
	protected PlayersCalculations playerCalc;
	protected MovementCalculations moveCalc;
	protected PowerupCalculations puCalc;
	protected TimeCalculations timeCalc;
	
	public AiCalculations(AiController aiCon,Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		moveCalc = new MovementCalculations(aiCon, map);
		playerCalc = new PlayersCalculations(aiCon, map, scoreboard, gameType, moveCalc);
		puCalc = new PowerupCalculations(aiCon, map, playerCalc);
		timeCalc = new TimeCalculations();
		this.map = map;
		this.aiPlayer = aiCon.getAiPlayer();
	}
	
	/**
	 * initialises AI calculations object relevant to game type 
	 * @param map map of game
	 * @param scoreboard score board 
	 * @param gameType game type
	 */
	public static AiCalculations initializeAiCalculations(Rectangle map, ScoreBoard scoreboard, GameType gameType, AiController aiCon) {
		switch(gameType.getType()) {
		case FirstToXKills:
			return new KillsCalculations(aiCon, map, scoreboard, gameType);
		case Hill:
			return new HillCalculations(aiCon, map, scoreboard, gameType);
		case Regicide:
			return new RegicideCalculations(aiCon, map, scoreboard, gameType);
		case Timed:
			return new TimedCalculations(aiCon, map, scoreboard, gameType);
		default:
			return null;
		}
	}

	public PlayersCalculations getPlayerCalc() {
		return playerCalc;
	}
	
	public MovementCalculations getMoveCalc() {
		return moveCalc;
	}
	
	public PowerupCalculations getPowerupCalc() {
		return puCalc;
	}
	
	public TimeCalculations getTimeCalc() {
		return timeCalc;
	}
	
	
}
