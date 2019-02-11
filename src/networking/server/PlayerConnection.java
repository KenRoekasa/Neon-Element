package networking.server;

import java.net.InetAddress;

public class PlayerConnection {
    private int id;
    private InetAddress ipAddress;
    private int port;

    public PlayerConnection(int id, InetAddress ipAddress, int port) {
        super();
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
    }
    
    public int getId() {
        return this.id;
    }

    public boolean is(InetAddress ipAddress, int port) {
        return this.ipAddress.equals(ipAddress) && this.port == port;
    }

}
