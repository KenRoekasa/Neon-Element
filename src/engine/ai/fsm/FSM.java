package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.PlayersCalculations;
import engine.ai.calculations.PowerupCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;

public abstract class FSM {

	protected Player aiPlayer;
	protected AiController aiCon;
	protected AiCalculations calc;
	protected float maxHP ;
	protected PowerupCalculations puCalc;
	protected PlayersCalculations playerCalc;
	
	public FSM(Player aiPlayer, AiController aiCon,AiCalculations calc) {
		this.aiPlayer = aiPlayer;
		this.aiCon = aiCon;
		this.calc = calc;
		puCalc = calc.getPowerupCalc();
		playerCalc = calc.getPlayerCalc();
		maxHP = aiPlayer.getMAX_HEALTH();
	}
	
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
	
	protected abstract void easyAiFetchAction() ;
	protected abstract void normalAiFetchAction() ;
	protected abstract void hardAiFetchAction() ;
}
