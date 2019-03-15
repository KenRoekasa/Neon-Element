package engine.ai.calculations;


import engine.ScoreBoard;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

public class AiCalculations {
	
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
