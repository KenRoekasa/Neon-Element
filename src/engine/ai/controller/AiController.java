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
		FSMManager fsmManager;
		public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player, AiType aiType, ScoreBoard scoreboard, GameType gameType) {
	    	
			activeState = AiStates.IDLE;
	        this.objects = objects;
	        this.player = player;
	        this.aiPlayer = aiPlayer;
	        this.aiType = aiType;
	        
	        AiCalculations calc = new AiCalculations(this, map, scoreboard, gameType);
	        AiActions actions = new AiActions(this, calc, map);
	        
	        initializeProperStateActions(calc, actions, gameType);
	        fsmManager = new FSMManager (aiPlayer, this, calc, gameType);
	        
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
		
		private void initializeProperStateActions(AiCalculations calc, AiActions actions, GameType gt) {
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
	
}
