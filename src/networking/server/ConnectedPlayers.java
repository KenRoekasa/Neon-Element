package networking.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.stream.Collectors;

import engine.entities.Player;
import javafx.geometry.Point2D;

public class ConnectedPlayers {

    private ArrayList<PlayerConnection> connections;
	private ArrayList<Integer> playerIds;
	private ArrayList<Point2D> playerLocations;

    ConnectedPlayers() {
        this.connections = new ArrayList<>();
		this.playerIds = new ArrayList<>();
		this.playerLocations = new ArrayList<>();
    }

    void addConnection(Player player, InetAddress ipAddress, int port) {
        PlayerConnection conn = new PlayerConnection(player, ipAddress, port);

        this.connections.add(conn);

        this.playerIds.add(player.getId());
    }

    ArrayList<PlayerConnection> getConnections() {
        return this.connections;
    }

	PlayerConnection getPlayerConnection(InetAddress ipAddress, int port) {
        return this.connections.stream()
            .filter(c -> c.is(ipAddress, port))
            .findFirst()
            .orElse(null);
	}

    public int count() {
        return this.connections.size();
    }

    ArrayList<Integer> getIds() {
        return this.connections.stream()
            .map(x -> x.getId())
            .collect(Collectors.toCollection(ArrayList::new));
    }

    ArrayList<Point2D> getLocations() {
        return this.connections.stream()
            .map(x -> x.getPlayer().getLocation())
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public void assignStartingLocations(double width, double height) {
        Point2D[] locations = {
            (new Point2D(width - width/10, height - height/10)),
            (new Point2D(0 +  width/10, height - height/10)),
            (new Point2D(0 + width/10, 0 + height/10)),
            (new Point2D(height - height/10, 0 + height/10)),
        };

        int i = 0;
        for (PlayerConnection conn : this.connections) {
            conn.getPlayer().setLocation(locations[i]);
            i++;
        }
    }

}