package engine.ai.actions;

import java.util.Random;

import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

//low level actions used by state actions and in ai controller
public class AiActions {
	

	int wanderingDirection ;
	boolean spamming = false;
	AiController aiCon;
	AiCalculations calc;
	Player aiPlayer;
	Rectangle map;
	Random r;
	public AiActions(AiController aiCon, AiCalculations calc, Rectangle map) {
		this.calc = calc;
		this.aiCon = aiCon;
		this.map = map;
		aiPlayer = aiCon.getAiPlayer();
		r = new Random();
	}

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

	public void moveAwayFromPlayer(Player player) {
		
		attackIfInDistanceWithShield(player);
		
		if(calc.reachedAnEdge()) 
			moveAwayFromEdge();
		else 
			simpleMovement(player.getLocation(),aiPlayer.getLocation());
	}
	
	
	public void moveAwayFromEdge() {
		int i = calc.closestEdgeLocation();
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
	

	public void moveTo(int powerupIndex, Point2D loc) {
		
		Player player = calc.getNearestPlayer();
		attackIfInDistanceWithShield(player);

		double distance = calc.calcDistance(aiPlayer.getLocation(), loc);

		if(!((int) distance > 2 || powerupIndex == -1))
			return;
		simpleMovement(aiPlayer.getLocation(), loc);

	}

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
	
	void up() {aiPlayer.moveUp();}
	void down() {aiPlayer.moveDownCartestian(map.getWidth());}
	void left() {aiPlayer.moveLeft(map.getWidth());}
	void right() {aiPlayer.moveRight(map.getWidth(), map.getHeight());}
	void upcart() {aiPlayer.moveUpCartesian();}
	void downcart() {aiPlayer.moveDownCartestian(map.getWidth());};
	void leftcart() {aiPlayer.moveLeftCartesian();};
	void rightcart() {aiPlayer.moveRightCartesian(map.getWidth());};
	
	public void moveTo(Player player) {

		Point2D playerLoc = player.getLocation();
		double distance = calc.calcDistance(aiPlayer.getLocation(), playerLoc);
		
		if( player.getTag().equals(ObjectType.PLAYER) && distance <= 2*aiPlayer.getWidth()) 
			return;
		if( player.getTag().equals(ObjectType.ENEMY) && distance <= 2) 
			return;
			
		aiPlayer.setPlayerAngle(new Rotate(calc.calcAngle(aiPlayer.getLocation(),player.getLocation())));
		
		simpleMovement(aiPlayer.getLocation(), playerLoc);

	}
	

	public void moveToOnHill(Player player) {
		if(calc.closeToHill() && calc.onHill(aiPlayer.getLocation()))
			moveTo(player);
		
	}
	
	public void changeToRandomElementAfter(int seconds) {
		if(calc.secondsElapsed() >= seconds) {
			calc.setStartTime(System.nanoTime()/1000000000);
			assignDifferentRandomElement();
		}
	}


	public void startWandering() {
		
		Player player = calc.getNearestPlayer();
		attackIfInDistance(player);

		if(calc.reachedAnEdge()) {
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
	
	public void changeToBefittingElement() {
		Player player = calc.getNearestPlayer();
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
		case ESCAPE_ON_HILL:
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

	public void attackIfInDistance(Player player) {
		
		if (calc.inAttackDistance(player) && player.getHealth()>0 && !calc.isCharging(aiPlayer)) {
			int i = r.nextInt(10);
			if(i>7)
				aiPlayer.chargeHeavyAttack();
			else 
				aiPlayer.lightAttack();
		}
	}
	
	
	public void attackIfInDistanceWithShield(Player player) {
		
		if (calc.inAttackDistance(player) && player.getHealth()>0 && !calc.isCharging(aiPlayer)) {
			int i = r.nextInt(10);
			aiPlayer.unShield();
			if(i>7)
				aiPlayer.chargeHeavyAttack();
			else 
				aiPlayer.lightAttack();
			aiPlayer.shield();
		}
	}
	
	public void shieldWhenAlone() {
		if(calc.playerIsTooClose())
			aiPlayer.shield();
		else 
			aiPlayer.unShield();
	}


	
	//	public void spam(int time) {
	//	
	//	spammingTimer(time);
	//	while (spamming) {
	//		aiPlayer.delay(DELAY_TIME);
	//		switch (activeState) {
	//		case ATTACK:
	//			aiPlayer.unShield();
	//			attack();
	//			break;
	//		case AGGRESSIVE_ATTACK:
	//			aiPlayer.unShield();
	//			aggressiveAttack();
	//			break;
	//		case FIND_HEALTH:
	//			aiPlayer.shield();
	//			findHealth();
	//			break;
	//		case FIND_DAMAGE:
	//			aiPlayer.shield();
	//			findDamage();
	//			break;
	//		case FIND_SPEED:
	//			aiPlayer.shield();
	//			findSpeed();
	//			break;
	//		case ESCAPE:
	//			aiPlayer.shield();
	//			escape();
	//			break;
	//		case WANDER:
	//			aiPlayer.unShield();
	//			startWandering();
	//		case IDLE:
	//			break;
	//		default:
	//			break;
	//		}
	//	}
	//	
	//}
	//
	//private void spammingTimer(int time) {
	//	spamming = true;
	//	Thread t = new Thread(new Runnable() {
	//		@Override
	//		public void run() {
	//		aiPlayer.delay(time);
	//		spamming = false;
	//		}
	//
	//	});
	//	t.start();
	//}




}
