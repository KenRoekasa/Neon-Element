package entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ai.EnemyFSM;
import ai.EnemyStates;
import enumSwitches.objectSize;
import enums.ObjectType;
import enums.PowerUpType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Enemy extends Character {

	EnemyStates activeState;
	Character[] players;
	ArrayList<PhysicsObject> objects;
	Enemy enemy = this;
	int powerupIndex = -1;
	Rectangle map;

	public Enemy(Character [] players, ArrayList<PhysicsObject> objects, Rectangle map) {

		canUp=  canDown= canLeft= canRight= canUpCart= canDownCart= canLeftCart= canRightCart= true;
    	

        activeState = EnemyStates.IDLE;
        //default random
        assignRandomElement();
        this.players = players;
        this.objects = objects;
        this.map = map;
        location = new Point2D(10, 100);
        playerAngle = new Rotate(0);
        health = 100;
        movementSpeed = 2;
        isShielded = false;
        width = objectSize.getObjectSize(ObjectType.ENEMY);
        
        for (int i = 0; i < timerArray.length; i++) {
            timerArray[i] = System.currentTimeMillis() - 10 * 1000;
        }
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
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				boolean bool = true;
				while (bool) {
					EnemyFSM.basicEnemyFetchAction(enemy, players, getPowerups());

					basicAIExecuteAction();
					if (health <= 0)
						bool = false;
				}
			}

		});

		t.start();
	}

	public void startAdvancedAI() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				boolean bool = true;

				while (bool) {

					EnemyFSM.advancedEnemyFetchAction(enemy, players, getPowerups());

					advancedAIExecuteAction();

					if (health <= 0)
						bool = false;
				}
			}

		});

		t.start();

	}

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
			changeToEarth();
			break;
		case 1:
			changeToFire();
			break;
		case 2:
			changeToWater();
			break;
		case 3:
			changeToAir();
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
		Character player = findNearestPlayer();
		moveTo(player);
		if (inAttackDistance(player)) {
			// if(canHeavyAttack() )
			// heavyAttack();
			lightAttack();
		}
	}

	public boolean inAttackDistance(Character player) {
		if ((int) calcDistance(getLocation(), player.getLocation()) - getWidth() < getWidth())
			return true;
		return false;
	}

	public int findNearestPowerUp(PowerUpType pu) {
		ArrayList<PowerUp> powerups = getPowerups();
		int index = -1;
		double distance = Double.MAX_VALUE;
		for (int i = 0; i < powerups.size(); i++) {
			if (powerups.get(i).getType().equals(pu)) {
				double disToPU = calcDistance(powerups.get(i).getLocation(), getLocation());
				if (disToPU < distance) {
					distance = disToPU;
					index = i;
				}
			}
		}

		return index;
	}

	public Character findNearestPlayer() {
		double minDis = Double.MAX_VALUE;
		int index = 0;
		for (int i = 0; i < players.length; i++) {
			double tempDis = calcDistance(players[i].getLocation(), getLocation());
			if (tempDis < minDis) {
				minDis = tempDis;
				index = i;
			}
		}

		double angle = calcAngle(getLocation(), players[index].getLocation());

		setPlayerAngle(new Rotate(angle));

		return players[index];
	}

	public void attack() {
		Character player = findNearestPlayer();
		moveTo(player);

		if (inAttackDistance(player)) {
			lightAttack();
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
		double distance = calcDistance(getLocation(), loc);
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
				moveUp();
			else if (isUnder(loc))
				moveDown(map.getWidth(), map.getHeight());
			if (isLeftOf(loc))
				moveLeft(map.getWidth());
			else if (isRightOf(loc))
				moveRight(map.getWidth(), map.getHeight());
			else if (higherY(loc))
				moveDownCartestian(map.getWidth());
			else if (!higherY(loc))
				moveUpCartesian();
			else if (higherX(loc))
				moveLeftCartesian();
			else if (!higherX(loc))
				moveRightCartesian(map.getWidth());

			if (!objects.get(powerupIndex).getLocation().equals(loc))
				break;

			//System.out.println("stuck in move to pu loop");
			//System.out.println("distance: " + distance + "\nlocation: " + getLocation() + "\npu loc: " + loc);
			distance = calcDistance(getLocation(), loc);
		}
	}

	public void moveTo(Character player) {

		Point2D playerLoc = player.getLocation();
		double distance = calcDistance(getLocation(), playerLoc);
		

		while ((int) distance - getWidth() > getWidth()) {
			
			try {
				TimeUnit.MILLISECONDS.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			setPlayerAngle(new Rotate(calcAngle(getLocation(),player.getLocation())));

			if (isAbove(playerLoc)) {
				moveUp();
			}
			else if (isUnder(playerLoc)) {
			//	System.out.println("going down");
				moveDown(map.getWidth(), map.getHeight());
			}if (isLeftOf(playerLoc)) {
				//System.out.println("going left");
				moveLeft(map.getWidth());
			}else if (isRightOf(playerLoc)) {
				//System.out.println("going right");
				moveRight(map.getWidth(), map.getHeight());
			}else if (isAbove(playerLoc)) {	
				//System.out.println("going up");
				moveUp();
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
			distance = calcDistance(getLocation(), playerLoc);
		}
	}

	private boolean higherY(Point2D loc) {

		return (getLocation().getY() - loc.getY() < 0) ? true : false;
	}

	private boolean higherX(Point2D loc) {

		return (getLocation().getX() - loc.getX() < 0) ? true : false;
	}

	private boolean isRightOf(Point2D loc) {
		if (loc.getX() > getLocation().getX() && loc.getY() < getLocation().getY())
			return true;
		return false;
	}

	private boolean isLeftOf(Point2D loc) {
		if (loc.getX() < getLocation().getX() && loc.getY() > getLocation().getY())
			return true;
		return false;
	}

	private boolean isUnder(Point2D loc) {
		if (loc.getX() > getLocation().getX() && loc.getY() > getLocation().getY())
			return true;
		return false;
	}

	private boolean isAbove(Point2D loc) {
		if (loc.getX() < getLocation().getX() && loc.getY() < getLocation().getY())
			return true;
		return false;
	}

	@Override
	public void update() {

	}
}
