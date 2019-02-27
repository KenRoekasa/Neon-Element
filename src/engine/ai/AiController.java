package engine.ai;


import engine.calculations.AiCalculations;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.enums.AiStates;
import engine.enums.AiType;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

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
    AiStateActions stateActions;
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
        stateActions = new AiStateActions(aiCon, calc, actions);
        setAiType(AiType.EASY);
        //default random
        actions.assignRandomElement();
    }

    public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player, AiType aiType) {

        aiPlayer.canUp = aiPlayer.canDown = aiPlayer.canLeft = aiPlayer.canRight = aiPlayer.canUpCart = aiPlayer.canDownCart = aiPlayer.canLeftCart = aiPlayer.canRightCart = true;


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

        Thread t = new Thread(() -> {

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

                if (getActiveState().equals(AiStates.ESCAPE))
                    aiPlayer.delay((calc.DELAY_TIME / 3) * 2);
                else
                    aiPlayer.delay(calc.DELAY_TIME);

                easyAIExecuteAction();


            }

        });

        t.start();
    }

    public void startNormalAi() {
        //aiType = AiType.NORMAL;
        System.out.println("started ai\n difficulty: Normal\n\n");
        Thread t = new Thread(() -> {

            boolean bool = true;
            while (bool) {

                AiFSM.normalAiFetchAction(aiPlayer, aiCon, calc);

                if (getActiveState().equals(AiStates.ESCAPE))
                    aiPlayer.delay((calc.DELAY_TIME / 2) + (calc.DELAY_TIME / 4));
                else
                    aiPlayer.delay(calc.DELAY_TIME);

                normalAIExecuteAction();


            }

        });

        t.start();
    }

    public void startHardAi() {
        //aiType = AiType.HARD;
        System.out.println("started ai\n difficulty: Hard\n\n");
        Thread t = new Thread(() -> {
            boolean bool = true;
            while (bool) {

                AiFSM.hardAiFetchAction(aiPlayer, aiCon, calc);

                if (getActiveState().equals(AiStates.ESCAPE))
                    aiPlayer.delay((calc.DELAY_TIME / 2) + (calc.DELAY_TIME / 4));
                else
                    aiPlayer.delay(calc.DELAY_TIME);

                hardAIExecuteAction();


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
                stateActions.attack();
                break;
            case AGGRESSIVE_ATTACK:
                aiPlayer.unShield();
                stateActions.aggressiveAttack();
                break;
            case FIND_HEALTH:
                stateActions.findHealth();
                break;
            case FIND_DAMAGE:
                aiPlayer.shield();
                stateActions.findDamage();
                break;
            case FIND_SPEED:
                aiPlayer.shield();
                stateActions.findSpeed();
                break;
            case ESCAPE:
                aiPlayer.shield();
                stateActions.escape();
                break;
            case WANDER:
                aiPlayer.unShield();
                stateActions.startWandering();
            case IDLE:
                break;
            default:
                break;
        }
    }

    private void normalAIExecuteAction() {
        actions.changeToBefittingElement();
        if (!activeState.equals(AiStates.WANDER))
            wandering = false;
        switch (activeState) {
            case ATTACK:
                aiPlayer.unShield();
                stateActions.attack();
                break;
            case AGGRESSIVE_ATTACK:
                aiPlayer.unShield();
                stateActions.aggressiveAttack();
                break;
            case FIND_HEALTH:
                aiPlayer.shield();
                stateActions.findHealth();
                break;
            case FIND_DAMAGE:
                aiPlayer.shield();
                stateActions.findDamage();
                break;
            case FIND_SPEED:
                aiPlayer.shield();
                stateActions.findSpeed();
                break;
            case ESCAPE:
                aiPlayer.shield();
                stateActions.escape();
                break;
            case WANDER:
                aiPlayer.unShield();
                stateActions.startWandering();
            case IDLE:
                break;
            default:
                break;
        }
    }

    private void hardAIExecuteAction() {
        actions.changeToBefittingElement();
        if (!activeState.equals(AiStates.WANDER))
            wandering = false;
        switch (activeState) {
            case ATTACK:
                aiPlayer.unShield();
                stateActions.attack();
                break;
            case AGGRESSIVE_ATTACK:
                aiPlayer.unShield();
                stateActions.aggressiveAttack();
                break;
            case FIND_HEALTH:
                aiPlayer.shield();
                stateActions.findHealth();
                break;
            case FIND_DAMAGE:
                aiPlayer.shield();
                stateActions.findDamage();
                break;
            case FIND_SPEED:
                aiPlayer.shield();
                stateActions.findSpeed();
                break;
            case ESCAPE:
                aiPlayer.shield();
                stateActions.escape();
                break;
            case WANDER:
                aiPlayer.unShield();
                stateActions.startWandering();
            case IDLE:
                break;
            default:
                break;
        }
    }

    public void setState(AiStates s) {
        activeState = s;
    }

    public boolean isWandering() {
        return wandering;
    }

    public void setWandering(boolean bool) {
        wandering = bool;
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

    public void setAiType(AiType type) {
        aiType = type;
    }


}
