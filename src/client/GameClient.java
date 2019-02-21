package client;

import engine.GameTypeHandler;
import javafx.geometry.Point2D;
import networking.client.ClientNetwork;
import networking.client.ClientNetworkDispatcher;

public class GameClient extends Thread {

    private ClientGameState gameState;
    private ClientNetwork network;

    private boolean running = true;

    public GameClient(ClientGameState gameState) {
        this.gameState = gameState;
        this.network = new ClientNetwork(this.gameState);
    }

    public void run() {
            System.out.println("hello");
        while (this.running) {
            this.running = GameTypeHandler.checkRunning(gameState);
            this.doLocationState();

            try {
                Thread.sleep(1000l); // Every 1 second
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("GAME ENDED");

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
