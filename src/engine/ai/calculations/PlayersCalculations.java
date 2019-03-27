package engine.ai.calculations;

import java.util.ArrayList;

import engine.ai.controller.AiController;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.model.GameType;
import engine.model.ScoreBoard;
import engine.model.enums.Action;
import engine.model.enums.ObjectType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class PlayersCalculations {
	
	//AI controller object
	protected AiController aiCon;
	//player object being controlled
	protected Player aiPlayer;
	//Map of game
	protected Rectangle map;	
	//Score board object
	protected ScoreBoard scoreboard;
	//Movement calculations object
	protected MovementCalculations moveCalc;
	
	/**
	 * @param aiCon AI controller object
	 * @param map Map of game
	 * @param scoreboard Score board
	 * @param gameType Game Type
	 * @param moveCalc Movement calcualtions object
	 */
	public PlayersCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType, MovementCalculations moveCalc) {
		this.scoreboard = scoreboard;
		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.map = map;
		this.moveCalc = moveCalc;
	}

	/**
	 * Calculates if there is a player that is too close to AI player.
	 * @return True if there is a player that is too close to AI player, false otherwise
	 */
	public boolean playerIsTooClose() {
		Point2D playerLoc = getNearestPlayer().getLocation();
		return isTooClose(playerLoc);
	}
	
	/**
	 * Calculates score difference between this AI and player with highest score.
	 * @param score Threshold for the difference
	 * @return True if score difference between this AI and player with highest score is more than or equal to the parameter score, false otherwise
	 */
	public boolean scoreDifferenceIsMoreThan(int score) {
		Player winner = getWinningPlayer();
		int difference = scoreboard.getPlayerKills(winner.getId()) - scoreboard.getPlayerKills(aiPlayer.getId()) ;
		return difference >= score;
	}
	
	/**
	 * Calculates the score of given player
	 * @param player that its score is enquired about
	 * @return Score of player
	 */
	public double getScore(Player player) {
		return scoreboard.getPlayerKills(player.getId());
	}
	
	/**
	 * Calculates if some close player is charging a heavy attack
	 * @return True if there is some close player charging a heavy attack, false otherwise
	 */
	public boolean someoneCloseIsCharging() {
		ArrayList<Player> players = getOtherPlayers();
		for (Player player : players) {
			if(isCharging(player) && moveCalc.calcDistance(aiPlayer.getLocation(), player.getLocation()) < map.getWidth()*0.1)
				return true;
		}
		return false;
	}
	
	/**
	 * Calculates if given player is in attack distance, i.e. if an attack was done right now, the other player will take damage.
	 * @param player Player that its distance is enquired about
	 * @return True if player is in attack distance, false otherwise
	 */
	public boolean inAttackDistance(Player player) {
		if ( moveCalc.calcDistance(aiPlayer.getLocation(), player.getLocation()) < aiPlayer.getLightAttackRange())
			return true;
		return false;
	}
	
	/**
	 * Calculates if given player is charging a heavy attack 
	 * @param player Player that its charging state is enquired about
	 * @return True if player is charging a heavy attack, false otherwise
	 */
	public boolean isCharging(Player player) {
		return player.getCurrentAction().equals(Action.CHARGE);
	}
	
	/**
	 * Calculates if the object in the given location is too close, less than 0.2 of maps width, to this AI player
	 * @param loc Location of the object
	 * @return True if distance of this AI player to the object in location 'loc' is less than 0.2 of maps width, false otherwise
	 */
	public boolean isTooClose(Point2D loc ) {
		Point2D aiLoc = aiPlayer.getLocation();
		return (aiLoc.distance(loc)<(map.getWidth()*0.2));
	}
	
	/**
	 * Calculates if the given player is the nearest player to this AI player
	 * @param player Player to know if whether it is the nearest
	 * @return True if player is the nearest player to this AI player, false otherwise
	 */
	public boolean isNearestPlayer(Player player) {
		return (player.equals(getNearestPlayer()));
	}
	
	/**
	 * Calculates the distance from this AI's location to all other player.
	 * @return Object of player that is the nearest to this AI player
	 */
	public Player getNearestPlayer() {
		double minDis = Double.MAX_VALUE;
		ArrayList<Player> players = getOtherPlayers();
		int index = 0;
		for (int i = 0; i < players.size(); i++) {
			double tempDis = moveCalc.calcDistance(players.get(i).getLocation(), aiPlayer.getLocation());
			if (tempDis < minDis) {
				minDis = tempDis;
				index = i;
			}
		}

		double angle = moveCalc.calcAngle(aiPlayer.getLocation(), players.get(index).getLocation());

		aiPlayer.setPlayerAngle(new Rotate(angle));

		return players.get(index);
	}
	
	/**
	 * @return Array list of all players except this AI player
	 */
	public ArrayList<Player> getOtherPlayers(){
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<PhysicsObject> objects = new ArrayList<>();
		objects.addAll(aiCon.getObjects());
		for(PhysicsObject object : objects) {
			if( object.getTag() == (ObjectType.PLAYER) || ( object.getTag() == (ObjectType.ENEMY) && !object.equals(aiPlayer) )  ) {
				players.add((Player)object);
			}
		}
		return players;
	}

	/**
	 * @return Array list of all players AI and non-AI
	 */
	public ArrayList<Player> getAllPlayers(){
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<PhysicsObject> objects = new ArrayList<>();
		objects.addAll(aiCon.getObjects());
		for(PhysicsObject object : objects) {
			if( object.getTag() == (ObjectType.ENEMY) || object.getTag() == (ObjectType.PLAYER) ) {
				players.add((Player)object);
			}
		}
		return players;
	}
	
	/**
	 * @return Array list of non-AI players only
	 */
	public ArrayList<Player> getRealPlayers(){
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<PhysicsObject> objects = new ArrayList<>();
		objects.addAll(aiCon.getObjects());
		for(PhysicsObject object : objects) {
			if( object.getTag() == (ObjectType.PLAYER) ) {
				players.add((Player)object);
			}
		}
		return players;
	}
	
	/**
	 * @return Player object with lowest health
	 */
	public Player getPlayerWithLowestHealth() {
		ArrayList<Player> players = getOtherPlayers();
		Player lowestHealthPlayer = players.get(0);
		float minH = aiPlayer.getMAX_HEALTH();
		for (Player player : players) {
			if(player.getHealth()<minH) {
				minH = player.getHealth();
				lowestHealthPlayer = player;
			}
		}
		return lowestHealthPlayer;
	}
	
	/**
	 * Calculates which player has the highest score
	 * @return Object of player with highest score
	 */
	public Player getWinningPlayer() {
		ArrayList<Player> players = getAllPlayers();
		int  id =(scoreboard.getLeaderBoard().get(0));
		Player winningPlayer = null;
		for (Player player : players) {
			if(player.getId() == id)
				winningPlayer = player;
		}
		return winningPlayer;
	}
	
}
