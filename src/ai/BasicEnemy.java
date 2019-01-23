package ai;
import java.lang.Thread;
import ai.Elements;
import java.util.Random;

public class BasicEnemy extends Thread {

	BasicEnemyStates activeState;
	Elements activeElement;
	int hp;
	Position pos;
	TestPlayer player;
	
	public BasicEnemy(TestPlayer player) {
		
		activeState = BasicEnemyStates.IDLE;
		assignRandomElement();
		hp = TestGame.maxHP;
		pos = new Position(0,0);
		this.player = player;

	}
	
	public void run () {
		
		boolean bool = true;
		
		while (bool) {
			
			BasicEnemyFSM.nextAction(this, player);
			
			if (hp<=0)
				bool=false;
		}
		
	}
	
	void assignRandomElement() {
		
		Random r = new Random();
		int rand = r.nextInt(4);
		switch(rand) {
		case 0: activeElement = Elements.EARTH;break;
		case 1:activeElement = Elements.FIRE; break;
		case 2:activeElement = Elements.WATER; break;
		case 3:activeElement = Elements.WIND; break;
		}
	}
	
	int getHP() {
		return hp;
	}
	
	Position getPosition() {
		return pos;
	}
	
	Elements getElement() {
		return activeElement;
	}
	
	BasicEnemyStates getActiveState() {
		return activeState;
	}
	
	void setElement(Elements e) {
		activeElement = e;
	}
	
	void setState(BasicEnemyStates s) {
		activeState  = s ;
	}
	//basic enemy actions
}
