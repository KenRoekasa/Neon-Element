package Entities;


import java.lang.Thread;
import java.util.Random;
import Enums.*;;

public class BasicEnemy extends Character {

	BasicEnemyStates activeState;
	Character[] players;
	
	public BasicEnemy(Character players[]) {
		
		activeState = BasicEnemyStates.IDLE;
		assignRandomElement();
		this.players = players;

	}
	
	public void start () {
		
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				boolean bool = true;
				
				while (bool) {
					
					BasicEnemyFSM.nextAction(this, players);
					
					if (health<=0)
						bool=false;
				}
			}
			
		});
		
	}
	
	void assignRandomElement() {
		
		Random r = new Random();
		int rand = r.nextInt(4);
		switch(rand) {
		case 0: changeToEarth();break;
		case 1: changeToFire(); break;
		case 2: changeToWater(); break;
		case 3: changeToAir(); break;
		}
	}
	
	
	BasicEnemyStates getActiveState() {
		return activeState;
	}
	
	void setState(BasicEnemyStates s) {
		activeState  = s ;
	}
	//basic enemy actions
}
