package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import networking.packets.Packet;

public abstract class AbstractNetworkDispatcher {

    protected DatagramSocket socket;
    /*
    protected MulticastSocket multicastSocket;
    protected InetAddress groupAddress;
    */

    protected AbstractNetworkDispatcher(DatagramSocket socket/*, MulticastSocket multicastSocket, InetAddress groupAddress*/) {
        this.socket = socket;
        /*
        this.multicastSocket = multicastSocket;
        this.groupAddress = groupAddress;
        */
    }

    /**
     * Close the socket used by the dispatcher.
     */
    public void close() {
        this.socket.close();
        // this.multicastSocket.close();
    }

    /**
     * Send a Packet to the specified IP address and port.
     *
     * @param packet The packet to send.
     * @param ipAddress The destination IP address.
     * @param port The destination port.
     */
    public void send(Packet packet, InetAddress ipAddress, int port) {
        if (packet.getDirection() == Packet.PacketDirection.OUTGOING) {
            byte[] data = packet.getRawBytes();

            DatagramPacket datagram = new DatagramPacket(data, data.length, ipAddress, port);

            if (!packet.getPacketType().equals(Packet.PacketType.LOCATION_STATE)) {
                System.out.println("Sent " + packet.getPacketType() + " to " + ipAddress + ":" + port);
            }

            try {
                this.socket.send(datagram);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Attempted to send a received packet.");
        }
    }
}
