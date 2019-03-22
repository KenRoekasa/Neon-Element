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
        while (!gameState.getRunning()) {
            if (controller instanceof LobbyHostController) {
                // If host, update lobby screen
                ArrayList<Player> players = gameState.getAllPlayers();
                ArrayList<Integer> playerIds;

                synchronized (players) {
                    playerIds = players.stream()
                            .map(x -> x.getId())
                            .collect(Collectors.toCollection(ArrayList::new));
                }

                ((LobbyHostController) controller).showConnections(playerIds);
            }
        }

        controller.startGame();
    }
}