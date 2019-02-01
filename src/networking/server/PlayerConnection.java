package networking.server;

import java.net.InetAddress;

public class PlayerConnection {
    private InetAddress ipAddress;
    private int port;

    public PlayerConnection(InetAddress ipAddress, int port) {
        super();
        this.ipAddress = ipAddress;
        this.port = port;
    }

}
