package ai;

import java.util.ArrayList;
import java.util.Random;

import entities.PhysicsObject;
import entities.Player;
import entities.PowerUp;
import enums.ObjectType;
import enums.PowerUpType;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import java.util.concurrent.TimeUnit;

import ai.AiFSM;
import ai.AiStates;
import javafx.geometry.Point2D;
public class AiController {


		AiStates activeState;
		Player[] players;
		ArrayList<PhysicsObject> objects;
		Player aiPlayer ;
		int powerupIndex = -1;
		Rectangle map;
		AiController  aiCon= this;
		final int DELAY_TIME = 28;

		public AiController(Player aiPlayer,Player [] players, ArrayList<PhysicsObject> objects, Rectangle map) {

			aiPlayer.canUp=  aiPlayer.canDown= aiPlayer.canLeft= aiPlayer.canRight= aiPlayer.canUpCart= aiPlayer.canDownCart= aiPlayer.canLeftCart= aiPlayer.canRightCart= true;
	    	

	        activeState = AiStates.IDLE;
	     
	        this.players = players;
	        this.objects = objects;
	        this.map = map;
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

		public void startBasicAi() {
			System.out.println("started basic ai\n\n");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					boolean bool = true;
					while (bool) {
						AiFSM.basicAiFetchAction(aiPlayer, aiCon, players);
						//System.out.println("health "+aiPlayer.getHealth());
						basicAIExecuteAction();
						if (aiPlayer.getHealth() <= 0)
							bool = false;
					}
				}

			});

			t.start();
		}

		/*public void startAdvancedAI() {

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {

					boolean bool = true;

					while (bool) {

						EnemyFSM.advancedEnemyFetchAction(aiPlayer, players, getPowerups());

						advancedAIExecuteAction();

						if (aiPlayer.getHealth() <= 0)
							bool = false;
					}
				}

			});

			t.start();

		}*/
		
		//TODO: figure why the shield and heavy attack are using the player's instead of the ai's
		private void basicAIExecuteAction() {
			switch (activeState) {
			case ATTACK:
				attack();
				break;
			case AGGRESSIVE_ATTACK:
				aggressiveAttack();
				break;
			case FIND_HEALTH:
				//aiPlayer.shield();
				findHealth();
				break;
			case FIND_DAMAGE:
				//aiPlayer.shield();
				findDamage();
				break;
			case FIND_SPEED:
				//aiPlayer.shield();
				findSpeed();
				break;
			case ESCAPE:
				//aiPlayer.shield();
				escape();
				break;
			case IDLE:
				break;
			default:
				break;
			}
		}

		/*private void advancedAIExecuteAction() {
			switch (activeState) {
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
			}
		}*/

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
			if (inAttackDistance(player)) {
				aiPlayer.chargeHeavyAttack();
				aiPlayer.lightAttack();
			}
		}
		
		public void escape() {
			delay();
		
			Player player = findNearestPlayer();
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
			int index = 0;
			for (int i = 0; i < players.length; i++) {
				double tempDis = calcDistance(players[i].getLocation(), aiPlayer.getLocation());
				if (tempDis < minDis) {
					minDis = tempDis;
					index = i;
				}
			}

			double angle = calcAngle(aiPlayer.getLocation(), players[index].getLocation());

			aiPlayer.setPlayerAngle(new Rotate(angle));

			return players[index];
		}

		public void attack() {
			Player player = findNearestPlayer();
			moveTo(player);

			if (inAttackDistance(player)) {
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

			//System.out.println("enemy moving to powerup: "+getPowerups().get(powerupIndex).getType());
			double distance = calcDistance(aiPlayer.getLocation(), loc);
			//System.out.println("distance: " + distance);

			while ((int) distance > 2) {

				delay();

				if (powerupIndex == -1)
					break;

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

				ArrayList<PowerUp> powerups = getPowerups();
				//check if the power up has been taken by another player
				if ( powerups.size() < 1 || !powerups.get(powerupIndex).getLocation().equals(loc))
					break;

				//System.out.println("stuck in move to pu loop");
				//System.out.println("distance: " + distance + "\nlocation: " + getLocation() + "\npu loc: " + loc);
				distance = calcDistance(aiPlayer.getLocation(), loc);
			}
		}

		public void moveTo(Player player) {

			Point2D playerLoc = player.getLocation();
			double distance = calcDistance(aiPlayer.getLocation(), playerLoc);
			
			
			
			//System.out.println("distance: "+distance+"\n2*width: "+2*aiPlayer.getWidth());
			if( distance <= 2*aiPlayer.getWidth()) 
				return;

			//while ((int) distance - aiPlayer.getWidth() > aiPlayer.getWidth()) {
			
				delay();
				//System.out.println("moving to player");
				
				
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
				}/*else if (higherY(playerLoc)) {
					System.out.println("going up cart");
					moveUpCartesian();
				}else if (!higherY(playerLoc)) {
					System.out.println("going down cart");
					moveDownCartestian(map.getWidth());
				}else if (higherX(playerLoc)) {
					System.out.println("going left cart");
					moveLeftCartesian();
				}else if (!higherX(playerLoc)) {
					System.out.println("going right cart");
					moveRightCartesian(map.getWidth());
				}*/
			//	playerLoc = player.getLocation();
				//distance = calcDistance(aiPlayer.getLocation(), playerLoc);
			//}
		}
		
		private void delay() {
			try {
				TimeUnit.MILLISECONDS.sleep(DELAY_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
