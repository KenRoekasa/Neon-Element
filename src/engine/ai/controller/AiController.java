 package engine.ai.controller;

import java.util.ArrayList;
import engine.ai.actions.stateActions.AiStateActions;
import engine.ai.calculations.AiCalculations;
import engine.ai.enums.AiStates;
import engine.ai.enums.AiType;
import engine.ai.fsm.FSM;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.model.GameType;
import engine.model.ScoreBoard;
import javafx.scene.shape.Rectangle;

public class AiController {

		//AI's current active state, set in FSM classes and executed in state action classes
		protected AiStates activeState;
		//objects available in game, like players and power ups
		protected ArrayList<PhysicsObject> objects;
		//player object being controlled
		protected Player aiPlayer;
		//AI type, level of difficulty
		protected AiType aiType;
		//AI calculations object, used to calculate everything needed to make a decision or an action
		protected AiCalculations calc;
		//FSM , sets AI state to a one relevant to game situation
		protected FSM fsm;
		//state actions, executes state actions 
		protected AiStateActions stateActions; 
		
		/**
		 * @param aiPlayer Player object that is controlled by AI
		 * @param objects  list of objects in game
		 * @param map map of game
		 * @param aiType easy, normal or hard AI
		 * @param scoreboard score board object
		 * @param gameType game type object
		 */
		public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, AiType aiType, ScoreBoard scoreboard, GameType gameType) {
	    	
			this.activeState = AiStates.IDLE;
	        this.objects = objects;
	        this.aiPlayer = aiPlayer;
	        this.aiType = aiType;
	        
	        calc = AiCalculations.initializeAiCalculations(map, scoreboard, gameType, this);
	        stateActions = AiStateActions.initializeStateActions(calc, map, gameType, this);
	        fsm = FSM.initializeFSM(aiPlayer, calc, gameType, this);

	    }
		
		/**
		 * notifies AI that the game got paused
		 */
		public void pause() {
			calc.getTimeCalc().setPaused(true);
		}
		
		/**
		 * updates AI, i.e. AI makes one decision and one action
		 */
		public void update() {
			calc.getTimeCalc().tick();
			fsm.fetchAction();
			stateActions.executeAction();
		}
		
		/**
		 * sets state of AI
		 * @param s AI state
		 */
		public void setState(AiStates s) {
			activeState = s;
		}
		
		/**
		 * @return list of objects in game
		 */
		public ArrayList<PhysicsObject> getObjects() {
			return objects;
		}
		
		/**
		 * @return AI's current active state
		 */
		public AiStates getActiveState() {
			return activeState;
		}
		
		/**
		 * @return Player object controlled by AI
		 */
		public Player getAiPlayer() {
			return aiPlayer;
		}

		/**
		 * @return AI type, level of difficulty, easy, normal, or hard
		 */
		public AiType getAiType() {
			return aiType;
		}
	
}
