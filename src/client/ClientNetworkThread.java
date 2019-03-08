package client;

import java.net.InetAddress;

import engine.enums.Action;
import engine.enums.Elements;
import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;
import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

public class ClientNetworkThread extends Thread {

    private ClientGameState gameState;
    private ClientNetwork network;
    private long lastActionSendTime;
    private Elements playerElement;

    private boolean running = true;

    public ClientNetworkThread(ClientGameState gameState, InetAddress serverAddr) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState, serverAddr);
        lastActionSendTime = 0;
        playerElement = gameState.getPlayer().getCurrentElement();
    }

    public void run() {
        this.getDispatcher().sendConnect();
        
        // Wait for game to start
        while (!this.gameState.getRunning()) {
            try {
                Thread.sleep(1000L); // Every 1 second
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Now we have been sent game started

        while (this.running) {
            this.doLocationState();

            if((gameState.getPlayer().getCurrentAction() != Action.IDLE && gameState.getPlayer().getCurrentAction() != Action.HEAVY) && gameState.getPlayer().getCurrentActionStart() > lastActionSendTime) {
                lastActionSendTime = gameState.getPlayer().getCurrentActionStart();
                //todo sent action

                this.doActionState();
            }

            if(gameState.getPlayer().getCurrentElement() != playerElement) {
                playerElement = gameState.getPlayer().getCurrentElement();
                this.doElementState();
            }

            try {
                Thread.sleep(25); // Every 1 second
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("GAME ENDED");

        this.network.close();
    }

    private void doElementState() {
        Elements element = this.gameState.getPlayer().getCurrentElement();
        this.getDispatcher().sendElementState(element);
    }

    private void doActionState() {
        Action playerAction = this.gameState.getPlayer().getCurrentAction();
        this.getDispatcher().sendActionState(playerAction);
    }

    private void doLocationState() {
        Point2D location = this.gameState.getPlayer().getLocation();
        double x = location.getX();
        double y = location.getY();
        Rotate playerAngle = this.gameState.getPlayer().getPlayerAngle();
        float playerHealth = this.gameState.getPlayer().getHealth();
        

        this.getDispatcher().sendLocationState(x, y, playerAngle.getAngle(),playerHealth);
    }

    private ClientNetworkDispatcher getDispatcher() {
        return this.network.getDispatcher();
    }


}
