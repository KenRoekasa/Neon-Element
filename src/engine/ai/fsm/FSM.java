package engine.ai.fsm;

import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.PlayersCalculations;
import engine.ai.calculations.PowerupCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;

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
	public abstract void easyAiFetchAction() ;
	public abstract void normalAiFetchAction() ;
	public abstract void hardAiFetchAction() ;
}
