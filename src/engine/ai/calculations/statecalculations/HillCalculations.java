package engine.ai.calculations.statecalculations;

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

	protected double circleX;
	protected double circleY;
	protected Point2D circleCentre;
	protected double circleRadius;
	
	public HillCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		super(aiCon, map, scoreboard, gameType);
		setCircleCoordination(gameType);
	}
	
	private void setCircleCoordination(GameType gameType) {
		HillGame hillGame = (HillGame) gameType;
		circleX = hillGame.getHill().getCenterX();
		circleY = hillGame.getHill().getCenterY();
		circleCentre = new Point2D(circleX,circleY);
		circleRadius = hillGame.getHill().getRadius();
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

	public boolean onHillEdge() {
		double locX = aiPlayer.getLocation().getX();
		double locY = aiPlayer.getLocation().getY();
		double distance = Math.sqrt( Math.pow((locX-circleX), 2) + Math.pow((locY-circleY), 2) );
		if( (distance - circleRadius) < map.getHeight()*0.05 )
			return true;
		return false;
	}

	public boolean closeToHill() {
		return closeToHill(aiPlayer);
	}

	public boolean closeToHill(Player player) {
		double locX = player.getLocation().getX();
		double locY = player.getLocation().getY();
		double distance = Math.sqrt( Math.pow((locX-circleX), 2) + Math.pow((locY-circleY), 2) );
		return distance < circleRadius*4;
	}

}
