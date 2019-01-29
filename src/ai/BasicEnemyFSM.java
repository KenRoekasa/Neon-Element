package ai;
import Entities.Character;
import javafx.geometry.Point2D;
import Enums.Elements;

//finite state machine class which acts as a brain; switches between states and elements when appropriate
public class BasicEnemyFSM {
	
	
	public static void nextAction(BasicEnemy enemy, Character player) {

		float enemyHP = enemy.getHealth();
		Elements enemyElement = enemy.getCurrentElement();
		Point2D enemyPosition = enemy.getLocation();
		
		BasicEnemyStates enemyState = enemy.getActiveState();
		
		
		float playerHP = player.getHealth();
		Point2D plyaerPosition = player.getLocation();
		Elements playerElement = player.getCurrentElement();
		
		if(playerHP< (TestGame.maxHP/4) ) {
			enemy.setState(BasicEnemyStates.ATTACKING);
		}
		else if(enemyHP< (TestGame.maxHP/4) ) {
			enemy.setState(BasicEnemyStates.ESCAPING);
		}
		
		
		
	}
	
}
