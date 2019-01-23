package ai;

//finite state machine class which acts as a brain; switches between states and elements when appropriate
public class BasicEnemyFSM {
	
	
	static void nextAction(BasicEnemy enemy, TestPlayer player) {
		
		int enemyHP = enemy.getHP();
		Elements enemyElement = enemy.getElement();
		Position enemyPosition = enemy.getPosition();
		
		BasicEnemyStates enemyState = enemy.getActiveState();
		
		
		int playerHP = player.getHP();
		Position plyaerPosition = player.getPosition();
		Elements playerElement = player.getElement();
		
		if(playerHP< (TestGame.maxHP/4) ) {
			enemy.setState(BasicEnemyStates.ATTACKING);
		}
		else if(enemyHP< (TestGame.maxHP/4) ) {
			enemy.setState(BasicEnemyStates.ESCAPING);
		}
		
		
		
	}
	
}
