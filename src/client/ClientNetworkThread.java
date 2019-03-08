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

    private boolean running = true;

    public ClientNetworkThread(ClientGameState gameState, InetAddress serverAddr) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState, serverAddr);
    }

    public void run() {
        // Send hello
        this.getDispatcher().sendHello();

        // Wait for gameType to be recieved from server
        while (this.gameState.getGameType() == null) {
            try {
                Thread.sleep(1000L); // Every 1 second
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Send connect
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
        Elements playerElement = gameState.getPlayer().getCurrentElement();
        long lastActionSendTime = 0;
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



        this.getDispatcher().sendLocationState(x, y, playerAngle.getAngle());
    }

    private ClientNetworkDispatcher getDispatcher() {
        return this.network.getDispatcher();
    }


}
