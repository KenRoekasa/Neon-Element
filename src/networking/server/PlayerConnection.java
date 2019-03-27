package networking.server;

import java.net.InetAddress;

import engine.entities.Player;

public class PlayerConnection {
    private Player player;
    private InetAddress ipAddress;
    private int port;

    /**
     * Create a record of the client connection.
     *
     * @param player The player object for this client.
     * @param ipAddress The remote IP address of the client.
     * @param port The port of the client.
     */
    public PlayerConnection(Player player, InetAddress ipAddress, int port) {
        super();
        this.player = player;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * Get the player for this client connection.
     *
     * @return The player of this client.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the unique identifier of the client's player.
     *
     * @return The player ID.
     */
    public int getId() {
        return this.player.getId();
    }

    /**
     * Predicate to check if the given IP address and port match this connection.
     *
     * @param ipAddress The IP address to compare.
     * @param port The port to compare.
     * @return True if IP address & port are equal to this connection.
     */
    public boolean is(InetAddress ipAddress, int port) {
        return this.ipAddress.equals(ipAddress) && this.port == port;
    }

    /**
     * Get the remote IP address of the client connection.
     *
     * @return The remote IP address of the client.
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * Get the remote port of this client connection.
     *
     * @return The remote port of this connection.
     */
    public int getPort() {
        return port;
    }

}
