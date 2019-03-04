package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import networking.packets.Packet;

public abstract class NetworkDispatcher {

    protected DatagramSocket socket;
    /*
    protected MulticastSocket multicastSocket;
    protected InetAddress groupAddress;
    */

    protected NetworkDispatcher(DatagramSocket socket/*, MulticastSocket multicastSocket, InetAddress groupAddress*/) {
        this.socket = socket;
        /*
        this.multicastSocket = multicastSocket;
        this.groupAddress = groupAddress;
        */
    }

    public void close() {
        this.socket.close();
        // this.multicastSocket.close();
    }

    protected void send(Packet packet, InetAddress ipAddress, int port) {
        if (packet.getDirection() == Packet.PacketDirection.OUTGOING) {
            byte[] data = packet.getRawBytes();

            DatagramPacket datagram = new DatagramPacket(data, data.length, ipAddress, port);

            if (!packet.getType().equals(Packet.PacketType.LOCATION_STATE)) {
                System.out.println("Sent " + packet.getType() + " to " + ipAddress + ":" + port);
            }

            try {
                this.socket.send(datagram);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Attempted to send a recived packet.");
        }
    }
}
