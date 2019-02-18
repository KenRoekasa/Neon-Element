package client;

import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

import client.ClientGameState;
import javafx.geometry.Point2D;

public class GameClient extends Thread {

    private ClientGameState gameState;
    private ClientNetwork network;

    private boolean running;

    public GameClient(ClientGameState gameState) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState);
    }

    public void run() {
        this.running = true;

        while(this.running) {
            this.doLocationState();

            try {
                Thread.sleep(1000l); // Every 1 second
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        this.network.close();
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
