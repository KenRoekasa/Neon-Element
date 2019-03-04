package engine.calculations;

import static engine.gameTypes.GameType.Type.Hill;

import java.util.ArrayList;

import engine.ScoreBoard;
import engine.ai.AiController;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.Action;
import engine.enums.ObjectType;
import engine.enums.PowerUpType;
import engine.gameTypes.GameType;
import engine.gameTypes.HillGame;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class AiCalculations {
	
	public final int DELAY_TIME = 28;
	private AiController aiCon;
	private Player aiPlayer;
	private Rectangle map;	
	private Player player;
	private ArrayList<PowerUp> powerups;
	private GameType gameType;
	private ScoreBoard scoreboard;
	private float startTime;
	private double circleX;
	private double circleY;
	private Point2D circleCentre;
	private double circleRadius;
	
	public AiCalculations(AiController aiCon,Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		this.aiCon = aiCon;
		this.map = map;
		this.scoreboard = scoreboard;
		this.gameType = gameType;
		
		player = aiCon.getPlayer();
		aiPlayer = aiCon.getAiPlayer();
		powerups = new ArrayList<>();
		startTime = System.nanoTime()/1000000000;
		
		setCircleCoordination();
	}
	
	private void setCircleCoordination() {
		if(gameType.getType().equals(Hill)) {
			HillGame hillGame = (HillGame) gameType;
			circleX = hillGame.getHill().getCenterX();
			circleY = hillGame.getHill().getCenterY();
			circleCentre = new Point2D(circleX,circleY);
			circleRadius = hillGame.getHill().getRadius();
		}
	}
	
	public Point2D getHillCentreLocation() {
		return circleCentre; 
	}
	
	public boolean onHill(Point2D loc) {
		double locX = loc.getX();
		double locY = loc.getY();
		double distance = Math.sqrt( Math.pow((locX-circleX), 2) + Math.pow((locY-circleY), 2) );
		return distance < circleRadius;
	}

	public float secondsElapsed() {
		float endTime = System.nanoTime()/1000000000;
		float elapsedTime = endTime - startTime;
		return elapsedTime;
	}
	
	public void setStartTime(float time) {
		startTime = time;
	}


	public ArrayList<Player> getOtherPlayers(){
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<PhysicsObject> objects = new ArrayList<>();
		objects.addAll(aiCon.getObjects());
		for(PhysicsObject object : objects) {
			if( object.getTag() == (ObjectType.ENEMY) && !object.equals(aiPlayer)  ) {
				players.add((Player)object);
			}
		}
		players.add(player);
		return players;
	}

	public ArrayList<Player> getPlayers(){
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<PhysicsObject> objects = new ArrayList<>();
		objects.addAll(aiCon.getObjects());
		for(PhysicsObject object : objects) {
			if( object.getTag() == (ObjectType.ENEMY) ) {
				players.add((Player)object);
			}
		}
		players.add(player);
		return players;
	}
	
	public Player getWinningPlayer() {
		ArrayList<Player> players = getPlayers();
		int  id =(scoreboard.getLeaderBoard().get(0));
		Player winningPlayer = null;
		for (Player player : players) {
			if(player.getId() == id)
				winningPlayer = player;
		}
		return winningPlayer;
	}
	
	public boolean killDifferenceIsMoreThan(int kills) {
		Player winner = getWinningPlayer();
		int difference = scoreboard.getPlayerKills(winner.getId()) - scoreboard.getPlayerKills(aiPlayer.getId()) ;
		return difference >= kills;
	}
	
	
	public void updatePowerups() {

		powerups.clear();
		ArrayList<PhysicsObject> objects = new ArrayList<>();
		objects.addAll(aiCon.getObjects());
		for (int i = 0; i < objects.size(); i++) {
			if (!objects.get(i).equals(null)) {
				if ( (objects.get(i).getTag()) ==  ObjectType.POWERUP)
					if (!powerups.contains(objects.get(i)))
						powerups.add((PowerUp) objects.get(i));
			}
		}
	}
	
	public boolean isCharging(Player player) {
		return player.getCurrentAction().equals(Action.CHARGE);
	}

	public boolean playerIsTooClose() {
		Point2D playerLoc = getNearestPlayer().getLocation();
		return isTooClose(playerLoc);
	}
	
	public boolean powerupIsTooClose() {
		int index = getNearestPowerUp();
		if(index != -1 && isTooClose(powerups.get(index).getLocation())) {
			return true;
		}
		return false;
	}
	
	private boolean isTooClose(Point2D playerLoc ) {
		Point2D aiLoc = aiPlayer.getLocation();
		return (aiLoc.distance(playerLoc)<(map.getWidth()*0.2));
	}


	public boolean reachedAnEdge() {
		double x = aiPlayer.getLocation().getX();
		double y = aiPlayer.getLocation().getY();
		if(x<map.getHeight()*0.02 || x>map.getHeight()*0.98 || y<map.getWidth()*0.02 || y>map.getWidth()*0.98)
			return true;
		return false;
	}
	
	/*returns integer value, between 0 and 7 inclusive, to indicate movement direction to move away from edge
	 * 0 = down 
	 * 1 = right cart
	 * 2 = down cart
	 * 3 = right 
	 * 4 = up cart
	 * 5 = left 
	 * 6 = left cart
	 * 7 = up 
	 */
	public int closestEdgeLocation() {
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
	
	public double calcAngle(Point2D loc1, Point2D loc2) {

		double x = loc1.getX() - loc2.getX();
		double y = loc1.getY() - loc2.getY();
		double angle = Math.toDegrees(Math.atan2(y, x)) - 90.0;
		return angle;
	}

	public double calcDistance(Point2D a, Point2D b) {
		return a.distance(b);
	}
	
	public boolean powerupCloserThanPlayer() {
		int puIndex = getNearestPowerUp();
		if(puIndex == -1)
			return false;
		double disToPu = powerups.get(puIndex).getLocation().distance(aiPlayer.getLocation());
		double disToPlayer = getNearestPlayer().getLocation().distance(aiPlayer.getLocation());
		
		return disToPu<disToPlayer;
	}


	public boolean inAttackDistance(Player player) {
		if ((int) calcDistance(aiPlayer.getLocation(), player.getLocation()) - aiPlayer.getWidth() < aiPlayer.getWidth())
			return true;
		return false;
	}
	
	public int getNearestPowerUp() {
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

	public int getNearestPowerUp(PowerUpType pu) {
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

	public Player getNearestPlayer() {
		double minDis = Double.MAX_VALUE;
		ArrayList<Player> players = getOtherPlayers();
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
	
	
	public ArrayList<PowerUp> getPowerups(){
		updatePowerups();
		return powerups;
	}
	


	
}
