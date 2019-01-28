package Entities;

import Enums.Elements;
import Enums.Directions;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public abstract class Character {
    protected float health;
    protected java.awt.geom.Point2D location;
    protected Elements currentElement;
    protected Rotate playerAngle;
    protected Directions characterDirection;
    protected boolean isShielded;
    protected int movementSpeed;
    protected final int WIDTH = 10;
    protected final float MAX_HEALTH = 100;

    private Rectangle attackHitbox = new Rectangle(WIDTH, WIDTH);

    public void moveUp() {
    	characterDirection = Directions.UP;

        double yCheck = location.getY() - movementSpeed - WIDTH / 2f;
        double xCheck = location.getX() - movementSpeed - WIDTH / 2f;

        if (yCheck >= 0 && xCheck >= 0) {
            location = location.add(-movementSpeed, -movementSpeed);
        }
    }

    public void moveDown(double boardWidth, double boardHeight) {
    	characterDirection = Directions.DOWN;
    	
        double yCheck = location.getY() + movementSpeed + WIDTH / 2f;
        double xCheck = location.getX() + movementSpeed + WIDTH / 2f;

        if (yCheck <= boardHeight && xCheck <= boardWidth) {
            location = location.add(movementSpeed, movementSpeed);
        }
    }

    public void moveLeft(double boardWidth) {
    	characterDirection = Directions.LEFT;
    	
        double xCheck = location.getX() - movementSpeed - WIDTH / 2f;
        double yCheck = location.getY() + movementSpeed + WIDTH / 2f;

        if (xCheck >= 0 && yCheck <= boardWidth) {
            location = location.add(-movementSpeed, movementSpeed);
        }
    }

    public void moveRight(double boardWidth, double boardHeight) {
    	characterDirection = Directions.RIGHT;
        //check within bounds

        double xCheck = location.getX() + movementSpeed + WIDTH / 2f;
        double yCheck = location.getY() - movementSpeed - WIDTH / 2f;


        if (xCheck <= boardWidth && yCheck >= 0) {
            location = location.add(movementSpeed, -movementSpeed);
        }
    }

    public void moveUpCartesian() {
    	characterDirection = Directions.UP;
    	
        if ((location.getY() - movementSpeed - WIDTH / 2f) >= 0) {
            location = location.add(0, -(movementSpeed * 2));
        } else {
            location = new Point2D(location.getX(), 0 + WIDTH / 2f);
        }
    }

    public void moveDownCartestian(double boardHeight) {
    	characterDirection = Directions.DOWN;

        if ((location.getY() + movementSpeed + WIDTH / 2f) <= boardHeight) {
            location = location.add(0, (movementSpeed * 2));
        } else {
            location = new Point2D(location.getX(), boardHeight - WIDTH / 2f);
        }

    }

    public void moveLeftCartesian() {
    	characterDirection = Directions.LEFT;
    	
        //check within bounds
        if ((location.getX() - movementSpeed - WIDTH / 2f) >= 0) {
            location = location.add(-(movementSpeed * 2), 0);
        } else {
            location = new Point2D(0 + WIDTH / 2f, location.getY());
        }

    }

    public void moveRightCartesian(double boardWidth) {
    	characterDirection = Directions.RIGHT;
    	
        //check within bounds
        if ((location.getX() + movementSpeed + WIDTH / 2f) <= boardWidth) {
            location = location.add((movementSpeed * 2), 0);
        } else {
            location = new Point2D(boardWidth - WIDTH / 2f, location.getY());
        }
    }

    public void lightAttack() {
        int damage = 3;

        //set attack hit box in front of the user
        //TODO: Change hitbox location based on rotation too, so the hitbox is in front of the player
        switch(characterDirection) {
        case Directions.UP: 
        	attackHitBox.setX(location.getX() - WIDTH);
        	attackHitbox.setY(location.getY() - WIDTH);
        	break;
        case Directions.DOWN:
        	attackHitBox.setX(location.getX() + WIDTH);
        	attackHitbox.setY(location.getY() + WIDTH);
        	break;
        case Directions.LEFT:
        	attackHitBox.setX(location.getX() - WIDTH);
        	attackHitbox.setY(location.getY() + WIDTH);
        	break;
        case Directions.RIGHT:
        	attackHitBox.setX(location.getX() + WIDTH);
        	attackHitbox.setY(location.getY() - WIDTH);
        	break;
        }

        //temp array for the other players
        Character otherCharacters[] = new Character[4];


        //If another Character is in the Hitbox calculate the damage they take
        // How is damaged dealt throught the victim or the attacker or server
        for (Character p : otherCharacters) {
            if (attackHitbox.intersects(p.getHitBox().getBoundsInParent())) {
                //TODO: What happens when you hit another Character
                //sends to server
            }
        }


    }

    public void removeHealth(float damage) {
        this.health -= damage;
    }

    public void heavyAttack() {


    }

    public void shield() {

        //need code to unshield after a certain duration
        isShielded = true;


    }

    public void changeToFire() {
        currentElement = Elements.FIRE;
    }

    public void changeToWater() {
        currentElement = Elements.WATER;
    }

    public void changeToEarth() {
        currentElement = Elements.EARTH;
    }

    public void changeToAir() {
        currentElement = Elements.AIR;
    }
    
    public Rotate getPlayerAngle() {
        return playerAngle;
    }

    public void setPlayerAngle(Rotate playerAngle) {
        this.playerAngle = playerAngle;
    }


    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location.add(WIDTH / 2f, WIDTH / 2f);
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public float getHealth() {
        return health;
    }

    public Rectangle getHitBox() {
        return new Rectangle(location.getX(), location.getY(), WIDTH, WIDTH);
    }


    public boolean getIsShielded() {
        return isShielded;
    }


    public Elements getCurrentElement() {
        return currentElement;
    }


    public boolean isShielded() {
        return isShielded;
    }

    public float getMAX_HEALTH() {
        return MAX_HEALTH;
    }

}
