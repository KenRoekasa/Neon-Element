package ai;

import ai.Elements;

public class TestPlayer {
	
	int hp;
	Elements activeElement;
	Position pos = new Position(0,0);
	
	int getHP() {
		return hp;
	}
	
	Position getPosition() {
		return pos;
	}
	
	Elements getElement() {
		return activeElement;
	}
	
}
