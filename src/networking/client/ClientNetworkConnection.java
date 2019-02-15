package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import networking.packets.Packet;

public class ClientNetworkConnection extends Thread {

    private ClientNetwork net;
    private boolean running;

    protected DatagramSocket socket;

    public ClientNetworkConnection(ClientNetwork net) {
        super();
        this.net = net;

        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.running = true;
    }
    
    protected DatagramSocket getSocket() {
        return this.socket;
    }
    
    public void close() {
        this.running = false;
        this.socket.close();
    }

    public void run() {
        while (this.running) {
            byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                this.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.net.parse(packet);
        }
    }

}
