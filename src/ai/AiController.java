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

import ai.EnemyFSM;
import ai.EnemyStates;
import javafx.geometry.Point2D;
public class AiController {


		EnemyStates activeState;
		Player[] players;
		ArrayList<PhysicsObject> objects;
		Player aiPlayer ;
		int powerupIndex = -1;
		Rectangle map;
		AiController  aiCon= this;

		public AiController(Player aiPlayer,Player [] players, ArrayList<PhysicsObject> objects, Rectangle map) {

			aiPlayer.canUp=  aiPlayer.canDown= aiPlayer.canLeft= aiPlayer.canRight= aiPlayer.canUpCart= aiPlayer.canDownCart= aiPlayer.canLeftCart= aiPlayer.canRightCart= true;
	    	

	        activeState = EnemyStates.IDLE;
	     
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

		private ArrayList<PowerUp> getPowerups() {

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

		public void startBasicAI() {
			System.out.println("IS THIS OWKRIGNKDFKJDSKFj");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					boolean bool = true;
					while (bool) {
						EnemyFSM.basicEnemyFetchAction(aiPlayer, aiCon, players, getPowerups());

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

		private void basicAIExecuteAction() {
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
			case ESCAPE:
				break;
			case IDLE:
				break;
			default:
				break;
			}
		}

		private void advancedAIExecuteAction() {
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

		public EnemyStates getActiveState() {
			return activeState;
		}

		public void setState(EnemyStates s) {
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
				// if(canHeavyAttack() )
				// heavyAttack();
				aiPlayer.lightAttack();
			}
		}

		public boolean inAttackDistance(Player player) {
			if ((int) calcDistance(aiPlayer.getLocation(), player.getLocation()) - aiPlayer.getWidth() < aiPlayer.getWidth())
				return true;
			return false;
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

			for(PowerUp pu:getPowerups())
				//System.out.println(pu.getType());

			
			while ((int) distance > 2) {

				try {
					TimeUnit.MILLISECONDS.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

				if (!objects.get(powerupIndex).getLocation().equals(loc))
					break;

				//System.out.println("stuck in move to pu loop");
				//System.out.println("distance: " + distance + "\nlocation: " + getLocation() + "\npu loc: " + loc);
				distance = calcDistance(aiPlayer.getLocation(), loc);
			}
		}

		public void moveTo(Player player) {

			Point2D playerLoc = player.getLocation();
			double distance = calcDistance(aiPlayer.getLocation(), playerLoc);
			

			while ((int) distance - aiPlayer.getWidth() > aiPlayer.getWidth()) {
				
				try {
					TimeUnit.MILLISECONDS.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
				playerLoc = player.getLocation();
				distance = calcDistance(aiPlayer.getLocation(), playerLoc);
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


}
