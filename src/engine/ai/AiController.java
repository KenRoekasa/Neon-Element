package engine.ai;

import java.util.ArrayList;
import java.util.Random;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
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
		final int DELAY_TIME = 28;
		boolean wandering = false;
		boolean spamming = false;
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

		public void startEasyAi() {
			System.out.println("started easy ai\n\n");
			changeToRandomElement();

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					boolean bool = true;
					while (bool) {
						
						AiFSM.easyAiFetchAction(aiPlayer, aiCon);
						
						if(getActiveState().equals(AiStates.ESCAPE))
							aiPlayer.delay((DELAY_TIME/2)+(DELAY_TIME/4));
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
			case ESCAPE:
				aiPlayer.shield();
				escape();
				break;
			case WANDER:
				aiPlayer.unShield();
				wander();
			case IDLE:
				break;
			default:
				break;
			}
		}
		
		private void mediumAIExecuteAction() {
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
				wander();
			case IDLE:
				break;
			default:
				break;
			}
		}

		private void wander() {
			Random r = new Random();
			Player player = findNearestPlayer();
			
			int i ;
			i = r.nextInt(2);
			if(i==0) {
				while(!inAttackDistance(player) && aiPlayer.getHealth()>0 ) {
					aiPlayer.delay(DELAY_TIME);
					moveTo(player);
				}
				aiPlayer.lightAttack();
			}
			else {
				
				int movementDirection;
				movementDirection = r.nextInt(8);
	
				wanderingTimer(5000);
				while (wandering && aiPlayer.getHealth()>0) {
					
					if(reachedAnEdge()) {
						switch(movementDirection) {
						case 0:aiPlayer.moveUp();break;
						case 1:aiPlayer.moveDown(map.getWidth(), map.getHeight());break;
						case 2:aiPlayer.moveRight(map.getWidth(), map.getHeight());break;
						case 3:aiPlayer.moveLeft(map.getWidth());break;
						case 4:aiPlayer.moveUpCartesian();break;
						case 5:aiPlayer.moveDownCartestian(map.getHeight());break;
						case 6:aiPlayer.moveRightCartesian(map.getWidth());break;
						case 7:aiPlayer.moveLeftCartesian();break;
						}
					}
					
					aiPlayer.delay(DELAY_TIME);
					
					player = findNearestPlayer();
					
					if(inAttackDistance(player))
						aiPlayer.lightAttack();
	
					switch(movementDirection) {
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
			}
		}
		
		private boolean reachedAnEdge() {
			double x = aiPlayer.getLocation().getX();
			double y = aiPlayer.getLocation().getY();
			if(x<map.getHeight()*0.02 || x>map.getHeight()*0.95 || y<map.getWidth()*0.05 || y>map.getWidth()*0.98)
				return true;
			return false;
		}

		private void wanderingTimer(int time) {
			wandering = true;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
				aiPlayer.delay(time);
				wandering = false;
				}

			});
			t.start();
		}
		
		private void changeToRandomElement() {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					boolean bool = true;
					while (bool) {
						aiPlayer.delay(15000);
						assignRandomElement();
						//TODO: make it terminate when ai player dies	
					}
				
				}

			});

			t.start();
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
		
		public void spam(int time) {
			
			spammingTimer(time);
			while (spamming) {
				aiPlayer.delay(DELAY_TIME);
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
					wander();
				case IDLE:
					break;
				default:
					break;
				}
			}
			
		}
		
		private void spammingTimer(int time) {
			spamming = true;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
				aiPlayer.delay(time);
				spamming = false;
				}

			});
			t.start();
		}
		
		public void escape() {
			Player player = findNearestPlayer();
			if (player.getTag().equals(ObjectType.ENEMY)) {
				moveAway(player);
				return;
			}
			
			if (inAttackDistance(player) && player.getHealth()>0) {
				aiPlayer.unShield();
				aiPlayer.lightAttack();
				aiPlayer.shield();
			}
				
			switch(player.getCharacterDirection()) {
			case DOWN:
				aiPlayer.moveUp();
				break;
			case DOWNCART:
				aiPlayer.moveUpCartesian();
				break;
			case LEFT:
				aiPlayer.moveRight(map.getWidth(), map.getHeight());
				break;
			case LEFTCART:
				aiPlayer.moveRightCartesian(map.getWidth());
				break;
			case RIGHT:
				aiPlayer.moveLeft(map.getWidth());
				break;
			case RIGHTCART:
				aiPlayer.moveRightCartesian(map.getWidth());
				break;
			case UP:
				aiPlayer.moveDown(map.getWidth(), map.getHeight());
				break;
			case UPCART:
				aiPlayer.moveDown(map.getWidth(), map.getHeight());
				break;
			default:
				break;
			
			}
		}
		
		public void moveAway(Player player) {
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
			ArrayList<Player> players = new ArrayList<Player>();
			for(PhysicsObject object : objects) {
				if( object.getTag().equals(ObjectType.ENEMY) && !object.equals(aiPlayer)  ) {
					players.add((Player)object);
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
