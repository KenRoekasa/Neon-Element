package client;

import java.net.InetAddress;

import engine.enums.Action;
import javafx.geometry.Point2D;
import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

public class ClientNetworkThread extends Thread {

    private ClientGameState gameState;
    private ClientNetwork network;
    private long lastActionSendTime;

    private boolean running = true;

    public ClientNetworkThread(ClientGameState gameState, InetAddress serverAddr) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState, serverAddr);
        lastActionSendTime = 0;
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



            if(gameState.getPlayer().getCurrentAction() != Action.IDLE && gameState.getPlayer().getCurrentActionStart() > lastActionSendTime) {
                lastActionSendTime = gameState.getPlayer().getCurrentActionStart();
                //todo sent action

                this.doActionState();

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

    private void doActionState() {
        Action playerAction = this.gameState.getPlayer().getCurrentAction();
        this.getDispatcher().sendActionState(playerAction);
    }

    private void doLocationState() {
        Point2D location = this.gameState.getPlayer().getLocation();
        double x = location.getX();
        double y = location.getY();

        this.getDispatcher().sendLocationState(x, y);
    }

    private ClientNetworkDispatcher getDispatcher() {
        return this.network.getDispatcher();
    }


}
