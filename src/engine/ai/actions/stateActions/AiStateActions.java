package engine.ai.actions.stateActions;

import java.util.ArrayList;
import java.util.Random;

import engine.ai.actions.AiActions;
import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.PlayersCalculations;
import engine.ai.calculations.PowerupCalculations;
import engine.ai.calculations.TimeCalculations;
import engine.ai.controller.AiController;
import engine.ai.enums.AiStates;
import engine.ai.enums.AiType;
import engine.entities.Player;
import engine.enums.PowerUpType;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;
//high level actions, based on ai states 
public abstract class AiStateActions {
	
	//AI controlled player object
	protected Player aiPlayer;
	//list of non-AI-players
	protected ArrayList<Player> realPlayers;
	//AI controller object
	protected AiController aiCon;
	//AI actions object, used to form different state actions
	protected AiActions actions;
	//Power up calculations object, used to access and calculate power up related matters
	protected PowerupCalculations puCalc;
	//Player calculations object, used to access and calculate players related matters
	protected PlayersCalculations playerCalc;
	//Time calculations object, used to access and calculate timing related matters
	protected TimeCalculations timeCalc;
	//Wandering boolean, used to know if AI just started wandering or have been wandering for a while
	protected boolean wandering = false;
	
	/**
	 * @param aiCon AI controller object
	 * @param calc AI calculations object
	 * @param actions AI actions object
	 */
	public AiStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		
		puCalc = calc.getPowerupCalc();
		playerCalc = calc.getPlayerCalc();
		timeCalc = calc.getTimeCalc();
		
		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.realPlayers = playerCalc.getRealPlayers();
		this.actions = actions;
		
	}
	
	/**
	 * Initialises and returns an object of AiStateActions sub class relevant to game type
	 * @param calc AI calculations object
	 * @param map map of game
	 * @param gameType game type
	 * @param aiCon AI controller object
	 * @return an AiStateActions sub class
	 */
	public static AiStateActions initializeStateActions(AiCalculations calc, Rectangle map, GameType gameType, AiController aiCon) {
		
		AiActions actions = new AiActions(aiCon, calc, map);
		
		switch(gameType.getType()) {
		case FirstToXKills:
			return new AiKillsStateActions(aiCon, calc, actions);
		case Hill:
			return new AiHillStateActions(aiCon, calc, actions);
		case Regicide:
			return new AiRegicideStateActions(aiCon, calc, actions);
		case Timed:
			return new AiTimedStateActions(aiCon, calc, actions);
		default:
			return null;
		}
	}

	/**
	 * Executes a state action relevant to current AI state. Also updates AI element in an AI type relevant matter.
	 */
	public void executeAction() {
		updateElement();
		updateWandering();
		
		switch (aiCon.getActiveState()) {
		case ATTACK:
			attack();
			break;
		case AGGRESSIVE_ATTACK:
			aggressiveAttack();
			break;
		case FIND_HEALTH:
			findHealth();
			break;
		case FIND_DAMAGE:
			findDamage();
			break;
		case FIND_SPEED:
			findSpeed();
			break;
		case ESCAPE:
			escape();
			break;
		case WANDER:
			wander();
			break;
		case ATTACK_WINNER:
			attackWinner();
			break;
		case IDLE:
			idle();
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * Implements actions for idle AI state 
	 */
	protected void idle() {
		Player player = playerCalc.getNearestPlayer();
		actions.attackIfInDistanceWithShield(player);
	}

	/**
	 * Implements actions for wander AI state 
	 */
	protected void wander() {
		aiPlayer.unShield();
		updateWandering();
		actions.startWandering();
	}

	/**
	 * Implements actions for find speed AI state 
	 */
	protected void findSpeed() {
		actions.shieldWhenAlone();
		int index = puCalc.getNearestPowerUp(PowerUpType.SPEED);
		if (index != -1)
			actions.moveTo(index, puCalc.getPowerups().get(index).getLocation());
	}

	/**
	 * Implements actions for find damage AI state 
	 */
	protected void findDamage() {
		actions.shieldWhenAlone();
		int index = puCalc.getNearestPowerUp(PowerUpType.DAMAGE);
		if (index != -1)
			actions.moveTo(index, puCalc.getPowerups().get(index).getLocation());
	}

	/**
	 * Implements actions for find health AI state 
	 */
	protected void findHealth() {
		actions.shieldWhenAlone();
		int index = puCalc.getNearestPowerUp(PowerUpType.HEAL);
		if (index != -1)
			actions.moveTo(index, puCalc.getPowerups().get(index).getLocation());
	}

	/**
	 * Implements actions for aggressive attack AI state 
	 */
	protected void aggressiveAttack() {
		aiPlayer.unShield();
		Player player = playerCalc.getNearestPlayer();
		aiPlayer.chargeHeavyAttack();
		if(aiCon.getAiType().equals(AiType.HARD))
			actions.moveToAndKeepDistance(player);
		else
			actions.moveTo(player);
		actions.attackIfInDistance(player);
	}

	/**
	 * Implements actions for escape AI state 
	 */
	protected void escape() {
		actions.shieldWhenAlone();
		Player player = playerCalc.getNearestPlayer();
		if(playerCalc.isTooClose(player.getLocation()))
			actions.moveAwayFromPlayer(player);
		else
			wander();
	}	

	/**
	 * Implements actions for attack AI state 
	 */
	protected void attack() {
		aiPlayer.unShield();
		Player player = playerCalc.getNearestPlayer();
		actions.moveTo(player);
		actions.attackIfInDistance(player);
	}

	/**
	 * Implements actions for attack winner AI state 
	 */
	protected void attackWinner() {
		aiPlayer.unShield();
		Player player = playerCalc.getWinningPlayer();
		aiPlayer.chargeHeavyAttack();
		actions.moveTo(player);
		
		actions.attackIfInDistance(player);
				
	}

	/**
	 * Updates element according to AI type.
	 * If AI type is easy, then element is changed to a random one every 15 seconds.
	 * If AI type is normal or hard, element is changed to the benefit of AI current action.
	 */
	protected void updateElement() {
		if(aiCon.getAiType().equals(AiType.EASY))
			actions.changeToRandomElementAfter(15);
		else
			actions.changeToBefittingElement();
	}

	/**
	 * Decides when to give the AI a new direction to move towards.
	 * Taking into account when AI started wandering and the time AI have been wandering for. 
	 */
	protected void updateWandering() {
		if(wandering && !aiCon.getActiveState().equals(AiStates.WANDER))
			wandering = false;
		else if( (!wandering && aiCon.getActiveState().equals(AiStates.WANDER)) || timeCalc.hasBeenWanderingFor(5) ) {
			wandering = true;
			Random r = new Random();
			actions.setWanderingDirection(r.nextInt(8));
		}
	}
	
}
