package engine.ai;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.enums.AiType;
import engine.enums.ObjectType;
import javafx.scene.shape.Rectangle;

public class AiControllersManager {
	
	private ArrayList<AiController> controllers;
	private ArrayList<PhysicsObject> objects;
	private Rectangle map;
	private Player player;
	

	public AiControllersManager(ArrayList<PhysicsObject> objects, Rectangle map, Player player) {
		controllers = new ArrayList<>();
		this.objects = objects;
        this.map = map;
        this.player = player;
	}
	
	public Player addAi(AiType type) {
		controllers.add(new AiController(new Player(ObjectType.ENEMY),objects, map, player,type));
		return controllers.get(controllers.size()-1).getAiPlayer();
	}
	
	public void updateAllAi() {
		for(AiController con : controllers) 
			con.update();
	}
	
	public void startAllAi() {
		(new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime=System.nanoTime(),endTime,delta;
				while(true) {
					endTime=System.nanoTime();
					delta = (endTime-startTime)/1000000;
					if( delta > 1 ) {
						startTime = endTime;
						for(AiController con : controllers) 
							con.update();
					}
					else {
						try {
				            TimeUnit.MILLISECONDS.sleep(1-delta);
				        } catch (InterruptedException e) {
				            // TODO Auto-generated catch block
				            e.printStackTrace();
				        }
					}
				}
			}
		})).start();
	}
}