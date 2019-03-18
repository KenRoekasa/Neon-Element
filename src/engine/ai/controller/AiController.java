package engine.ai.controller;

import java.util.ArrayList;
import java.util.Random;

import engine.ScoreBoard;
import engine.ai.actions.AiActions;
import engine.ai.actions.AiHillStateActions;
import engine.ai.actions.AiKillsStateActions;
import engine.ai.actions.AiRegicideStateActions;
import engine.ai.actions.AiStateActions;
import engine.ai.actions.AiTimedStateActions;
import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.HillCalculations;
import engine.ai.calculations.RegicideCalculations;
import engine.ai.enums.AiStates;
import engine.ai.enums.AiType;
import engine.ai.fsm.FSMManager;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.PowerUpType;
import engine.gameTypes.GameType;
import engine.gameTypes.HillGame;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class AiController {

		AiStates activeState;
		ArrayList<PhysicsObject> objects;
		Player aiPlayer ;
		Player player;
		AiType aiType;
		AiStateActions stateActions;
		AiCalculations calc;
		FSMManager fsmManager;
		public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player, AiType aiType, ScoreBoard scoreboard, GameType gameType) {
	    	
			this.activeState = AiStates.IDLE;
	        this.objects = objects;
	        this.player = player;
	        this.aiPlayer = aiPlayer;
	        this.aiType = aiType;
	        initializeCalculations(gameType, map, scoreboard, gameType);
	        fsmManager = new FSMManager (aiPlayer, this, calc, gameType);
	        AiActions actions = new AiActions(this, calc, map);
	        initializeStateActions(calc, actions, gameType);
	        
	        //default random
	        actions.assignRandomElement();
	        System.out.println("started ai\n difficulty: "+String.valueOf(aiType)+"\n\n");
	    }
		
		public void update() {
			fsmManager.fetchAction();
			stateActions.executeAction();
		}
		
		public void setState(AiStates s) {
			activeState = s;
		}
		
		public ArrayList<PhysicsObject> getObjects() {
			return objects;
		}
		
		public AiStates getActiveState() {
			return activeState;
		}
		
		public Player getAiPlayer() {
			return aiPlayer;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		public AiType getAiType() {
			return aiType;
		}
		
		private void initializeStateActions(AiCalculations calc, AiActions actions, GameType gt) {
			switch(gt.getType()) {
			case FirstToXKills:
				stateActions = new AiKillsStateActions(this, calc, actions);
				break;
			case Hill:
				stateActions = new AiHillStateActions(this, calc, actions);
				break;
			case Regicide:
				stateActions = new AiRegicideStateActions(this, calc, actions);
				break;
			case Timed:
				stateActions = new AiTimedStateActions(this, calc, actions);
				break;
			default:
				break;
			
			}
		}
		
		private void initializeCalculations(GameType gt, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
			switch(gt.getType()) {
			case FirstToXKills:
				calc = new AiCalculations(this, map, scoreboard, gameType);
				break;
			case Hill:
				calc = new HillCalculations(this, map, scoreboard, gameType);
				break;
			case Regicide:
				calc =  new RegicideCalculations(this, map, scoreboard, gameType);
				break;
			case Timed:
				calc = new AiCalculations(this, map, scoreboard, gameType);
				break;
			default:
				break;
			
			}
		}
	
}
