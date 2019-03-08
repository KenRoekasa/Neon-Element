package engine.ai;

import engine.calculations.AiCalculations;
import engine.entities.Character;
import engine.entities.Player;
import engine.enums.AiStates;
import engine.enums.PowerUpType;
import engine.gameTypes.GameType;
import engine.gameTypes.HillGame;

public class AiFSM {
	AiController aiCon;
	TimedFSM timed;
	HillFSM hill;
	GameType gameType;
	public AiFSM(Player aiPlayer, AiController aiCon,AiCalculations calc, GameType gameType) {	
		this.aiCon = aiCon; 
		this.gameType = gameType;
		timed = new TimedFSM(aiPlayer, aiCon, calc);
		hill = new HillFSM(aiPlayer, aiCon, calc);
	}
	
	public void fetchAction() {
		
		switch(aiCon.getAiType()) {
		case EASY:
			easy();
			break;
		case NORMAL:
			normal();
			break;
		case HARD:
			hard();
			break;
		}
	}
	
	private void easy() {
		
		switch(gameType.getType()) {
		case Timed:
		case FirstToXKills:
			timed.easyAiFetchAction();
			break;
		case Hill:
			hill.easyAiFetchAction();
			break;
		case Regicide:
			break;
		default:
			break;
		
		}

	}
	
	private void normal() {
		
		switch(gameType.getType()) {
		case Timed:
		case FirstToXKills:
			timed.normalAiFetchAction();
			break;
		case Hill:
			hill.normalAiFetchAction();
			break;
		case Regicide:
			break;
		default:
			break;
		
		}

	}
	
	private void hard() {
		
		switch(gameType.getType()) {
		case Timed:
		case FirstToXKills:
			timed.hardAiFetchAction();
			break;
		case Hill:
			hill.hardAiFetchAction();
			break;
		case Regicide:
			break;
		default:
			break;
		
		}
		
	}
	
}
