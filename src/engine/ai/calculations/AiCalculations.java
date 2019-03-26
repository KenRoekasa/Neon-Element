package engine.ai.calculations;


import engine.ScoreBoard;
import engine.ai.calculations.stateCalculations.HillCalculations;
import engine.ai.calculations.stateCalculations.KillsCalculations;
import engine.ai.calculations.stateCalculations.RegicideCalculations;
import engine.ai.calculations.stateCalculations.TimedCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

public abstract class AiCalculations {
	
	//Game type object
	protected GameType gameType;
	//Score board object
	protected ScoreBoard scoreboard;
	//player object being controlled
	protected Player aiPlayer;
	//Map of game
	protected Rectangle map;
	//Players calculations object, used to carry out player related calculations
	protected PlayersCalculations playerCalc;
	//Movement calculations object, used to carry out movement related calculations
	protected MovementCalculations moveCalc;
	//Power up calculations object, used to carry out power up related calculations
	protected PowerupCalculations puCalc;
	//Time calculations object, used to carry out time related calculations
	protected TimeCalculations timeCalc;
	
	/**
	 * @param aiCon AI controller object
	 * @param map Map of game
	 * @param scoreboard score board object
	 * @param gameType Game type
	 */
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

	/**
	 * @return PlayersCalculations object
	 */
	public PlayersCalculations getPlayerCalc() {
		return playerCalc;
	}
	/**
	 * @return MovementCalculations object
	 */
	public MovementCalculations getMoveCalc() {
		return moveCalc;
	}
	
	/**
	 * @return PowerupCalculations object
	 */
	public PowerupCalculations getPowerupCalc() {
		return puCalc;
	}
	
	/**
	 * @return TimeCalculations object
	 */
	public TimeCalculations getTimeCalc() {
		return timeCalc;
	}
	
	
}
