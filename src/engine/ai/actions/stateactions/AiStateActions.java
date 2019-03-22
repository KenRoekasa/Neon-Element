package engine.ai.actions.stateactions;

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
	
	protected Player aiPlayer;
	protected ArrayList<Player> realPlayers;
	protected AiController aiCon;
	protected AiCalculations calc;
	protected AiActions actions;
	protected PowerupCalculations puCalc;
	protected PlayersCalculations playerCalc;
	protected TimeCalculations timeCalc;
	protected boolean wandering = false;
	
	public AiStateActions(AiController aiCon, AiCalculations calc, AiActions actions) {
		
		this.calc = calc;
		puCalc = calc.getPowerupCalc();
		playerCalc = calc.getPlayerCalc();
		timeCalc = calc.getTimeCalc();
		
		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.realPlayers = playerCalc.getRealPlayers();
		this.actions = actions;
		
	}
	
	public static AiStateActions  initializeStateActions(AiCalculations calc, Rectangle map, GameType gameType, AiController aiCon) {
		
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
	
	protected void idle() {
		Player player = playerCalc.getNearestPlayer();
		actions.attackIfInDistanceWithShield(player);
	}

	public void setWandering(boolean bool) {
		wandering = bool;
	}
	
	public boolean isWandering() {
		return wandering;
	}

	protected void wander() {
		aiPlayer.unShield();
		updateWandering();
		actions.startWandering();
	}
	
	protected void findSpeed() {
		actions.shieldWhenAlone();
		int index = puCalc.getNearestPowerUp(PowerUpType.SPEED);
		if (index != -1)
			actions.moveTo(index, puCalc.getPowerups().get(index).getLocation());
	}

	protected void findDamage() {
		actions.shieldWhenAlone();
		int index = puCalc.getNearestPowerUp(PowerUpType.DAMAGE);
		if (index != -1)
			actions.moveTo(index, puCalc.getPowerups().get(index).getLocation());
	}

	protected void findHealth() {
		actions.shieldWhenAlone();
		int index = puCalc.getNearestPowerUp(PowerUpType.HEAL);
		if (index != -1)
			actions.moveTo(index, puCalc.getPowerups().get(index).getLocation());
	}

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
	
	protected void escape() {
		actions.shieldWhenAlone();
		Player player = playerCalc.getNearestPlayer();
		actions.moveAwayFromPlayer(player);
	}	
	
	protected void attack() {
		aiPlayer.unShield();
		Player player = playerCalc.getNearestPlayer();
		actions.moveTo(player);
		actions.attackIfInDistance(player);
	}

	protected void attackWinner() {
		aiPlayer.unShield();
		Player player = playerCalc.getWinningPlayer();
		aiPlayer.chargeHeavyAttack();
		actions.moveTo(player);
		
		actions.attackIfInDistance(player);
				
	}
	
	protected void updateElement() {
		if(aiCon.getAiType().equals(AiType.EASY))
			actions.changeToRandomElementAfter(15);
		else
			actions.changeToBefittingElement();
	}

	protected void updateWandering() {
		if(isWandering() && !aiCon.getActiveState().equals(AiStates.WANDER))
			setWandering(false);
		else if( (!isWandering() && aiCon.getActiveState().equals(AiStates.WANDER)) || timeCalc.hasBeenWanderingFor(5) ) {
			setWandering(true);
			Random r = new Random();
			actions.setWanderingDirection(r.nextInt(8));
		}
	}
	

}
