package graphics.userInterface;


import client.ClientGameState;
import engine.entities.Player;
import graphics.userInterface.controllers.AbstractLobbyController;
import graphics.userInterface.controllers.LobbyHostController;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LobbyThread extends Thread {
    private ClientGameState gameState;
    private AbstractLobbyController controller;

    public LobbyThread(ClientGameState gameState, AbstractLobbyController controller) {
        this.gameState = gameState;
        this.controller = controller;
    }

    public void run() {
        // Keeps looping till game states get running is true
        // The whole loop gets all the player's id and inserts into the lobby screen
        while (!gameState.getRunning()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (controller instanceof LobbyHostController) {
                // If host, update lobby screen

                ArrayList<Player> players = gameState.getAllPlayers();
                ArrayList<Integer> playerIds = new ArrayList<>();

                synchronized (players) {
                    for (Player p : players) {
                        synchronized (p) {
                            playerIds.add(p.getId());

                        }
                    }
                }

                ((LobbyHostController) controller).showConnections(playerIds);
            }
        }

        controller.startGame();
    }
}