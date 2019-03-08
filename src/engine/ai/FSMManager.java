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
		createProperFSM(aiPlayer, aiCon, calc, gameType);
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
	
	private void createProperFSM(Player aiPlayer, AiController aiCon2, AiCalculations calc, GameType gameType) {
		switch(gameType.getType()) {
		case Timed:
		case FirstToXKills:
			fsm = new TimedFSM(aiPlayer, aiCon, calc);
			break;
		case Hill:
			fsm = new HillFSM(aiPlayer, aiCon2, calc);
			break;
		case Regicide:
			break;
		default:
			break;
		
		}
	}
	
}
