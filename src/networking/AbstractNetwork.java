package networking;

import networking.packets.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public abstract class AbstractNetwork extends Thread {
    /** True if the network is running and receiving Packets */
    private boolean running;
    private DatagramSocket socket;

    protected AbstractNetwork() {
        try {
            this.socket = new DatagramSocket();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected AbstractNetwork(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Get the socket for the network.
     *
     * @return The socket.
     */
    protected DatagramSocket getSocket() {
        return this.socket;
    }

    /**
     * Stop the Network.
     *
     * Implementors must call <code>super.close()</code>.
     */
    public void close() {
        this.running = false;
    }

    /**
     * Start the ServerNetwork and begin receiving packets.
     */
    @Override
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

            this.parse(packet);
        }
    }

    /**
     * Parse the incoming DatagramPacket into a {@link Packet} and handle it.
     *
     * @param datagram The incoming packet.
     */
    protected abstract void parse(DatagramPacket datagram);
}
