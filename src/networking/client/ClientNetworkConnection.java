package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import networking.packets.Packet;

public class ClientNetworkConnection extends Thread {
    
    private ClientNetwork net;
    /** True if the ClientNetwork is running and receiving Packets. */
    private boolean running;

    protected DatagramSocket socket;

    /**
     * Create a thread to handle the network connection.
     *
     * @param net The ClientNetwork to use.
     */
    public ClientNetworkConnection(ClientNetwork net) {
        super();
        this.net = net;

        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    protected DatagramSocket getSocket() {
        return this.socket;
    }

    public void close() {
        this.running = false;
        this.socket.close();
    }

    public void run() {
        this.running = true;
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
