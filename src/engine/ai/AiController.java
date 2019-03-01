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


    public  AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player) {


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
        System.out.println("started ai\n difficulty: " + String.valueOf(aiType) + "\n\n");
    }

    public AiController(Player aiPlayer, ArrayList<PhysicsObject> objects, Rectangle map, Player player, AiType aiType) {


        activeState = AiStates.IDLE;
        this.objects = objects;
        this.map = map;
        this.player = player;
        this.aiPlayer = aiPlayer;
        aiCon = this;
        calc = new AiCalculations(aiCon, map);
        actions = new AiActions(aiCon, calc, map);
        stateActions = new AiStateActions(aiCon, calc, actions);
        setAiType(aiType);
        //default random
        actions.assignRandomElement();
        System.out.println("started ai\n difficulty: " + String.valueOf(aiType) + "\n\n");
    }

    public void update() {
        switch (aiType) {
            case EASY:
                updateEasyAi();
                break;
            case NORMAL:
                updateNormalAi();
                break;
            case HARD:
                updateHardAi();
                break;
        }
    }

    public void updateEasyAi() {

        AiFSM.easyAiFetchAction(aiPlayer, aiCon, calc);
        easyAIExecuteAction();
    }

    public void updateNormalAi() {

        AiFSM.normalAiFetchAction(aiPlayer, aiCon, calc);
        normalAIExecuteAction();

    }

    public void updateHardAi() {

        AiFSM.hardAiFetchAction(aiPlayer, aiCon, calc);
        hardAIExecuteAction();

    }

    private void easyAIExecuteAction() {
        actions.changeToRandomElementAfter(15);
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
                stateActions.wander();
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
                stateActions.wander();
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
                stateActions.wander();
            case IDLE:
                break;
            default:
                break;
        }
    }

    public void setAiType(AiType type) {
        aiType = type;
    }

    public void setState(AiStates s) {
        activeState = s;
    }

    public void setWandering(boolean bool) {
        wandering = bool;
    }

    public boolean isWandering() {
        return wandering;
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
