package engine.ai.actions;

import java.util.Random;

import engine.ai.calculations.AiCalculations;
import engine.ai.calculations.MovementCalculations;
import engine.ai.calculations.PlayersCalculations;
import engine.ai.calculations.TimeCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.model.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

//Low level actions, that includes movement, attacks, shielding, and elements control.
//Used in state action classes to build state action methods
public class AiActions {
	
	//Number of ticks before putting shield back on
	private static final int NUMBER_OF_TICKS = 60;
	//The AI controller object
	protected AiController aiCon;
	//Object of the Player being controlled
	protected Player aiPlayer;
	//Game map object  
	protected Rectangle map;
	//Object of AI calculations used to access more specific AI calculation objects
	protected AiCalculations calc;
	//Time calculation object, used to calculate time for things like changing element every X seconds
	protected TimeCalculations timeCalc;
	//Movement calculations object, used to calculate distance and angles.
	protected MovementCalculations moveCalc;
	//Player calculations object, used to know stuff like, if player is: charging, too close, in attack distance. And to access players objects.
	protected PlayersCalculations playerCalc;
	//Wandering direction variable, used to determine which of the 8 possible movements to use when in Wandering state 
	protected int wanderingDirection ;
	//Random object, used to calculate the wandering direction randomly
	protected Random r;
	
	/**
	 * Initialises an AiActions object
	 * @param aiCon the AI controller object
	 * @param calc AI calculation object
	 * @param map Game map 
	 */
	public AiActions(AiController aiCon, AiCalculations calc, Rectangle map) {
		
		this.calc = calc;
		this.aiCon = aiCon;
		this.map = map;
		timeCalc = calc.getTimeCalc();
		moveCalc = calc.getMoveCalc();
		playerCalc = calc.getPlayerCalc();
		aiPlayer = aiCon.getAiPlayer();
		r = new Random();
		//default random
        assignRandomElement();
	}

	/**
	 * Assigns the AI player an element at random
	 */
	public void assignRandomElement() {

		Random r = new Random();
		int rand = r.nextInt(4);
		switch (rand) {
		case 0:
			aiPlayer.changeToEarth();
			break;
		case 1:
			aiPlayer.changeToFire();
			break;
		case 2:
			aiPlayer.changeToWater();
			break;
		case 3:
			aiPlayer.changeToAir();
			break;
		}
	}
	
	/**
	 * Assigns the AI player a random element that is different than the current one
	 * For example, if the current element is Fire, then this method will assign either Water, Air or Earth.
	 */
	public void assignDifferentRandomElement() {

		Random r = new Random();
		int rand = r.nextInt(3);
		switch(aiPlayer.getCurrentElement()) {
		case AIR:
			switch (rand) {
			case 0:
				aiPlayer.changeToEarth();
				break;
			case 1:
				aiPlayer.changeToFire();
				break;
			case 2:
				aiPlayer.changeToWater();
				break;
			}
			break;
		case EARTH:
			switch (rand) {
			case 0:
				aiPlayer.changeToAir();
				break;
			case 1:
				aiPlayer.changeToFire();
				break;
			case 2:
				aiPlayer.changeToWater();
				break;
			}
			break;
		case FIRE:
			switch (rand) {
			case 0:
				aiPlayer.changeToEarth();
				break;
			case 1:
				aiPlayer.changeToAir();
				break;
			case 2:
				aiPlayer.changeToWater();
				break;
			}
			break;
		case WATER:
			switch (rand) {
			case 0:
				aiPlayer.changeToEarth();
				break;
			case 1:
				aiPlayer.changeToFire();
				break;
			case 2:
				aiPlayer.changeToAir();
				break;
			}
			break;
		}
	}
	
	/**
	 * Moves away from the player.
	 * Tries to maximise distance from the player given as parameter.
	 * @param player the player to move away from
	 */
	public void moveAwayFromPlayer(Player player) {
		
		attackIfInDistanceWithShield(player);
		
		if(moveCalc.reachedAnEdge()) 
			moveAwayFromEdge();
		else 
			simpleMovement(player.getLocation(),aiPlayer.getLocation());
	}
	
	/**
	 * This method should be called when an AI player has reached an edge either wandering or running away from someone.
	 * It finds out where the edge is an moves in opposite direction.
	 */
	public void moveAwayFromEdge() {
		int i = moveCalc.closestEdgeLocation();
		switch(i) {
		case 0:down();break;
		case 1:rightcart();break;
		case 2:downcart();break;
		case 3:right();break;
		case 4:upcart();break;
		case 5:left();break;
		case 6:leftcart();break;
		case 7:up();break;
		}
	}
	
	/**
	 * Moves the AI player to the power up given its index in 'objects' and its location. 
	 * @param powerupIndex The index of the power up in objects, uses it to make sure the power up exist
	 * @param loc the location of the power up given as a Point2D object
	 */
	public void moveTo(int powerupIndex, Point2D loc) {
		
		Player player = playerCalc.getNearestPlayer();
		attackIfInDistanceWithShield(player);

		double distance = moveCalc.calcDistance(aiPlayer.getLocation(), loc);

		if(!((int) distance > 2 || powerupIndex == -1))
			return;
		simpleMovement(aiPlayer.getLocation(), loc);

	}

	/**
	 * Abstract movement method given two locations.
	 * Can be give the AI's location as the first argument and the destination as the second to move the player to the destination.
	 * Or can be given an oppnent's location as the first argument and the AI's location as the second to move away from an opponent.
	 * @param myLoc First location given as a Point2D object
	 * @param theirLoc Second location given as a Point2D object
	 */
	public void simpleMovement(Point2D myLoc, Point2D theirLoc) {
		double myX = myLoc.getX();
		double myY = myLoc.getY();
		double theirX = theirLoc.getX();
		double theirY = theirLoc.getY();
		
		if( myX > theirX && myY > theirY )
			up();
		else if( myX > theirX && (int)myY == (int)theirY )
			leftcart();
		else if( myX > theirX && myY < theirY )
			left();
		
		else if( (int)myX == (int)theirX && myY > theirY )
			upcart();
		else if( (int)myX == (int)theirX && myY < theirY )
			downcart();
		
		else if( myX < theirX && (int)myY == (int)theirY )
			rightcart();
		else if( myX < theirX && myY > theirY )
			right();
		else if( myX < theirX && myY < theirY )
			down();
		
	}
	
	//Moves the AI player up
	private void up() {aiPlayer.moveUp();}
	//Moves the AI player down
	private void down() {aiPlayer.moveDownCartestian(map.getWidth());}
	//Moves the AI player left
	private void left() {aiPlayer.moveLeft(map.getWidth());}
	//Moves the AI player right
	private void right() {aiPlayer.moveRight(map.getWidth(), map.getHeight());}
	//Moves the AI player up cartesian
	private void upcart() {aiPlayer.moveUpCartesian();}
	//Moves the AI player down cartesian
	private void downcart() {aiPlayer.moveDownCartestian(map.getWidth());};
	//Moves the AI player left cartesian
	private void leftcart() {aiPlayer.moveLeftCartesian();};
	//Moves the AI player right cartesian
	private void rightcart() {aiPlayer.moveRightCartesian(map.getWidth());};
	
	/**
	 * Moves the AI player to a player given its Player object, and keeps reasonable distance when it gets close to the player so they do not collide.
	 * Sets attack angle to that player.
	 * @param player The player to move to
	 */
	public void moveTo(Player player) {

		Point2D playerLoc = player.getLocation();
		double distance = moveCalc.calcDistance(aiPlayer.getLocation(), playerLoc);
		
		if( player.getTag().equals(ObjectType.PLAYER) && distance <= 2*aiPlayer.getWidth()) 
			return;
		if( player.getTag().equals(ObjectType.ENEMY) && distance <= 2) 
			return;
			
		aiPlayer.setPlayerAngle(new Rotate(moveCalc.calcAngle(aiPlayer.getLocation(),player.getLocation())));
		
		simpleMovement(aiPlayer.getLocation(), playerLoc);

	}
	
	/**
	 * Moves to a player and keeps distance if they are charging a heavy attack, so the AI is not affected.
	 * @param player The player to move to
	 */
	public void moveToAndKeepDistance(Player player) {
	
		if(playerCalc.isCharging(aiPlayer)) {
			if(moveCalc.calcDistance(aiPlayer.getLocation(), player.getLocation()) > map.getWidth()*0.075) {
				moveTo(player);
			}
			else {
				moveAwayFromPlayer(player);
			}
		}
		else {
			moveTo(player);
		}
		
	}
	
	/**
	 * Changes AI player's element if the number of seconds have passed since last time element changed.
	 * @param seconds Number of seconds after which element changes.
	 */
	public void changeToRandomElementAfter(int seconds) {
		if(timeCalc.secondsElapsed() >= seconds) {
			timeCalc.setStartTime(System.nanoTime()/1000000000);
			assignDifferentRandomElement();
		}
	}

	/**
	 * Makes AI player wander in a random direction 
	 * Used by easy mode AI, and sometimes normal mode as well.
	 */
	public void startWandering() {
		
		Player player = playerCalc.getNearestPlayer();
		attackIfInDistance(player);

		if(moveCalc.reachedAnEdge()) {
			switch(wanderingDirection) {
			case 0:wanderingDirection = 1;break;
			case 1:wanderingDirection = 0;break;
			case 2:wanderingDirection = 3;break;
			case 3:wanderingDirection = 2;break;
			case 4:wanderingDirection = 5;break;
			case 5:wanderingDirection = 4;break;
			case 6:wanderingDirection = 7;break;
			case 7:wanderingDirection = 8;break;
			}
		}

		switch(wanderingDirection) {
		case 0:down();break;
		case 1:up();break;
		case 2:left();break;
		case 3:right();break;
		case 4:downcart();break;
		case 5:upcart();break;
		case 6:leftcart();break;
		case 7:rightcart();break;
		}
	}
	
	/**
	 * Changes AI element to a one that maximises damage given if it is in an attacking state, or 
	 * to an element that minimises damage taken if it is in a non-attacking state.
	 * Mainly uses by hard mode enemies, but normal mode enemies uses it in some game types.
	 */
	public void changeToBefittingElement() {
		Player player = playerCalc.getNearestPlayer();
		switch(aiCon.getActiveState()) {
		
		//when attacking, change element to maximize damage given
		case AGGRESSIVE_ATTACK:
		case ATTACK:
		case ATTACK_WINNER:
			switch(player.getCurrentElement()) {
			case WATER:
			case AIR:
				aiPlayer.changeToFire();
				break;
			case EARTH:
			case FIRE:
				aiPlayer.changeToWater();
				break;
			}
			break;
			
		//when not attacking, change element to minimize damage received
		case ESCAPE:
		case FIND_DAMAGE:
		case FIND_HEALTH:
		case FIND_SPEED:
		case WANDER:
		case IDLE:
		case GO_TO_HILL:
		case WANDER_ON_HILL:
			switch(player.getCurrentElement()) {
			case EARTH:
			case FIRE:
				aiPlayer.changeToWater();
				break;
			case AIR:
				aiPlayer.changeToEarth();
				break;
			case WATER:
				aiPlayer.changeToAir();
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Attacks the player given as an argument if that player is in attacking distance from the AI.
	 * @param player The player to attack if they were in attackable distance.
	 */
	public void attackIfInDistance(Player player) {
		
		if (playerCalc.inAttackDistance(player) && player.getHealth()>0 && !playerCalc.isCharging(player)) {
			int i = r.nextInt(10);
			if(i>7)
				aiPlayer.chargeHeavyAttack();
			else 
				aiPlayer.lightAttack();
		}
	}
	
	/**
	 * Attacks the player given as an argument if that player is in attacking distance from AI.
	 * This method is used when the AI is in defence mode, and has its shield on. So it attacks the player and puts the shield back on
	 * @param player The player to attack if they were in attackable distance.
	 */
	public void attackIfInDistanceWithShield(Player player) {
		
		if (playerCalc.inAttackDistance(player) && player.getHealth()>0 && !playerCalc.isCharging(player)) {
			int i = r.nextInt(10);
			aiPlayer.unShield();
			if(i>7)
				aiPlayer.chargeHeavyAttack();
			else 
				aiPlayer.lightAttack();
			if(timeCalc.gameTicked(NUMBER_OF_TICKS))
				aiPlayer.shield();
		}
	}
	
	/**
	 * Puts shield on if and only if another player is nearby, otherwise, remove shield
	 */
	public void shieldWhenAlone() {
		if(playerCalc.playerIsTooClose() ) { 
			if(timeCalc.gameTicked(NUMBER_OF_TICKS))
				aiPlayer.shield();
		}
		else 
			aiPlayer.unShield();
	}

	public void setWanderingDirection(int dir) {
		wanderingDirection = dir;
	}


}
