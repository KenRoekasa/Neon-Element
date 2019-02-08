package entities;

import java.util.Random;

import ai.EnemyFSM;
import ai.EnemyStates;
import enums.PowerUpType;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Enemy extends Character {

	EnemyStates activeState;
    Character [] players;
    PowerUp [] powerups;
    Enemy enemy  = this;
    int powerupIndex = -1;

	
    public Enemy(Character [] players, PowerUp [] powerUps) {

        activeState = EnemyStates.IDLE;
        //default random
        assignRandomElement();
        this.players = players;
        this.powerups = powerUps;
        location = new Point2D(10, 100);
        playerAngle = new Rotate(0);
        health = 100;
        movementSpeed = 2;
        isShielded = false;
        width = 20;
    }
    
    public void startBasicAI() {
		System.out.println("enemy started");
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
            	System.out.println("in thread");
                boolean bool = true;
                int ctr=0;
                while (bool) {
                	
                	System.out.print("while: "+(ctr++) + "\n");
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
    	if(this.getLocation().distance(player.getLocation())<getWidth())
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
		return players[index];
	}



    public void attack() {
    	Character player = findNearestPlayer();
    	moveTo(player);
    	if(inAttackDistance(player)) {
    		lightAttack();
    	}
    }
    

    public double calcDistance(Point2D x, Point2D y) {
    	return x.distance(y);
    }

    
    public void moveTo(PowerUp powerup) {
    	System.out.println("enemy moving to powerup");
    	Point2D powerupLoc = powerup.getLocation();
    	double distance = calcDistance(getLocation(),powerupLoc );

    	while( (int) distance - 1 > getWidth() ) {

    		if( (getLocation().getX() - powerupLoc.getX()) > 0 )
    			setLocation(new Point2D( getLocation().getX()-getMovementSpeed(), getLocation().getY()) );
    		else if( (getLocation().getX() - powerupLoc.getX()) < 0 )
    			setLocation(new Point2D( getLocation().getX()+getMovementSpeed(), getLocation().getY()) );

    		if( (getLocation().getY() - powerupLoc.getY()) > 0 )
    			setLocation(new Point2D( getLocation().getX(), getLocation().getY()-getMovementSpeed()) );

    		else if( (getLocation().getY() - powerupLoc.getY()) < 0 )
        			setLocation(new Point2D( getLocation().getX(), getLocation().getY()+getMovementSpeed()) );

    		distance = calcDistance(getLocation(), powerupLoc);
    	}
    }
    
    public void moveTo(Character player) {
    	System.out.println("enemy moving to player");
    	Point2D playerLoc = player.getLocation();
    	double distance = calcDistance(getLocation(),playerLoc );

    	while( (int) distance + 1 > getWidth() ) {

    		if(isAbove(player))
    			moveUp();
    		else if(isUnder(player))
    			moveDown(2000,2000);
    		if(isLeftOf(player))
    			moveLeft(2000);
    		else if (isRightOf(player))
    			moveRight(2000,2000);
    		
    		/*if( (getLocation().getX() - playerLoc.getX()) > 0 )
    			setLocation(new Point2D( getLocation().getX()-getMovementSpeed(), getLocation().getY()) );
    		else if( (getLocation().getX() - playerLoc.getX()) < 0 )
    			setLocation(new Point2D( getLocation().getX()+getMovementSpeed(), getLocation().getY()) );

    		if( (getLocation().getY() - playerLoc.getY()) > 0 )
    			setLocation(new Point2D( getLocation().getX(), getLocation().getY()-getMovementSpeed()) );

    		else if( (getLocation().getY() - playerLoc.getY()) < 0 )
        			setLocation(new Point2D( getLocation().getX(), getLocation().getY()+getMovementSpeed()) );

    		distance = calcDistance(getLocation(), playerLoc);
    		*/
    	}
    }
    private boolean isRightOf(Character player) {
    	Point2D playerLoc = player.getLocation();
    	double distance = calcDistance(getLocation(),playerLoc );
    	if(player.getLocation().getX()>getLocation().getX() && player.getLocation().getY()<getLocation().getY())
    		return true;
    	return false;
    }

    private boolean isLeftOf(Character player) {
    	Point2D playerLoc = player.getLocation();
    	double distance = calcDistance(getLocation(),playerLoc );
    	if(player.getLocation().getX()<getLocation().getX() && player.getLocation().getY()>getLocation().getY())
    		return true;
    	return false;
    }
    
    private boolean isUnder(Character player) {
    	Point2D playerLoc = player.getLocation();
    	double distance = calcDistance(getLocation(),playerLoc );
    	if(player.getLocation().getX()>getLocation().getX() && player.getLocation().getY()>getLocation().getY())
    		return true;
    	return false;
    }
    
    private boolean isAbove(Character player) {
    	Point2D playerLoc = player.getLocation();
    	double distance = calcDistance(getLocation(),playerLoc );
    	if(player.getLocation().getX()<getLocation().getX() && player.getLocation().getY()<getLocation().getY())
    		return true;
    	return false;
    }
    
    @Override
    public void update() {

    }
}
