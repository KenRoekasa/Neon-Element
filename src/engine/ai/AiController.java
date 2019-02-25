package engine.ai;

import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.enums.AiType;
import engine.enums.PowerUpType;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class AiController {


    AiStates activeState;
    ArrayList<PhysicsObject> objects;
    Player aiPlayer;
    Rectangle map;
    Player player;
    AiController aiCon;
    AiType aiType;
    AiCalculations calc;
    AiActions actions;

    boolean wandering = false;

    public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player) {

        aiPlayer.canUp = aiPlayer.canDown = aiPlayer.canLeft = aiPlayer.canRight = aiPlayer.canUpCart = aiPlayer.canDownCart = aiPlayer.canLeftCart = aiPlayer.canRightCart = true;


        activeState = AiStates.IDLE;
        this.objects = objects;
        this.map = map;
        this.player = player;
        this.aiPlayer = aiPlayer;
        aiCon = this;
        calc = new AiCalculations(aiCon, map);
        actions = new AiActions(aiCon, calc, map);
        //default random
        actions.assignRandomElement();
    }

    public void startEasyAi() {
        aiType = AiType.EASY;
        System.out.println("started easy ai\n\n");

        Thread t = new Thread(new Runnable() {


            @Override
            public void run() {

                double startTime = System.nanoTime() / 1000000000;
                double endTime;

                boolean bool = true;
                while (bool) {

                    //assigns random element every fifteen seconds
                    endTime = System.nanoTime() / 1000000000;
                    double elapsedTime = endTime - startTime;
                    if (elapsedTime >= 15) {
                        startTime = System.nanoTime() / 1000000000;
                        actions.assignRandomElement();
                    }

                    //System.out.println(aiPlayer.getLocation());
                    AiFSM.easyAiFetchAction(aiPlayer, aiCon, calc);


                    easyAIExecuteAction();


                }

            }

        });

        t.start();
    }

    public void startMediumAi() {
        aiType = AiType.MEDIUM;
        System.out.println("started medium ai\n\n");
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                boolean bool = true;
                while (bool) {

                    AiFSM.mediumAiFetchAction(aiPlayer, aiCon, calc);


                    mediumAIExecuteAction();


                }

            }

        });

        t.start();
    }

    public void startHardAi() {
        aiType = AiType.HARD;
        System.out.println("started hard ai\n\n");
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean bool = true;
                while (bool) {
                    AiFSM.hardAiFetchAction(aiPlayer, aiCon, calc);
                    //System.out.println("health "+aiPlayer.getHealth());


                    easyAIExecuteAction();
                    //delay to limit speed


//						if(timer.off)
//							bool = false;				
                }

            }

        });

        t.start();
    }

    private void easyAIExecuteAction() {
        if (!activeState.equals(AiStates.WANDER))
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

    private void mediumAIExecuteAction() {
        if (!activeState.equals(AiStates.WANDER))
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
        if (!wandering) {
            wandering = true;
            Random r = new Random();
            actions.wanderingDirection = r.nextInt(8);
        }
        actions.wander();
    }

    public void findSpeed() {
        int index = calc.findNearestPowerUp(PowerUpType.SPEED);
        if (index != -1)
            actions.moveTo(index, calc.getPowerups().get(index).getLocation());
    }

    public void findDamage() {
        int index = calc.findNearestPowerUp(PowerUpType.DAMAGE);
        if (index != -1)
            actions.moveTo(index, calc.getPowerups().get(index).getLocation());
    }

    public void findHealth() {
        int index = calc.findNearestPowerUp(PowerUpType.HEAL);
        if (index != -1)
            actions.moveTo(index, calc.getPowerups().get(index).getLocation());
    }

    public void aggressiveAttack() {
        Player player = calc.findNearestPlayer();
        actions.moveTo(player);
        if (calc.inAttackDistance(player) && player.getHealth() > 0) {
            aiPlayer.lightAttack();
            aiPlayer.chargeHeavyAttack();
        }
    }

    public void escape() {
        Player player = calc.findNearestPlayer();
        actions.moveAway(player);
    }

    public void attack() {
        Player player = calc.findNearestPlayer();
        actions.moveTo(player);

        if (calc.inAttackDistance(player) && player.getHealth() > 0) {
            aiPlayer.lightAttack();
        }
    }

    public void changeToBefittingElement() {
        actions.changeToBefittingElement();
    }

    public AiStates getActiveState() {
        return activeState;
    }

    public void setState(AiStates s) {
        activeState = s;
    }

    public Player getAiPlayer() {
        return aiPlayer;
    }

    public Player getPlayer() {
        return player;
    }


}
