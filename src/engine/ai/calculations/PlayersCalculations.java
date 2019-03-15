package engine.ai.calculations;

import java.util.ArrayList;

import engine.ScoreBoard;
import engine.ai.controller.AiController;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.enums.Action;
import engine.enums.ObjectType;
import engine.gameTypes.GameType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class PlayersCalculations {
	
	protected AiController aiCon;
	protected Player aiPlayer;
	protected Rectangle map;	
	protected Player player;
	protected ScoreBoard scoreboard;
	protected MovementCalculations moveCalc;
	
	public PlayersCalculations(AiController aiCon, Rectangle map, ScoreBoard scoreboard, GameType gameType, MovementCalculations moveCalc) {
		this.scoreboard = scoreboard;
		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.map = map;
		this.player = aiCon.getPlayer();
		this.moveCalc = moveCalc;
	}


	public boolean playerIsTooClose() {
		Point2D playerLoc = getNearestPlayer().getLocation();
		return isTooClose(playerLoc);
	}
	
	public boolean scoreDifferenceIsMoreThan(int score) {
		Player winner = getWinningPlayer();
		int difference = scoreboard.getPlayerKills(winner.getId()) - scoreboard.getPlayerKills(aiPlayer.getId()) ;
		return difference >= score;
	}
	
	public double getScore(Player player) {
		return scoreboard.getPlayerKills(player.getId());
	}
	
	public boolean someoneCloseIsCharging() {
		ArrayList<Player> players = getOtherPlayers();
		for (Player player : players) {
			if(isCharging(player) && moveCalc.calcDistance(aiPlayer.getLocation(), player.getLocation()) < map.getWidth()*0.1)
				return true;
		}
		return false;
	}
	
	public boolean inAttackDistance(Player player) {
		if ((int) moveCalc.calcDistance(aiPlayer.getLocation(), player.getLocation()) - aiPlayer.getWidth() < aiPlayer.getWidth())
			return true;
		return false;
	}
	
	public boolean isCharging(Player player) {
		return player.getCurrentAction().equals(Action.CHARGE);
	}
	
	protected boolean isTooClose(Point2D playerLoc ) {
		Point2D aiLoc = aiPlayer.getLocation();
		return (aiLoc.distance(playerLoc)<(map.getWidth()*0.2));
	}
	
	public boolean isNearestPlayer(Player player) {
		return (player.equals(getNearestPlayer()));
	}
	
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
	
}
