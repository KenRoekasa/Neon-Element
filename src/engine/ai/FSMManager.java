package engine.ai;

import engine.calculations.AiCalculations;
import engine.entities.Character;
import engine.entities.Player;
import engine.enums.AiStates;
import engine.enums.PowerUpType;
import engine.gameTypes.GameType;
import engine.gameTypes.HillGame;

public class FSMManager {
	
	private AiController aiCon;
	private FSM fsm;
	
	public FSMManager(Player aiPlayer, AiController aiCon,AiCalculations calc, GameType gameType) {	
		this.aiCon = aiCon; 
		initializeProperFSM(aiPlayer, aiCon, calc, gameType);
	}
	
	public void fetchAction() {
		
		switch(aiCon.getAiType()) {
		case EASY:
			fsm.easyAiFetchAction();
			break;
		case NORMAL:
			fsm.normalAiFetchAction();
			break;
		case HARD:
			fsm.hardAiFetchAction();
			break;
		}
	}
	
	private void initializeProperFSM(Player aiPlayer, AiController aiCon2, AiCalculations calc, GameType gameType) {
		switch(gameType.getType()) {
		case Timed:
			fsm = new TimedFSM(aiPlayer, aiCon, calc);
			break;
		case FirstToXKills:
			fsm = new KillsFSM(aiPlayer, aiCon, calc);
			break;
		case Hill:
			fsm = new HillFSM(aiPlayer, aiCon2, calc);
			break;
		case Regicide:
			fsm = new RegicideFSM(aiPlayer, aiCon2, calc);
			break;
		default:
			break;
		
		}
	}
	
}
