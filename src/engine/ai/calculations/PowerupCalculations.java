package engine.ai.calculations;

import java.util.ArrayList;

import engine.ai.controller.AiController;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.ObjectType;
import engine.enums.PowerUpType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class PowerupCalculations {

	//AI controller object
	protected AiController aiCon;
	//player object being controlled
	protected Player aiPlayer;
	//Map of game
	protected Rectangle map;
	//Array list of power ups
	protected ArrayList<PowerUp> powerups;
	//Player calculations object
	protected PlayersCalculations playerCalc;
	
	/**
	 * @param aiCon AI controller object
	 * @param map Map of game
	 * @param playerCalc Player calculations object
	 */
	public PowerupCalculations(AiController aiCon, Rectangle map, PlayersCalculations playerCalc) {

		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.map = map;
		this.playerCalc = playerCalc;
		powerups = new ArrayList<>();
		
	}
	
	/**
	 * Calculates location of neatest power up
	 * @return Index of nearest power up in 'powerups' array list, -1 if no power up is available
	 */
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

	/**
	 * Calculates if a power up of given type exists in 'powerups' array list
	 * @param pu Power up type enquired about
	 * @return True if a power up of type given exists in 'powerups' array list, false otherwise
	 */
	public boolean powerUpExist(PowerUpType pu) {
		ArrayList<PowerUp> powerups = getPowerups();
		for (int i = 0; i < powerups.size(); i++) {
			if (powerups.get(i).getType().equals(pu)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates location of power ups of type given
	 * @param pu Power up type enquired about
	 * @return Index of nearest power up of type given in 'powerups' array list, -1 if no power up of given type exist 
	 */
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
	
	/**
	 * @return Array list of all power ups
	 */
	public ArrayList<PowerUp> getPowerups(){
		updatePowerups();
		return powerups;
	}
	
	/**
	 * Calculates locations of nearest power up and player, and see which is closer
	 * @return True if nearest power up is closer than nearest player, false otherwise
	 */
	public boolean powerupCloserThanPlayer() {
		int puIndex = getNearestPowerUp();
		if(puIndex == -1)
			return false;
		double disToPu = powerups.get(puIndex).getLocation().distance(aiPlayer.getLocation());
		double disToPlayer = playerCalc.getNearestPlayer().getLocation().distance(aiPlayer.getLocation());
		
		return disToPu<disToPlayer;
	}
	
	/**
	 * Calculates distance between this AI player and locations of power ups available 
	 * @return True if distance to nearest power up is less than 0.2 of map's width, false otherwise
	 */
	public boolean powerupIsTooClose() {
		int index = getNearestPowerUp();
		if(index != -1 && isTooClose(powerups.get(index).getLocation())) {
			return true;
		}
		return false;
	}
	
	/**
	 * Updates 'powerups' array list from objects array list
	 */
	private void updatePowerups() {
		
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
	
	/**
	 * Calculates distance between AI player's location and given location
	 * @param loc Location to calculate distance to
	 * @return True distance between this AI player's location and given location is less than 0.2 of map's width, false otherwise
	 */
	private boolean isTooClose(Point2D loc ) {
		Point2D aiLoc = aiPlayer.getLocation();
		return (aiLoc.distance(loc)<(map.getWidth()*0.2));
	}
	
	/**
	 * Calculates distance between two points
	 * @param a The first point 
	 * @param b The second point
	 * @return distance between point a and b
	 */
	private double calcDistance(Point2D a, Point2D b) {
		return a.distance(b);
	}

}
