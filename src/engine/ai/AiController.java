package engine.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.AiType;
import engine.enums.ObjectType;
import engine.enums.PowerUpType;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import javafx.geometry.Point2D;
public class AiController {


		AiStates activeState;
		ArrayList<PhysicsObject> objects;
		Player aiPlayer ;
		int powerupIndex = -1;
		Rectangle map;
		Player player;
		AiController  aiCon= this;
		AiType aiType;
		final int DELAY_TIME = 28;
		boolean wandering = false;
		boolean spamming = false;
		int wanderingDirection ;
		public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player) {

			aiPlayer.canUp=  aiPlayer.canDown= aiPlayer.canLeft= aiPlayer.canRight= aiPlayer.canUpCart= aiPlayer.canDownCart= aiPlayer.canLeftCart= aiPlayer.canRightCart= true;
	    	

	        activeState = AiStates.IDLE;
	        this.objects = objects;
	        this.map = map;
	        this.player = player;
	        this.aiPlayer =aiPlayer;
	        //default random
	        assignRandomElement();
	        
	    }
	

		public void startEasyAi() {
			aiType = AiType.EASY;
			System.out.println("started easy ai\n\n");
			
			Thread t = new Thread(new Runnable() {
				
			
				@Override
				public void run() {
					
					double startTime = System.nanoTime()/1000000000;
					double endTime ;
					
					boolean bool = true;
					while (bool) {
						
						//assigns random element every fifteen seconds
						endTime = System.nanoTime()/1000000000;
						double elapsedTime = endTime - startTime;
						if(elapsedTime >= 15) {
							startTime = System.nanoTime()/1000000000;
							assignRandomElement();
						}
						
						//System.out.println(aiPlayer.getLocation());
						AiFSM.easyAiFetchAction(aiPlayer, aiCon);
						
						if(getActiveState().equals(AiStates.ESCAPE))
							aiPlayer.delay((DELAY_TIME/3)*2);
						else
							aiPlayer.delay(DELAY_TIME);
						
						easyAIExecuteAction();

						if (aiPlayer.getHealth() <= 0) {
							aiPlayer.respawn(map.getWidth(),map.getHeight());
						}
						
					}
				
				}

			});

			t.start();
		}

		public void startMediumAi() {
			aiType = AiType.MEDIUM;
			System.out.println("started medium ai\n\n");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					
					boolean bool = true;
					while (bool) {
						
						AiFSM.mediumAiFetchAction(aiPlayer, aiCon);
						
						if(getActiveState().equals(AiStates.ESCAPE))
							aiPlayer.delay((DELAY_TIME/2)+(DELAY_TIME/4));
						else
							aiPlayer.delay(DELAY_TIME);
						
						mediumAIExecuteAction();
						
						if (aiPlayer.getHealth() <= 0) {
							aiPlayer.respawn(map.getWidth(),map.getHeight());
						}
						
					}
					
				}

			});

			t.start();
		}
		
		public void startHardAi() {
			aiType = AiType.HARD;
			System.out.println("started hard ai\n\n");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					boolean bool = true;
					while (bool) {
						AiFSM.hardAiFetchAction(aiPlayer, aiCon);
						//System.out.println("health "+aiPlayer.getHealth());
						
						if(getActiveState().equals(AiStates.ESCAPE))
							aiPlayer.delay((DELAY_TIME/2)+(DELAY_TIME/4));
						else
							aiPlayer.delay(DELAY_TIME);
						
						easyAIExecuteAction();
						//delay to limit speed 
						
						if (aiPlayer.getHealth() <= 0) {
							aiPlayer.respawn(map.getWidth(),map.getHeight());
						}
						
//						if(timer.off)
//							bool = false;				
					}
				
				}

			});

			t.start();
		}
		
		private void easyAIExecuteAction() {
			if(!activeState.equals(AiStates.WANDER))
				wandering = false;
			switch (activeState) {
			case ATTACK:
				aiPlayer.unShield();
				attack();
				break;
			case AGGRESSIVE_ATTACK:
				aiPlayer.unShield();
				aggressiveAttack();
				break;
			case FIND_HEALTH:
				findHealth();
				break;
			case FIND_DAMAGE:
				aiPlayer.shield();
				findDamage();
				break;
			case FIND_SPEED:
				aiPlayer.shield();
				findSpeed();
				break;
			case ESCAPE:
				aiPlayer.shield();
				escape();
				break;
			case WANDER:
				aiPlayer.unShield();
				startWandering();
			case IDLE:
				break;
			default:
				break;
			}
		}
		
		private void mediumAIExecuteAction() {
			if(!activeState.equals(AiStates.WANDER))
				wandering = false;
			switch (activeState) {
			case ATTACK:
				aiPlayer.unShield();
				attack();
				break;
			case AGGRESSIVE_ATTACK:
				aiPlayer.unShield();
				aggressiveAttack();
				break;
			case FIND_HEALTH:
				aiPlayer.shield();
				findHealth();
				break;
			case FIND_DAMAGE:
				aiPlayer.shield();
				findDamage();
				break;
			case FIND_SPEED:
				aiPlayer.shield();
				findSpeed();
				break;
			case ESCAPE:
				aiPlayer.shield();
				escape();
				break;
			case WANDER:
				aiPlayer.unShield();
				startWandering();
			case IDLE:
				break;
			default:
				break;
			}
		}

		private void startWandering() {
			if(!wandering) {
				wandering = true;
				Random r = new Random();
				wanderingDirection = r.nextInt(8);
			}
			wander();
		}

		private void wander() {
			
			Player player = findNearestPlayer();
			if(inAttackDistance(player))
				aiPlayer.lightAttack();

			if(reachedAnEdge()) {
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
		
		public boolean playerIsTooClose() {
			Point2D playerLoc = findNearestPlayer().getLocation();
			return isTooClose(playerLoc);
		}
		
		public boolean powerupIsTooClose() {
			int index = findNearestPowerUp();
			if(index != -1 && isTooClose(getPowerups().get(index).getLocation())) {
				return true;
			}
			return false;
		}
		
		private boolean isTooClose(Point2D playerLoc ) {
			Point2D aiLoc = aiPlayer.getLocation();
			return (aiLoc.distance(playerLoc)<(map.getWidth()*0.2));
		}


		private boolean reachedAnEdge() {
			double x = aiPlayer.getLocation().getX();
			double y = aiPlayer.getLocation().getY();
			if(x<map.getHeight()*0.02 || x>map.getHeight()*0.98 || y<map.getWidth()*0.02 || y>map.getWidth()*0.98)
				return true;
			return false;
		}
		
		private void assignRandomElement() {

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

		public AiStates getActiveState() {
			return activeState;
		}

		public void setState(AiStates s) {
			activeState = s;
		}

		public void setPowerUpIndex(int i) {
			powerupIndex = i;
		}

		public void findSpeed() {
			int index = findNearestPowerUp(PowerUpType.SPEED);
			if (index != -1)
				moveTo(index, getPowerups().get(index).getLocation());
		}

		public void findDamage() {
			int index = findNearestPowerUp(PowerUpType.DAMAGE);
			if (index != -1)
				moveTo(index, getPowerups().get(index).getLocation());
		}

		public void findHealth() {
			int index = findNearestPowerUp(PowerUpType.HEAL);
			if (index != -1)
				moveTo(index, getPowerups().get(index).getLocation());
		}

		public void aggressiveAttack() {
			Player player = findNearestPlayer();
			moveTo(player);
			if (inAttackDistance(player) && player.getHealth()>0) {
				aiPlayer.lightAttack();
				aiPlayer.chargeHeavyAttack();
			}
		}
		
//		public void spam(int time) {
//			
//			spammingTimer(time);
//			while (spamming) {
//				aiPlayer.delay(DELAY_TIME);
//				switch (activeState) {
//				case ATTACK:
//					aiPlayer.unShield();
//					attack();
//					break;
//				case AGGRESSIVE_ATTACK:
//					aiPlayer.unShield();
//					aggressiveAttack();
//					break;
//				case FIND_HEALTH:
//					aiPlayer.shield();
//					findHealth();
//					break;
//				case FIND_DAMAGE:
//					aiPlayer.shield();
//					findDamage();
//					break;
//				case FIND_SPEED:
//					aiPlayer.shield();
//					findSpeed();
//					break;
//				case ESCAPE:
//					aiPlayer.shield();
//					escape();
//					break;
//				case WANDER:
//					aiPlayer.unShield();
//					startWandering();
//				case IDLE:
//					break;
//				default:
//					break;
//				}
//			}
//			
//		}
//		
//		private void spammingTimer(int time) {
//			spamming = true;
//			Thread t = new Thread(new Runnable() {
//				@Override
//				public void run() {
//				aiPlayer.delay(time);
//				spamming = false;
//				}
//
//			});
//			t.start();
//		}
		
		public void escape() {
			Player player = findNearestPlayer();
//			if (player.getTag().equals(ObjectType.ENEMY)) {
//				moveAway(player);
//				return;
//			}
			moveAway(player);
//			
//			if (inAttackDistance(player) && player.getHealth()>0) {
//				aiPlayer.unShield();
//				aiPlayer.lightAttack();
//				aiPlayer.shield();
//			}
//				
//			switch(player.getCharacterDirection()) {
//			case DOWN:
//				aiPlayer.moveUp();
//				break;
//			case DOWNCART:
//				aiPlayer.moveUpCartesian();
//				break;
//			case LEFT:
//				aiPlayer.moveRight(map.getWidth(), map.getHeight());
//				break;
//			case LEFTCART:
//				aiPlayer.moveRightCartesian(map.getWidth());
//				break;
//			case RIGHT:
//				aiPlayer.moveLeft(map.getWidth());
//				break;
//			case RIGHTCART:
//				aiPlayer.moveRightCartesian(map.getWidth());
//				break;
//			case UP:
//				aiPlayer.moveDown(map.getWidth(), map.getHeight());
//				break;
//			case UPCART:
//				aiPlayer.moveDown(map.getWidth(), map.getHeight());
//				break;
//			default:
//				break;
//			
//			}
		}
		
		public void moveAway(Player player) {
			if(reachedAnEdge()) {
				moveAwayFromEdge();
			}
			
			if (inAttackDistance(player) && player.getHealth()>0) {
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
		
		
		private void moveAwayFromEdge() {
			double width = map.getWidth();
			double height = map.getHeight();
			int i = closestEdgeLocation();
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
		/*
		 * 0 = down cart
		 * 1 = right
		 * 2 = down
		 * 3 = right cart
		 * 4 = up
		 * 5 = left cart
		 * 6 = left
		 * 7 = up cart
		 */
		private int closestEdgeLocation() {
			Point2D loc = aiPlayer.getLocation();
			int dir = -1;
			double x = loc.getX();
			double y = loc.getY();
			double width = map.getWidth();
			double height = map.getHeight();
			//x < 1000
			if(x<width/2) {
				//x<1000 and y < 1000 - first block, up-most
				if(y<height/2) {
					//x < 100 
					if(x<(width*0.05)) {
						// x and y < 100,(at start point) - move down cart 
						if(y<(height*0.05)) {
							dir = 0;
						}
						// x < 100 but 100> y >1000 left-up wall - move right
						else {
							dir = 1;
						}
					}
					//x > 100
					else {
						// 1000> x >100 and y < 100, right-up wall - move down
						if(y<(height*0.05)) {
							dir = 2;
						}
						// 1000 > x&y > 100  - close to middle point, not near a wall
						else {
						}
					}
				}
				//x<1000 and y > 1000 - second block, left-most
				else {
					//x < 100
					if(x<(width*0.05)) {
						// x < 100 and y > 1900,(left start point) - right cart
						if(y>(height*0.95)) {
							dir = 3;
						}
						// x < 100 and 1000 < y < 1900, left-up wall - move right
						else {
							dir = 1;
						}
					}
					//x 100 < x < 1000
					else {
						//x 100 < x < 1000 and y > 1900 left-down wall, move up
						if(y>(height*0.95))
							dir = 4;
						//close to middle point, not near a wall
						else {
						}
							
					}
					
				}
			}
			//x > 1000
			else {
				//y < 1000 - third block, right-most
				if(y<height/2) {
					// x > 1000 & y < 100
					if(y<height*0.05) {
						// x < 1900 and y < 100,(right start point) - left cart
						if(x>width*0.95) {
							dir = 5;
						}
						//y < 100 and x < 1900, right-up wall -  down
						else {
							dir = 2;
						}
					}
					//x > 1000 and 100 > y > 1000
					else {
						// x > 1900 and 100 < y < 1000, right-down wall, left
						if(x>width*0.95)
							dir = 6;
						//else goes to the centre, not near a wall
						else {
						}
					}
					
				}
				//x and y > 1000	fourth block, down-most
				else {
					//x > 1900 and y > 1000
					if(x>width*0.95) {
						//x and y > 1900, (down start point) - up cart
						if(y>height*0.95) {
							dir = 7;
						}
						//x > 1900 and 1000 < y < 1900, right-down wall, left
						else {
							dir = 6;
						}
					}
					//1000 < x < 1900
					else {
						//1000 < x < 1900 and y > 1900, left-down wall, up
						if(y>height*0.95) {
							dir = 4;
						}
						//going to the centre, not close to wall
						else {
						}
					}
				}
			}
			
			return dir;
		}


		public Player getAiPlayer() {
			return aiPlayer;
		}

		public ArrayList<PowerUp> getPowerups() {

			ArrayList<PowerUp> powerups = new ArrayList<PowerUp>();
			for (int i = 0; i < objects.size(); i++) {
				if (!objects.get(i).equals(null)) {
					if (ObjectType.POWERUP.equals(objects.get(i).getTag()))
						if (!powerups.contains(objects.get(i)))
							powerups.add((PowerUp) objects.get(i));
				}
			}
			return powerups;
		}
		
		public boolean powerupCloserThanPlayer() {
			int puIndex = findNearestPowerUp();
			if(puIndex == -1)
				return false;
			double disToPu = getPowerups().get(puIndex).getLocation().distance(aiPlayer.getLocation());
			double disToPlayer = findNearestPlayer().getLocation().distance(aiPlayer.getLocation());
			
			return disToPu<disToPlayer;
		}


		public boolean inAttackDistance(Player player) {
			if ((int) calcDistance(aiPlayer.getLocation(), player.getLocation()) - aiPlayer.getWidth() < aiPlayer.getWidth())
				return true;
			return false;
		}
		
		public int findNearestPowerUp() {
			ArrayList<PowerUp> powerups = getPowerups();
			int index = -1;
			double distance = Double.MAX_VALUE;
			for (int i = 0; i < powerups.size(); i++) {
				double disToPU = calcDistance(powerups.get(i).getLocation(), aiPlayer.getLocation());
				if (disToPU < distance) {
					distance = disToPU;
					index = i;
				}
			}

			return index;
		}

		public int findNearestPowerUp(PowerUpType pu) {
			ArrayList<PowerUp> powerups = getPowerups();
			int index = -1;
			double distance = Double.MAX_VALUE;
			for (int i = 0; i < powerups.size(); i++) {
				if (powerups.get(i).getType().equals(pu)) {
					double disToPU = calcDistance(powerups.get(i).getLocation(), aiPlayer.getLocation());
					if (disToPU < distance) {
						distance = disToPU;
						index = i;
					}
				}
			}

			return index;
		}

		public Player findNearestPlayer() {
			double minDis = Double.MAX_VALUE;
			ArrayList<Player> players = getPlayers();
			int index = 0;
			for (int i = 0; i < players.size(); i++) {
				double tempDis = calcDistance(players.get(i).getLocation(), aiPlayer.getLocation());
				if (tempDis < minDis) {
					minDis = tempDis;
					index = i;
				}
			}

			double angle = calcAngle(aiPlayer.getLocation(), players.get(index).getLocation());

			aiPlayer.setPlayerAngle(new Rotate(angle));

			return players.get(index);
		}

		public void attack() {
			Player player = findNearestPlayer();
			moveTo(player);

			if (inAttackDistance(player) && player.getHealth()>0) {
				aiPlayer.lightAttack();
			}
		}

		private double calcAngle(Point2D loc1, Point2D loc2) {

			double x = loc1.getX() - loc2.getX();
			double y = loc1.getY() - loc2.getY();
			double angle = Math.toDegrees(Math.atan2(y, x)) - 90.0;
			return angle;
		}

		private double calcDistance(Point2D a, Point2D b) {
			return a.distance(b);
		}

		public void moveTo(int powerupIndex, Point2D loc) {

			double distance = calcDistance(aiPlayer.getLocation(), loc);

			if(!((int) distance > 2 || powerupIndex == -1))
				return;

				if (isAbove(loc))
					aiPlayer.moveUp();
				else if (isUnder(loc))
					aiPlayer.moveDown(map.getWidth(), map.getHeight());
				if (isLeftOf(loc))
					aiPlayer.moveLeft(map.getWidth());
				else if (isRightOf(loc))
					aiPlayer.moveRight(map.getWidth(), map.getHeight());
				else if (higherY(loc))
					aiPlayer.moveDownCartestian(map.getWidth());
				else if (!higherY(loc))
					aiPlayer.moveUpCartesian();
				else if (higherX(loc))
					aiPlayer.moveLeftCartesian();
				else if (!higherX(loc))
					aiPlayer.moveRightCartesian(map.getWidth());

		}

		public void moveTo(Player player) {

			Point2D playerLoc = player.getLocation();
			double distance = calcDistance(aiPlayer.getLocation(), playerLoc);
			
			if( player.getTag().equals(ObjectType.PLAYER) && distance <= 2*aiPlayer.getWidth()) 
				return;
			if( player.getTag().equals(ObjectType.ENEMY) && distance <= 2) 
				return;
				
				aiPlayer.setPlayerAngle(new Rotate(calcAngle(aiPlayer.getLocation(),player.getLocation())));

				if (isAbove(playerLoc)) {
					aiPlayer.moveUp();
				}
				else if (isUnder(playerLoc)) {
				//	System.out.println("going down");
					aiPlayer.moveDown(map.getWidth(), map.getHeight());
				}if (isLeftOf(playerLoc)) {
					//System.out.println("going left");
					aiPlayer.moveLeft(map.getWidth());
				}else if (isRightOf(playerLoc)) {
					//System.out.println("going right");
					aiPlayer.moveRight(map.getWidth(), map.getHeight());
				}else if (isAbove(playerLoc)) {	
					//System.out.println("going up");
					aiPlayer.moveUp();
				}else if (higherY(playerLoc)) {
//					System.out.println("going up cart");
					aiPlayer.moveUpCartesian();
				}else if (!higherY(playerLoc)) {
//					System.out.println("going down cart");
					aiPlayer.moveDownCartestian(map.getWidth());
				}else if (higherX(playerLoc)) {
//					System.out.println("going left cart");
					aiPlayer.moveLeftCartesian();
				}else if (!higherX(playerLoc)) {
//					System.out.println("going right cart");
					aiPlayer.moveRightCartesian(map.getWidth());
				}
			//	playerLoc = player.getLocation();
				//distance = calcDistance(aiPlayer.getLocation(), playerLoc);
			//}
		}
		

		private boolean higherY(Point2D loc) {

			return (aiPlayer.getLocation().getY() - loc.getY() < 0) ? true : false;
		}

		private boolean higherX(Point2D loc) {

			return (aiPlayer.getLocation().getX() - loc.getX() < 0) ? true : false;
		}

		private boolean isRightOf(Point2D loc) {
			if (loc.getX() > aiPlayer.getLocation().getX() && loc.getY() < aiPlayer.getLocation().getY())
				return true;
			return false;
		}

		private boolean isLeftOf(Point2D loc) {
			if (loc.getX() < aiPlayer.getLocation().getX() && loc.getY() > aiPlayer.getLocation().getY())
				return true;
			return false;
		}

		private boolean isUnder(Point2D loc) {
			if (loc.getX() > aiPlayer.getLocation().getX() && loc.getY() > aiPlayer.getLocation().getY())
				return true;
			return false;
		}

		private boolean isAbove(Point2D loc) {
			if (loc.getX() < aiPlayer.getLocation().getX() && loc.getY() < aiPlayer.getLocation().getY())
				return true;
			return false;
		}
		
		public ArrayList<Player> getPlayers(){
			ArrayList<Player> players = new ArrayList<>();
			synchronized (objects) {
				for (Iterator<PhysicsObject> itr1 = objects.iterator(); itr1.hasNext(); ) {
					PhysicsObject object = itr1.next();
					if (object.getTag().equals(ObjectType.ENEMY) && !object.equals(aiPlayer)) {
						players.add((Player) object);
					}
				}
			}
			players.add(player);
			return players;
		}

		public void changeToBefittingElement() {
			Player player = findNearestPlayer();
			switch(activeState) {
			
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

}
