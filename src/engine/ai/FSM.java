package engine.ai;

import engine.calculations.AiCalculations;
import engine.entities.Player;

public abstract class FSM {

	Player aiPlayer;
	AiController aiCon;
	AiCalculations calc;
	float maxHP ;
	
	public FSM(Player aiPlayer, AiController aiCon,AiCalculations calc) {
		this.aiPlayer = aiPlayer;
		this.aiCon = aiCon;
		this.calc = calc;
		maxHP = aiPlayer.getMAX_HEALTH();
	}
	public abstract void easyAiFetchAction() ;
	public abstract void normalAiFetchAction() ;
	public abstract void hardAiFetchAction() ;
}
