package networking.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.stream.Collectors;

import engine.entities.Player;
import javafx.geometry.Point2D;

public class ConnectedPlayers {

    private ArrayList<PlayerConnection> connections;
    private ArrayList<Integer> playerIds;

    public ArrayList<Integer> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(ArrayList<Integer> playerIds) {
		this.playerIds = playerIds;
	}

	ConnectedPlayers() {
        this.connections = new ArrayList<>();
    }

    /**
     * Add a new connection to the store.
     *
     * @param player The client's player.
     * @param ipAddress The client's remote IP address.
     * @param port The client's remote port.
     */
    void addConnection(Player player, InetAddress ipAddress, int port) {
        PlayerConnection conn = new PlayerConnection(player, ipAddress, port);

        this.connections.add(conn);
        this.playerIds.add(player.getId());
    }

    /**
     * Get all the connections in the store.
     *
     * @return The connected players.
     */
    ArrayList<PlayerConnection> getConnections() {
        return this.connections;
    }

    /**
     * Get the player connection for the client with the specified remote IP address and port.
     *
     * @param ipAddress The client's remote IP address.
     * @param port The client's remote port.
     * @return The {@link PlayerConnection} or <code>null</code> if not found.
     */
	PlayerConnection getPlayerConnection(InetAddress ipAddress, int port) {
        return this.connections.stream()
            .filter(c -> c.is(ipAddress, port))
            .findFirst()
            .orElse(null);
	}

    /**
     * The number of players connected.
     *
     * @return The number of connected players.
     */
    public int count() {
        return this.connections.size();
    }

    /**
     * Get an ArrayList of the connected players' IDs.
     *
     * @return ArrayList of the connected players' IDs.
     */
    ArrayList<Integer> getIds() {
        return this.connections.stream()
            .map(x -> x.getId())
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get an ArrayList of the connected players' locations.
     *
     * @return ArrayList of the connected players' locations.
     */
    ArrayList<Point2D> getLocations() {
        return this.connections.stream()
            .map(x -> x.getPlayer().getLocation())
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Assign the starting locations for the connected players.
     *
     * @param width The width of the map.
     * @param height The height of the map.
     */
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