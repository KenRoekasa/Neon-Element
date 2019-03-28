package engine.physics;

import client.ClientGameState;
import client.GameClient;
import engine.calculations.DamageCalculation;
import engine.controller.GameTypeHandler;
import engine.entities.PhysicsObject;
import engine.entities.Player;
import engine.entities.PowerUp;
import engine.model.GameState;
import engine.model.GameType;
import engine.model.ScoreBoard;
import engine.model.enums.Action;
import engine.model.enums.ObjectType;
import engine.model.gametypes.HillGame;
import engine.model.gametypes.Regicide;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import networking.server.ServerNetworkDispatcher;
import server.ServerGameState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The physics engine and more
 */
public class PhysicsController {
    private ServerNetworkDispatcher dispatcher;
    private GameState gameState;
    private long lastTimeRegi;

    /**
     * Constructor
     *
     * @param gameState the game state of the current game
     */
    public PhysicsController(GameState gameState) {
        this.gameState = gameState;
    }


    /**
     * Constructor
     *
     * @param gameState the game state of the current game
     */
    public PhysicsController(GameState gameState, ServerNetworkDispatcher dispatcher) {
        this.gameState = gameState;
        this.dispatcher = dispatcher;
        this.lastTimeRegi = GameClient.timeElapsed;
    }
    public void serverLoop(){
        doCollisionDetection();
        new Thread(() -> doHitDetection()).start();
        doUpdates();
        serverdeathHandler();
        gameState.getAiConMan().updateAllAi();

        if (gameState.getGameType().getType().equals(GameType.Type.Hill)) {
            kingOfHillHandler();
        }
        if(gameState.getGameType().getType() == GameType.Type.Regicide){
            regicideHandler();
        }
        if (!GameTypeHandler.checkRunning(gameState)) {
            gameState.stop();
        }
    }


    /**
     * Call this method every game tick as it controls the game
     */
    public void clientLoop() {
        doCollisionDetection();
        new Thread(() -> doHitDetection()).start();
        doUpdates();
        deathHandler();
        gameState.getAiConMan().updateAllAi();

        if (gameState.getGameType().getType().equals(GameType.Type.Hill)) {
            kingOfHillHandler();
        }
        if(gameState.getGameType().getType() == GameType.Type.Regicide){

            regicideHandler();
        }
        if (!GameTypeHandler.checkRunning(gameState)) {
            gameState.stop();
        }
    }
    /**
     * Call this method every game tick as it controls the game
     */
    public void dumbClientLoop() {
        doCollisionDetection();
        doUpdates();
    }


    /**
     * Run the update method for all objects
     */
    private void doUpdates() {
        synchronized (gameState.getObjects()) {
            // Call update function for all physics objects

//            gameState.getPlayer().update();

            for (PhysicsObject o : gameState.getObjects()) {
                o.update();
            }
        }
    }

    /**
     * Handles the points system in the game mode king of the hill
     */
    private void kingOfHillHandler() {
        HillGame hillGame = (HillGame) gameState.getGameType();
        Circle hill = hillGame.getHill();
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        ArrayList<Player> playersInside = new ArrayList<>();
        ScoreBoard scoreBoard = gameState.getScoreBoard();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            if (CollisionDetector.checkCollision(hill, player.getBounds())) {
                playersInside.add(player);
            }
        }
        // if only one player is inside the circle/hill count score
        if (playersInside.size() == 1) {
            Player onlyPlayer = playersInside.get(0);
            int onlyPlayerId = onlyPlayer.getId();
            scoreBoard.addScore(onlyPlayerId, (int) (1 * DeltaTime.deltaTime));
        }
    }

    /**
     * Handles the points system in the game mode king of the hill
     */
    private void regicideHandler() {
        Regicide regicideGame = (Regicide) gameState.getGameType();
        Player king = regicideGame.getKing();
        if(GameClient.timeElapsed - lastTimeRegi >= 10000){
            gameState.getScoreBoard().addScore(king.getId(),20);
            lastTimeRegi = GameClient.timeElapsed;
        }
    }


    /**
     * Handles the deaths of a player
     */
    private void deathHandler() {
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        LinkedBlockingQueue deadPlayers = gameState.getDeadPlayers();
        ScoreBoard scoreBoard = gameState.getScoreBoard();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            //If not already dead
            if (!deadPlayers.contains(player) && !player.isAlive()) {
                // Add to dead list

                deadPlayers.offer(player);
                // For any game mode add kills to scoreboard
                scoreBoard.addKill(player.getLastAttacker().getId(), player.getId());
                // TODO broadcast kills

                if (gameState.getGameType().getType() == GameType.Type.FirstToXKills) {
                    scoreBoard.addScore(player.getLastAttacker().getId(), 1);

                } else if (gameState.getGameType().getType() == GameType.Type.Regicide) {
                    Regicide regicide = (Regicide) gameState.getGameType();
                    int baseScore = 5;
                    // if the player dead is the king the killer gets more points
                    if (regicide.getKing().getId() == player.getId()) {
                        scoreBoard.addScore(player.getLastAttacker().getId(), baseScore * 2);

                        // Make the attacker the king now
                        regicide.setKing(player.getLastAttacker());

                        //TODO BROADCAST THE KING
                    } else {
                        scoreBoard.addScore(player.getLastAttacker().getId(), baseScore);

                    }
                }
                //if dead teleport player off screen
                player.setLocation(new Point2D(5000, 5000));
            }

        }
    }
        private void serverdeathHandler() {
            ArrayList<Player> allPlayers = gameState.getAllPlayers();
            LinkedBlockingQueue deadPlayers = gameState.getDeadPlayers();
            ScoreBoard scoreBoard = gameState.getScoreBoard();
            for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
                Player player = itr.next();
                //If not already dead
                if (!deadPlayers.contains(player) && !player.isAlive()) {
                    // Add to dead list

                    deadPlayers.offer(player);
                    // For any game mode add kills to scoreboard
                    scoreBoard.addKill(player.getLastAttacker().getId(), player.getId());
                    this.dispatcher.broadcastScore(player.getLastAttacker().getId(),0,1,player.getId());

                    // TODO broadcast kills

                    if (gameState.getGameType().getType() == GameType.Type.FirstToXKills) {

                        scoreBoard.addScore(player.getLastAttacker().getId(), 1);
                        this.dispatcher.broadcastScore(player.getLastAttacker().getId(),1,0,-1);
                    } else if (gameState.getGameType().getType() == GameType.Type.Regicide) {
                        Regicide regicide = (Regicide) gameState.getGameType();
                        int baseScore = 5;
                        // if the player dead is the king the killer gets more points
                        if (regicide.getKingId() == player.getId()) {
                            scoreBoard.addScore(player.getLastAttacker().getId(), baseScore * 2);
                            this.dispatcher.broadcastScore(player.getLastAttacker().getId(),baseScore * 2,0,-1);
                            // Make the attacker the king now
                            regicide.setKing(player.getLastAttacker());

                            //TODO BROADCAST THE KING
                        } else {
                            scoreBoard.addScore(player.getLastAttacker().getId(), baseScore);
                            this.dispatcher.broadcastScore(player.getLastAttacker().getId(),baseScore,0,-1);
                        }
                    }
                    //if dead teleport player off screen
                    player.setLocation(new Point2D(5000, 5000));
                }

            }


    }

    /**
     * The detection of collision between a player and other objects
     */
    private void doCollisionDetection() {
        ArrayList<PhysicsObject> objects = gameState.getObjects();
        ArrayList<Player> allPlayers = gameState.getAllPlayers();

        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            synchronized (objects) {
                // Collision detection code
                Player projectedPlayer = new Player(ObjectType.PLAYER);

                ArrayList<PhysicsObject> otherObjects = gameState.getOtherObjects(player);
                for (Iterator<PhysicsObject> itr1 = otherObjects.iterator(); itr1.hasNext(); ) {
                    PhysicsObject e = itr1.next();
                    // Check if the moving in a certain direction will cause a collision
                    // The player has collided with e do something
                    if (e.getTag() == ObjectType.POWERUP) {
                        if (CollisionDetector.checkCollision(player, e)) {
                            PowerUp powerUp = (PowerUp) e;
                            powerUp.activatePowerUp(player);
                            // remove power up from objects array list
                            itr1.remove();
                            objects.remove(e);
                        }
                    } else {
                        Point2D checkNext = player.getLocation().add(player.getHorizontalMove() * DeltaTime.deltaTime, player.getVerticalMove() * DeltaTime.deltaTime);
                        projectedPlayer.setLocation(checkNext);
                        if (CollisionDetector.checkCollision(projectedPlayer, e)) {
                            player.setVerticalMove(0);
                            player.setHorizontalMove(0);
                        }
                    }
                }
            }
        }
    }


    /**
     * To detect hits when a player attacks another player
     */
    private void doHitDetection() {
        ArrayList<Player> allPlayers = gameState.getAllPlayers();
        for (Iterator<Player> itr = allPlayers.iterator(); itr.hasNext(); ) {
            Player player = itr.next();
            ArrayList<Player> otherPlayers = gameState.getOtherPlayers(player);
            ArrayList<Player> lightHittablePlayers = new ArrayList<>();
            ArrayList<Player> heavyHittablePlayer = new ArrayList<>();
            // Loop through all enemies to detect hit detection
            for (Iterator<Player> itr1 = otherPlayers.iterator(); itr1.hasNext(); ) {
                Player e = itr1.next();
                // Check light attack
                if (CollisionDetector.checkCollision(player.getAttackHitbox(), e.getBounds())) {
                    lightHittablePlayers.add(e);
                }
                //Check heavy attack
                if (CollisionDetector.checkCollision(player.getHeavyAttackHitbox(),
                        e.getBounds())) {
                    heavyHittablePlayer.add(e);
                }
            }
            // Attack Collision
            // if player is light attacking
            if (player.getCurrentAction() == Action.LIGHT) {
                for (Player e : lightHittablePlayers) {
                    if (e.getLastAttacker() != null) {
                        // If the player isn't invulnerable and attack by the same person
                        if (e.getIframes() <= 0 || e.getLastAttacker().getId() != player.getId()) {
                            float damage = DamageCalculation.calculateDealtDamage(player, e);
                            e.takeDamage(damage, player);
                        }
                    } else {
                        float damage = DamageCalculation.calculateDealtDamage(player, e);
                        e.takeDamage(damage, player);
                    }
                }
            }

            // if player is heavy attacking
            if (player.getCurrentAction() == Action.HEAVY) {
                for (Player e : heavyHittablePlayer) {
                    if (e.getLastAttacker() != null) {
                        // If the player isn't invulnerable and attack by the same person
                        if (e.getIframes() <= 0 || e.getLastAttacker().getId() != player.getId()) {
                            float damage = DamageCalculation.calculateDealtDamage(player, e);
                            e.takeDamage(damage, player);
                        }
                    } else {
                        float damage = DamageCalculation.calculateDealtDamage(player, e);
                        e.takeDamage(damage, player);
                    }
                }
            }
        }
    }
}