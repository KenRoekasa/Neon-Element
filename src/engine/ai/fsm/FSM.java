package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.PlayersCalculations;
import engine.ai.calculations.PowerupCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;

public abstract class FSM {

	//player object being controlled
	protected Player aiPlayer;
	//AI controller object
	protected AiController aiCon;
	//Maximum possible HP
	protected float maxHP ;
	//PowerupCalculations object
	protected PowerupCalculations puCalc;
	//PlayersCalculations object
	protected PlayersCalculations playerCalc;
	
	/**
	 * @param aiPlayer Player object being controlled
	 * @param aiCon AI controller object
	 * @param calc AI calculations object
	 */
	public FSM(Player aiPlayer, AiController aiCon,AiCalculations calc) {
		this.aiPlayer = aiPlayer;
		this.aiCon = aiCon;
		puCalc = calc.getPowerupCalc();
		playerCalc = calc.getPlayerCalc();
		maxHP = aiPlayer.getMAX_HEALTH();
	}
	
	/**
	 * Initialises and returns FSM sub class relevant to game type
	 * @param aiPlayer Player object being controlled
	 * @param calc AI calculations object
	 * @param gameType Game type object
	 * @param aiCon AI controller object
	 * @return FSM sub class relevant to game type
	 */
	public static FSM initializeFSM(Player aiPlayer, AiCalculations calc, GameType gameType, AiController aiCon) {
		switch(gameType.getType()) {
		case Timed:
			return new TimedFSM(aiPlayer, aiCon, calc);
		case FirstToXKills:
			return new KillsFSM(aiPlayer, aiCon, calc);
		case Hill:
			return new HillFSM(aiPlayer, aiCon, calc);
		case Regicide:
			return new RegicideFSM(aiPlayer, aiCon, calc);
		default:
			return null;
		}
	}

	/**
	 * Calls fetch action method relevant to AI type
	 */
	public void fetchAction() {
		
		switch(aiCon.getAiType()) {
		case EASY:
			easyAiFetchAction();
			break;
		case NORMAL:
			normalAiFetchAction();
			break;
		case HARD:
			hardAiFetchAction();
			break;
		}
	}
	
	/**
	 * Fetch actions for easy level AI. Sets AI state given everything going on in the game
	 */
	protected abstract void easyAiFetchAction() ;
	/**
	 * Fetch actions for normal level AI. Sets AI state given everything going on in the game
	 */
	protected abstract void normalAiFetchAction() ;
	/**
	 * Fetch actions for hard level AI. Sets AI state given everything going on in the game
	 */
	protected abstract void hardAiFetchAction() ;
}
