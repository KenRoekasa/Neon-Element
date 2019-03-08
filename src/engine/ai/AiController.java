package engine.ai;

import java.util.ArrayList;
import java.util.Random;

import engine.ScoreBoard;
import engine.calculations.AiCalculations;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.AiStates;

import engine.enums.AiType;
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
		AiFSM brain;
		public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player, AiType aiType, ScoreBoard scoreboard, GameType gameType) {
	    	
			activeState = AiStates.IDLE;
	        this.objects = objects;
	        this.player = player;
	        this.aiPlayer = aiPlayer;
	        this.aiType = aiType;
	        
	        AiCalculations calc = new AiCalculations(this, map, scoreboard, gameType);
	        AiActions actions = new AiActions(this, calc, map);
	        
	        stateActions = new AiStateActions(this, calc, actions);
	        brain = new AiFSM (aiPlayer, this, calc, gameType);
	        
	        //default random
	        actions.assignRandomElement();
	        System.out.println("started ai\n difficulty: "+String.valueOf(aiType)+"\n\n");
	    }
		
		public void update() {
			brain.fetchAction();
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
		
	
}
