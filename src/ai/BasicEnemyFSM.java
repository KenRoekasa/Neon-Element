package ai;
import entities.Character;
import javafx.geometry.Point2D;
import enums.Elements;
import ai.BasicEnemyStates;
import entities.BasicEnemy;
import enums.PowerUpType;
import entities.PowerUp;

//finite state machine class which acts as a brain; switches between states and elements when appropriate
public class BasicEnemyFSM {


	public static void fetchAction(BasicEnemy basicEnemy, Character [] players, PowerUp[] powerups) {

		float maxHP = basicEnemy.getMAX_HEALTH();

		float enemyHP = basicEnemy.getHealth();
		Elements enemyElement = basicEnemy.getCurrentElement();
		Point2D enemyPosition = basicEnemy.getLocation();
		BasicEnemyStates enemyState = basicEnemy.getActiveState();

		Character nearestPlayer = findNearest(basicEnemy, players);
		float playerHP = nearestPlayer.getHealth();
		Point2D plyaerPosition = nearestPlayer.getLocation();
		Elements playerElement = nearestPlayer.getCurrentElement();

		if(enemyHP< (maxHP/4) || (findClosestPowerUp(basicEnemy , nearestPlayer ,powerups ,PowerUpType.DAMAGE) !=-1 && enemyHP<maxHP) ) {
			if(!basicEnemy.isShielded())
				basicEnemy.shield();
			basicEnemy.setState(BasicEnemyStates.FIND_HEALTH);
		}
		else if( enemyHP>playerHP || playerHP< (maxHP/2) ) {
			basicEnemy.setState(BasicEnemyStates.ATTACK);
		}
		else if(playerHP< (maxHP/4) ) {
			basicEnemy.setState(BasicEnemyStates.AGGRESSIVE_ATTACK);
		}
		else if( findClosestPowerUp(basicEnemy , nearestPlayer ,powerups ,PowerUpType.DAMAGE) !=-1  ) {
			basicEnemy.setState(BasicEnemyStates.FIND_DAMAGE);
		}
		else if( findClosestPowerUp(basicEnemy , nearestPlayer ,powerups ,PowerUpType.SPEED) !=-1  ) {
			basicEnemy.setState(BasicEnemyStates.FIND_SPEED);
		}

	}

	private static Character findNearest(Character enemy,Character [] players) {
		double minDis = Double.MAX_VALUE;
		int index = 0;
		for (int i = 0; i < players.length; i++) {
			double tempDis = calcDistance(players[i],enemy);
			if( tempDis<minDis ) {
				minDis = tempDis;
				index = i;
			}
		}
		return players[index];
	}

	private static double calcDistance(Character x , Character y) {
		Point2D locX = x.getLocation();
		Point2D locY = y.getLocation();
		return locX.distance(locY);
	}

	private static int findClosestPowerUp(Character enemy, Character player ,PowerUp [] puList , PowerUpType pu) {
		int index=-1;
		double distance = Double.MAX_VALUE;
		for (int i = 0; i < puList.length; i++) {
			if(puList[i].getType().equals(pu)) {
				double disToPU = puList[i].getLocation().distance(enemy.getLocation());
				if( disToPU < calcDistance(enemy,player) ) {
					if(disToPU < distance) {
						distance = disToPU;
						index = i;
					}
				}
			}
		}

		return index;
	}
	
}
