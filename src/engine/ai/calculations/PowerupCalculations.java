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

	protected AiController aiCon;
	protected Player aiPlayer;
	protected Rectangle map;
	protected ArrayList<PowerUp> powerups;
	protected PlayersCalculations playerCalc;
	
	public PowerupCalculations(AiController aiCon, Rectangle map, PlayersCalculations playerCalc) {

		this.aiCon = aiCon;
		this.aiPlayer = aiCon.getAiPlayer();
		this.map = map;
		this.playerCalc = playerCalc;
		powerups = new ArrayList<>();
		
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

	public boolean powerUpExist(PowerUpType pu) {
		ArrayList<PowerUp> powerups = getPowerups();
		for (int i = 0; i < powerups.size(); i++) {
			if (powerups.get(i).getType().equals(pu)) {
				return true;
			}
		}
		return false;
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
	
	public ArrayList<PowerUp> getPowerups(){
		updatePowerups();
		return powerups;
	}
	
	public boolean powerupCloserThanPlayer() {
		int puIndex = getNearestPowerUp();
		if(puIndex == -1)
			return false;
		double disToPu = powerups.get(puIndex).getLocation().distance(aiPlayer.getLocation());
		double disToPlayer = playerCalc.getNearestPlayer().getLocation().distance(aiPlayer.getLocation());
		
		return disToPu<disToPlayer;
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
	
	public boolean powerupIsTooClose() {
		int index = getNearestPowerUp();
		if(index != -1 && isTooClose(powerups.get(index).getLocation())) {
			return true;
		}
		return false;
	}
	
	private boolean isTooClose(Point2D loc ) {
		Point2D aiLoc = aiPlayer.getLocation();
		return (aiLoc.distance(loc)<(map.getWidth()*0.2));
	}
	
	private double calcDistance(Point2D a, Point2D b) {
		return a.distance(b);
	}

}
