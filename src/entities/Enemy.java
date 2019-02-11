package entities;

import java.util.Random;

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
    Character [] players;
    PowerUp [] powerups;
    Enemy enemy  = this;
    int powerupIndex = -1;
    Rectangle map;
	
    public Enemy(Character [] players, PowerUp [] powerUps, Rectangle map) {

        activeState = EnemyStates.IDLE;
        //default random
        assignRandomElement();
        this.players = players;
        this.powerups = powerUps;
        this.map = map;
        location = new Point2D(10, 100);
        playerAngle = new Rotate(0);
        health = 100;
        movementSpeed = 2;
        isShielded = false;
        width = objectSize.getObjectSize(ObjectType.ENEMY);
    }
    
    public void startBasicAI() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean bool = true;
                while (bool) {


                	EnemyFSM.basicEnemyFetchAction(enemy, players, powerups);

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

                	EnemyFSM.advancedEnemyFetchAction(enemy, players, powerups);

                	advancedAIExecuteAction();
                	
                    if (health <= 0)
                        bool = false;
                }
            }

        });
        
        t.start();
		
	}
    
    private void basicAIExecuteAction() {
    	switch(activeState) {
    	case ATTACK :attack(); break;
    	case AGGRESSIVE_ATTACK: aggressiveAttack(); break;
    	case FIND_HEALTH: findHealth(); break;
    	case FIND_DAMAGE: findDamage(); break;
    	case FIND_SPEED: findSpeed(); break;
		case ESCAPE:break;
		case IDLE:break;
		default:break;
    	}
    }
    
	private void advancedAIExecuteAction() {
		switch(activeState) {
    	case ATTACK :attack(); break;
    	case AGGRESSIVE_ATTACK: aggressiveAttack(); break;
    	case FIND_HEALTH: findHealth(); break;
    	case FIND_DAMAGE: findDamage(); break;
    	case FIND_SPEED: findSpeed(); break;
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
    	PowerUp powerup = findNearestPowerUp( PowerUpType.SPEED);
		moveTo(powerup);
	}

	public void findDamage() {
		PowerUp powerup = findNearestPowerUp( PowerUpType.DAMAGE);
		moveTo(powerup);
	}

	public void findHealth() {
		PowerUp powerup = findNearestPowerUp( PowerUpType.HEAL);
		moveTo(powerup);
	}

	public void aggressiveAttack() {
		Character player = findNearestPlayer();
		moveTo(player);
    	if(inAttackDistance(player)) {
    		//if(canHeavyAttack() )
    		//heavyAttack();
    		lightAttack();
    	}
	}
	

    public boolean inAttackDistance(Character player) {
    	if ( (int) calcDistance(getLocation(), player.getLocation()) -getWidth() <getWidth())
    		return true;
    	return false;
    }

    public PowerUp findNearestPowerUp(PowerUpType pu) {
    	Character player = findNearestPlayer();
		int index=-1;
		double distance = Double.MAX_VALUE;
		for (int i = 0; i < powerups.length; i++) {
			if(powerups[i].getType().equals(pu)) {
				double disToPU = calcDistance(powerups[i].getLocation(),getLocation());
				if( disToPU < calcDistance(getLocation(),player.getLocation()) ) {
					if(disToPU < distance) {
						distance = disToPU;
						index = i;
					}
				}
			}
		}

		return (index == -1)?null:powerups[index];
	}

    public Character findNearestPlayer() {
		double minDis = Double.MAX_VALUE;
		int index = 0;
		for (int i = 0; i < players.length; i++) {
			double tempDis = calcDistance(players[i].getLocation(),getLocation());
			if( tempDis<minDis ) {
				minDis = tempDis;
				index = i;
			}
		}
		

		double angle = calcAngle(getLocation(),players[index].getLocation());
		
		setPlayerAngle(  new Rotate(angle) );
		
		return players[index];
	}



    public void attack() {
    	System.out.println("attack");
    	Character player = findNearestPlayer();
    	moveTo(player);
    	
    	if(inAttackDistance(player)) {
    		System.out.println("in attack distance");
    		//lightAttack();
    	}
    }
    
    private double calcAngle(Point2D loc1, Point2D loc2) {
    	
    	double x = Math.abs(loc1.getX()-loc2.getX());
    	double y = Math.abs(loc1.getY()-loc2.getY());
    	
       	x = Math.toDegrees(x);
       	y = Math.toDegrees(y);
    	
       	double angle = Math.toDegrees(Math.atan2(y, x)); 
    	
       	//System.out.println("x: "+x+"\ny: "+y+"\nangle: "+angle);
       	return angle;
    }

    private double calcDistance(Point2D a, Point2D b) {
    	return a.distance(b);
    }

    
    public void moveTo(PowerUp powerup) {
    	System.out.println("enemy moving to powerup");
    	Point2D powerupLoc = powerup.getLocation();
    	double distance = calcDistance(getLocation(),powerupLoc );

    	while( (int) distance - getWidth() > getWidth() ) {

    		if(isAbove(powerupLoc))
    			moveUp();
    		else if(isUnder(powerupLoc))
    			moveDown(map.getWidth(),map.getHeight());
    		if(isLeftOf(powerupLoc))
    			moveLeft(map.getWidth());
    		else if (isRightOf(powerupLoc))
    			moveRight(map.getWidth(),map.getHeight());
    		
    		distance = calcDistance(getLocation(), powerupLoc);
    	}
    }
    
    public void moveTo(Character player) {
    	Point2D playerLoc = player.getLocation();
    	double distance = calcDistance(getLocation(),playerLoc );

    	while( (int) distance - getWidth() > getWidth() ) {

    		if(isAbove(playerLoc))
    			moveUp();
    		else if(isUnder(playerLoc))
    			moveDown(map.getWidth(),map.getHeight());
    		if(isLeftOf(playerLoc))
    			moveLeft(map.getWidth());
    		else if (isRightOf(playerLoc))
    			moveRight(map.getWidth(),map.getHeight());
    		
    		distance = calcDistance(getLocation(), playerLoc);
    	}
    }
    private boolean isRightOf(Point2D loc) {
    	if(loc.getX()>getLocation().getX() && loc.getY()<getLocation().getY())
    		return true;
    	return false;
    }

    private boolean isLeftOf(Point2D loc) {
    	if(loc.getX()<getLocation().getX() && loc.getY()>getLocation().getY())
    		return true;
    	return false;
    }
    
    private boolean isUnder(Point2D loc) {
    	if(loc.getX()>getLocation().getX() && loc.getY()>getLocation().getY())
    		return true;
    	return false;
    }
    
    private boolean isAbove(Point2D loc) {
    	if(loc.getX()<getLocation().getX() && loc.getY()<getLocation().getY())
    		return true;
    	return false;
    }
    
    @Override
    public void update() {

    }
}
