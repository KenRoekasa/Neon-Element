package ai;

import java.lang.Thread;

import Enums.Elements;

import java.util.Random;

import Entities.Character;

public class BasicEnemy extends Character implements Runnable {

    BasicEnemyStates activeState;
    int hp;
    Position pos;
    Character player;

    public BasicEnemy(Character player) {

        activeState = BasicEnemyStates.IDLE;
        assignRandomElement();
        hp = TestGame.maxHP;
        pos = new Position(0, 0);
        this.player = player;

    }

    public void run() {

        boolean bool = true;

        while (bool) {

            BasicEnemyFSM.nextAction(this, player);

            if (hp <= 0)
                bool = false;
        }

    }

    void assignRandomElement() {

        Random r = new Random();
        int rand = r.nextInt(4);
        switch (rand) {
            case 0:
                currentElement = Elements.EARTH;
                break;
            case 1:
                currentElement = Elements.FIRE;
                break;
            case 2:
                currentElement = Elements.WATER;
                break;
            case 3:
                currentElement = Elements.AIR;
                break;
        }
    }

    BasicEnemyStates getActiveState() {
        return activeState;
    }

    void setElement(Elements e) {
        currentElement = e;
    }

    void setState(BasicEnemyStates s) {
        activeState = s;
    }


    @Override
    public void update() {

    }
    //basic enemy actions
}
