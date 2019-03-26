package engine.ai.calculations.stateCalculations;

import java.util.ArrayList;

import engine.ScoreBoard;
import engine.ai.calculations.AiCalculations;
import engine.ai.controller.AiController;
import engine.entities.Player;
import engine.gameTypes.GameType;
import engine.gameTypes.HillGame;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class HillCalculations extends AiCalculations {

	//X component of Hill's centre
	protected double circleX;
	//Y component of Hill's centre
	protected double circleY;
	//X and Y packed in Point2d object of Hill's centre
	protected Point2D circleCentre;
	//Hill's circle radius
	protected double circleRadius;
	
	/**
	 * @param aiCon AI controller
	 * @param map Map of game
	 * @param scoreboard Score board
	 * @param gameType Game type
	 */
	public HillCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		super(aiCon, map, scoreboard, gameType);
		setCircleCoordination(gameType);
	}
	
	/**
	 * Sets Hill's circle coordinations 
	 * @param gameType Game type, coordinations can be extracted from this object
	 */
	private void setCircleCoordination(GameType gameType) {
		HillGame hillGame = (HillGame) gameType;
		circleX = hillGame.getHill().getCenterX();
		circleY = hillGame.getHill().getCenterY();
		circleCentre = new Point2D(circleX,circleY);
		circleRadius = hillGame.getHill().getRadius();
	}	

	/**
	 * @return Point2D object of center of hill
	 */
	public Point2D getHillCentreLocation() {
		return circleCentre; 
	}
	
	/**
	 * Calculates if given location is inside Hill's circle
	 * @param loc Location
	 * @return True if given location is on hill, false otherwise
	 */
	public boolean onHill(Point2D loc) {
		double locX = loc.getX();
		double locY = loc.getY();
		double distance = Math.sqrt( Math.pow((locX-circleX), 2) + Math.pow((locY-circleY), 2) );
		return distance < circleRadius;
	}
	
	/**
	 * Calculates if a player is within 0.1 of map's width of hill's centre 
	 * @return Player object of the player who is within 0.1 of map's width of hill's centre, nearest player otherwise
	 */
	public Player getOnHillPlayer() {
		ArrayList<Player> players = getPlayerCalc().getAllPlayers();
		Player onHillPlayer = null;
		for (Player player : players) {
			if(player.getLocation().distance(circleCentre) < (map.getWidth()*0.01)) {
				onHillPlayer = player;
				break;
			}
		}
		if(onHillPlayer == null)
			return getPlayerCalc().getNearestPlayer();
		return onHillPlayer;
	}

	/**
	 * Calculates if this AI player is on edge of the hill
	 * @return True if this AI's location is on edge of the hill
	 */
	public boolean onHillEdge() {
		double locX = aiPlayer.getLocation().getX();
		double locY = aiPlayer.getLocation().getY();
		double distance = Math.sqrt( Math.pow((locX-circleX), 2) + Math.pow((locY-circleY), 2) );
		if( (distance - circleRadius) < map.getHeight()*0.05 )
			return true;
		return false;
	}

	/**
	 * Calculates distance between this AI player's location and hills centre
	 * @return True if distance between this AI player's location and hills centre is less than 4*circle radius, false otherwise
	 */
	public boolean closeToHill() {
		return closeToHill(aiPlayer);
	}

	/**
	 * Calculates distance between given player's location and hills centre
	 * @param player Player to check its location
	 * @return True if distance between this AI player's location and hills centre is less than 4*circle radius, false otherwise
	 */
	public boolean closeToHill(Player player) {
		double locX = player.getLocation().getX();
		double locY = player.getLocation().getY();
		double distance = Math.sqrt( Math.pow((locX-circleX), 2) + Math.pow((locY-circleY), 2) );
		return distance < circleRadius*4;
	}

}
