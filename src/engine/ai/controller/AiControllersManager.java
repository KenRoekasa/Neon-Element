package engine.ai.controller;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import engine.ScoreBoard;
import engine.ai.enums.AiType;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.enums.ObjectType;
import engine.gameTypes.GameType;
import javafx.scene.shape.Rectangle;

public class AiControllersManager {
	
	//AI controllers objects
	private ArrayList<AiController> controllers;
	//Objects in game
	private ArrayList<PhysicsObject> objects;
	//Map of game
	private Rectangle map;
	//Score board
	private ScoreBoard scoreboard;
	//Game type
	private GameType gameType;

	/**
	 * @param objects list of objects in game
	 * @param map map of game
	 * @param scoreboard score board object
	 * @param gameType game type object
	 */
	public AiControllersManager(ArrayList<PhysicsObject> objects, Rectangle map, ScoreBoard scoreboard, GameType gameType) {
		controllers = new ArrayList<>();
		this.objects = objects;
        this.map = map;
        this.gameType = gameType;
        this.scoreboard = scoreboard;
	}
	
	/**
	 * Adds an AI with type, level of difficulty of the parameter Type
	 * @param type The type of the AI to create
	 * @return The player object controller by AI
	 */
	public Player addAi(AiType type) {
		controllers.add(new AiController(new Player(ObjectType.ENEMY),objects, map, type, scoreboard, gameType));
		return controllers.get(controllers.size()-1).getAiPlayer();
	}
	
	/**
	 * Updates all AI once
	 *use this method if you have your own game loop to update all AI's 
	 */
	public void updateAllAi() {
		for(AiController con : controllers) 
			con.update();
	}
	
	/**
	 * Notifies AI that the game was paused, does not cause AI to actually be paused.
	 * To pause AI just stop calling updateAllAi method
	 * use this method when pausing your game to notify AI that the game is paused, this is necessary for AI time calculations
	 */
	public void pauseAllAi() {
		for(AiController con : controllers) 
			con.pause();
	}
	
	/**
	 * Starts a thread that updates all AI every second.
	 * use this method to let the AI controller manager have it is own loop
	 * it is not encouraged to use this method, use updateAllAi in your own loop instead.
	 * This method does not implement the pause function.
	 */
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
				            e.printStackTrace();
				        }
					}
				}
			}
		})).start();
	}
}
