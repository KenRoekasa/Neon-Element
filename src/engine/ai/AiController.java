package engine.ai;

import java.util.ArrayList;
import java.util.Random;

import engine.calculations.AiCalculations;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.enums.AiStates;
import engine.enums.AiType;
import engine.enums.ObjectType;
import engine.enums.PowerUpType;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import javafx.geometry.Point2D;
public class AiController {


		AiStates activeState;
		ArrayList<PhysicsObject> objects;
		Player aiPlayer ;
		Rectangle map;
		Player player;
		AiController aiCon;
		AiType aiType;
		AiCalculations calc;
		AiActions actions;
		boolean wandering = false;
		
		public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player) {

			aiPlayer.canUp=  aiPlayer.canDown= aiPlayer.canLeft= aiPlayer.canRight= aiPlayer.canUpCart= aiPlayer.canDownCart= aiPlayer.canLeftCart= aiPlayer.canRightCart= true;
	    	

	        activeState = AiStates.IDLE;
	        this.objects = objects;
	        this.map = map;
	        this.player = player;
	        this.aiPlayer = aiPlayer;
	        aiCon = this;
	        calc = new AiCalculations(aiCon, map);
	        actions = new AiActions(aiCon, calc, map);
	        setAiType(AiType.EASY);
	        //default random
	        actions.assignRandomElement();
	    }
		
		public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player, AiType aiType) {

			aiPlayer.canUp=  aiPlayer.canDown= aiPlayer.canLeft= aiPlayer.canRight= aiPlayer.canUpCart= aiPlayer.canDownCart= aiPlayer.canLeftCart= aiPlayer.canRightCart= true;
	    	

	        activeState = AiStates.IDLE;
	        this.objects = objects;
	        this.map = map;
	        this.player = player;
	        this.aiPlayer = aiPlayer;
	        this.aiType = aiType;
	        aiCon = this;
	        calc = new AiCalculations(aiCon, map);
	        actions = new AiActions(aiCon, calc, map);
	        //default random
	        actions.assignRandomElement();
	    }
		
		public void startEasyAi() {
			//aiType = AiType.EASY;
			System.out.println("started ai\n difficulty: Easy\n\n");
			
			Thread t = new Thread(new Runnable() {
				
			
				@Override
				public void run() {
					
					double startTime = System.nanoTime()/1000000000;
					double endTime ;
					
					boolean bool = true;
					while (bool) {
						
						//assigns random element every fifteen seconds
						endTime = System.nanoTime()/1000000000;
						double elapsedTime = endTime - startTime;
						if(elapsedTime >= 15) {
							startTime = System.nanoTime()/1000000000;
							actions.assignRandomElement();
						}
						
						//System.out.println(aiPlayer.getLocation());
						AiFSM.easyAiFetchAction(aiPlayer, aiCon, calc);
						
						if(getActiveState().equals(AiStates.ESCAPE))
							aiPlayer.delay((calc.DELAY_TIME/3)*2);
						else
							aiPlayer.delay(calc.DELAY_TIME);
						
						easyAIExecuteAction();

						if (aiPlayer.getHealth() <= 0) {
							aiPlayer.respawn(map.getWidth(),map.getHeight());
						}
						
					}
				
				}

			});

			t.start();
		}

		public void startNormalAi() {
			//aiType = AiType.NORMAL;
			System.out.println("started ai\n difficulty: Normal\n\n");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					
					boolean bool = true;
					while (bool) {
						
						AiFSM.normalAiFetchAction(aiPlayer, aiCon, calc);
						
						if(getActiveState().equals(AiStates.ESCAPE))
							aiPlayer.delay((calc.DELAY_TIME/2)+(calc.DELAY_TIME/4));
						else
							aiPlayer.delay(calc.DELAY_TIME);
						
						normalAIExecuteAction();
						
						if (aiPlayer.getHealth() <= 0) {
							aiPlayer.respawn(map.getWidth(),map.getHeight());
						}
						
					}
					
				}

			});

			t.start();
		}
		
		public void startHardAi() {
			//aiType = AiType.HARD;
			System.out.println("started ai\n difficulty: Hard\n\n");
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					boolean bool = true;
					while (bool) {
						
						AiFSM.hardAiFetchAction(aiPlayer, aiCon, calc);
						
						if(getActiveState().equals(AiStates.ESCAPE))
							aiPlayer.delay((calc.DELAY_TIME/2)+(calc.DELAY_TIME/4));
						else
							aiPlayer.delay(calc.DELAY_TIME);
						
						hardAIExecuteAction();
						
						if (aiPlayer.getHealth() <= 0) {
							aiPlayer.respawn(map.getWidth(),map.getHeight());
						}
								
					}
				
				}

			});

			t.start();
		}
		
		private void easyAIExecuteAction() {
			if(!activeState.equals(AiStates.WANDER))
				wandering = false;
			switch (activeState) {
			case ATTACK:
				aiPlayer.unShield();
				attack();
				break;
			case AGGRESSIVE_ATTACK:
				aiPlayer.unShield();
				aggressiveAttack();
				break;
			case FIND_HEALTH:
				findHealth();
				break;
			case FIND_DAMAGE:
				aiPlayer.shield();
				findDamage();
				break;
			case FIND_SPEED:
				aiPlayer.shield();
				findSpeed();
				break;
			case ESCAPE:
				aiPlayer.shield();
				escape();
				break;
			case WANDER:
				aiPlayer.unShield();
				startWandering();
			case IDLE:
				break;
			default:
				break;
			}
		}
		
		private void normalAIExecuteAction() {
			actions.changeToBefittingElement();
			if(!activeState.equals(AiStates.WANDER))
				wandering = false;
			switch (activeState) {
			case ATTACK:
				aiPlayer.unShield();
				attack();
				break;
			case AGGRESSIVE_ATTACK:
				aiPlayer.unShield();
				aggressiveAttack();
				break;
			case FIND_HEALTH:
				aiPlayer.shield();
				findHealth();
				break;
			case FIND_DAMAGE:
				aiPlayer.shield();
				findDamage();
				break;
			case FIND_SPEED:
				aiPlayer.shield();
				findSpeed();
				break;
			case ESCAPE:
				aiPlayer.shield();
				escape();
				break;
			case WANDER:
				aiPlayer.unShield();
				startWandering();
			case IDLE:
				break;
			default:
				break;
			}
		}
		
		private void hardAIExecuteAction() {
			actions.changeToBefittingElement();
			if(!activeState.equals(AiStates.WANDER))
				wandering = false;
			switch (activeState) {
			case ATTACK:
				aiPlayer.unShield();
				attack();
				break;
			case AGGRESSIVE_ATTACK:
				aiPlayer.unShield();
				aggressiveAttack();
				break;
			case FIND_HEALTH:
				aiPlayer.shield();
				findHealth();
				break;
			case FIND_DAMAGE:
				aiPlayer.shield();
				findDamage();
				break;
			case FIND_SPEED:
				aiPlayer.shield();
				findSpeed();
				break;
			case ESCAPE:
				aiPlayer.shield();
				escape();
				break;
			case WANDER:
				aiPlayer.unShield();
				startWandering();
			case IDLE:
				break;
			default:
				break;
			}
		}

		private void startWandering() {
			if(!wandering) {
				wandering = true;
				Random r = new Random();
				actions.wanderingDirection = r.nextInt(8);
			}
			actions.wander();
		}
		
		private void findSpeed() {
			int index = calc.getNearestPowerUp(PowerUpType.SPEED);
			if (index != -1)
				actions.moveTo(index, calc.getPowerups().get(index).getLocation());
		}

		private void findDamage() {
			int index = calc.getNearestPowerUp(PowerUpType.DAMAGE);
			if (index != -1)
				actions.moveTo(index, calc.getPowerups().get(index).getLocation());
		}

		private void findHealth() {
			int index = calc.getNearestPowerUp(PowerUpType.HEAL);
			if (index != -1)
				actions.moveTo(index, calc.getPowerups().get(index).getLocation());
		}

		private void aggressiveAttack() {
			Player player = calc.getNearestPlayer();
			actions.moveTo(player);
			if (calc.inAttackDistance(player) && player.getHealth()>0) {
				aiPlayer.lightAttack();
				aiPlayer.chargeHeavyAttack();
			}
		}
		
		private void escape() {
			Player player = calc.getNearestPlayer();
			actions.moveAway(player);
		}	
		
		private void attack() {
			Player player = calc.getNearestPlayer();
			actions.moveTo(player);

			if (calc.inAttackDistance(player) && player.getHealth()>0) {
				aiPlayer.lightAttack();
			}
		}
		
		public void setAiType(AiType type) {
			aiType = type;
		}
		
		public void setState(AiStates s) {
			activeState = s;
		}
		
		public ArrayList<PhysicsObject> getObjects() {
			return objects;
		}
		
		public AiStates getActiveState() {
			return activeState;
		}
		
		public Player getAiPlayer() {
			return aiPlayer;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		public AiType getAiType() {
			return aiType;
		}
		
		

}
