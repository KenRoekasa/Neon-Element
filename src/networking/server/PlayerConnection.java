package networking.server;

import java.net.InetAddress;

import engine.entities.Player;

public class PlayerConnection {
    private Player player;
    private InetAddress ipAddress;
    private int port;

    public PlayerConnection(Player player, InetAddress ipAddress, int port) {
        super();
        this.player = player;
        this.ipAddress = ipAddress;
        this.port = port;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public int getId() {
        return this.player.getId();
    }

    public boolean is(InetAddress ipAddress, int port) {
        return this.ipAddress.equals(ipAddress) && this.port == port;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

}
