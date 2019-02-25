package engine.ai;

import java.util.Random;

import engine.entities.Player;
import engine.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class AiActions {
	

	int wanderingDirection ;
	boolean spamming = false;
	AiController aiCon;
	AiCalculations calc;
	Player aiPlayer;
	Rectangle map;
	
	public AiActions(AiController aiCon, AiCalculations calc, Rectangle map) {
		this.calc = calc;
		this.aiCon = aiCon;
		this.map = map;
		aiPlayer = aiCon.getAiPlayer();
		
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

	public void moveAway(Player player) {
		if(calc.reachedAnEdge()) {
			moveAwayFromEdge();
		}
		
		if (calc.inAttackDistance(player) && player.getHealth()>0) {
			aiPlayer.unShield();
			aiPlayer.lightAttack();
			aiPlayer.shield();
		}
			
		
		Point2D playerLoc = player.getLocation(), aiLoc = aiPlayer.getLocation();
		if(playerLoc.getX()>aiLoc.getX()) {
			if(playerLoc.getY()>aiLoc.getY())
				aiPlayer.moveUp();
			else if(playerLoc.getY()<aiLoc.getY())
				aiPlayer.moveLeft(map.getWidth());
			else
				aiPlayer.moveUpCartesian();
		}
		else if(playerLoc.getX()<aiLoc.getX()) {
			if(playerLoc.getY()>aiLoc.getY())
				aiPlayer.moveRight(map.getWidth(), map.getHeight());
			else if(playerLoc.getY()<aiLoc.getY())
				aiPlayer.moveDown(map.getWidth(), map.getHeight());
			else
				aiPlayer.moveDownCartestian(map.getHeight());
		}
		else {
			if(playerLoc.getY()>aiLoc.getY())
				aiPlayer.moveLeftCartesian();
			else if(playerLoc.getY()<aiLoc.getY())
				aiPlayer.moveRightCartesian(map.getWidth());
			else
				aiPlayer.moveDown(map.getWidth(), map.getHeight());
		}
		
		
	}
	
	
	public void moveAwayFromEdge() {
		double width = map.getWidth();
		double height = map.getHeight();
		int i = calc.closestEdgeLocation();
		if(i == 0)
			i = 2;
		else if(i == 2)
			i = 0;
		if(i == 1)
			i = 3;
		else if(i == 3)
			i = 1;
		if(i == 4)
			i = 7;
		else if(i == 7)
			i = 4;
		if(i == 5)
			i = 6;
		else if(i == 6)
			i = 5;
		switch(i) {
		case 0:aiPlayer.moveDownCartestian(height);break;
		case 1:aiPlayer.moveRight(width, height);break;
		case 2:aiPlayer.moveDown(width, height);break;
		case 3:aiPlayer.moveRightCartesian(width);break;
		case 4:aiPlayer.moveUp();break;
		case 5:aiPlayer.moveLeftCartesian();break;
		case 6:aiPlayer.moveLeft(width);break;
		case 7:aiPlayer.moveUpCartesian();break;
		}
	}
	

	public void moveTo(int powerupIndex, Point2D loc) {

		double distance = calc.calcDistance(aiPlayer.getLocation(), loc);

		if(!((int) distance > 2 || powerupIndex == -1))
			return;

			if (calc.isAbove(loc))
				aiPlayer.moveUp();
			else if (calc.isUnder(loc))
				aiPlayer.moveDown(map.getWidth(), map.getHeight());
			if (calc.isLeftOf(loc))
				aiPlayer.moveLeft(map.getWidth());
			else if (calc.isRightOf(loc))
				aiPlayer.moveRight(map.getWidth(), map.getHeight());
			else if (calc.higherY(loc))
				aiPlayer.moveDownCartestian(map.getWidth());
			else if (!calc.higherY(loc))
				aiPlayer.moveUpCartesian();
			else if (calc.higherX(loc))
				aiPlayer.moveLeftCartesian();
			else if (!calc.higherX(loc))
				aiPlayer.moveRightCartesian(map.getWidth());

	}

	public void moveTo(Player player) {

		Point2D playerLoc = player.getLocation();
		double distance = calc.calcDistance(aiPlayer.getLocation(), playerLoc);
		
		if( player.getTag().equals(ObjectType.PLAYER) && distance <= 2*aiPlayer.getWidth()) 
			return;
		if( player.getTag().equals(ObjectType.ENEMY) && distance <= 2) 
			return;
			
			aiPlayer.setPlayerAngle(new Rotate(calc.calcAngle(aiPlayer.getLocation(),player.getLocation())));

			if (calc.isAbove(playerLoc)) {
				aiPlayer.moveUp();
			}
			else if (calc.isUnder(playerLoc)) {
			//	System.out.println("going down");
				aiPlayer.moveDown(map.getWidth(), map.getHeight());
			}if (calc.isLeftOf(playerLoc)) {
				//System.out.println("going left");
				aiPlayer.moveLeft(map.getWidth());
			}else if (calc.isRightOf(playerLoc)) {
				//System.out.println("going right");
				aiPlayer.moveRight(map.getWidth(), map.getHeight());
			}else if (calc.isAbove(playerLoc)) {	
				//System.out.println("going up");
				aiPlayer.moveUp();
			}else if (calc.higherY(playerLoc)) {
//				System.out.println("going down cart");
				aiPlayer.moveDownCartestian(map.getWidth());
			}else if (!calc.higherY(playerLoc)) {
//				System.out.println("going up cart");
				aiPlayer.moveUpCartesian();
			}else if (calc.higherX(playerLoc)) {
//				System.out.println("going left cart");
				aiPlayer.moveLeftCartesian();
			}else if (!calc.higherX(playerLoc)) {
//				System.out.println("going right cart");
				aiPlayer.moveRightCartesian(map.getWidth());
			}
		//	playerLoc = player.getLocation();
			//distance = calcDistance(aiPlayer.getLocation(), playerLoc);
		//}
	}
	


	public void wander() {
		
		Player player = calc.findNearestPlayer();
		if(calc.inAttackDistance(player))
			aiPlayer.lightAttack();

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
		case 0:aiPlayer.moveDown(map.getWidth(), map.getHeight());break;
		case 1:aiPlayer.moveUp();break;
		case 2:aiPlayer.moveLeft(map.getWidth());break;
		case 3:aiPlayer.moveRight(map.getWidth(), map.getHeight());break;
		case 4:aiPlayer.moveDownCartestian(map.getHeight());break;
		case 5:aiPlayer.moveUpCartesian();break;
		case 6:aiPlayer.moveLeftCartesian();break;
		case 7:aiPlayer.moveRightCartesian(map.getWidth());break;
		}
	}
	
	public void changeToBefittingElement() {
		Player player = calc.findNearestPlayer();
		switch(aiCon.activeState) {
		
		//when attacking, change element to maximize damage given
		case AGGRESSIVE_ATTACK:
		case ATTACK:
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
		}
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
