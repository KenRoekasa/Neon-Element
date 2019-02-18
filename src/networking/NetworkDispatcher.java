package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import networking.packets.Packet;

public abstract class NetworkDispatcher {

    private DatagramSocket socket;
    protected MulticastSocket multicastSocket;
    protected InetAddress groupAddress;

    protected NetworkDispatcher(DatagramSocket socket, MulticastSocket multicastSocket, InetAddress groupAddress) {
        this.socket = socket;
        this.multicastSocket = multicastSocket;
        this.groupAddress = groupAddress;
    }

    public void close() {
        this.socket.close();
        this.multicastSocket.close();
    }

    protected void send(Packet packet) {
        if (packet.getDirection() == Packet.PacketDirection.OUTGOING) {
            byte[] data = packet.getRawBytes();
            //System.out.println("" + packet.getIpAddress() + ":" + packet.getPort() + " <-- " + packet.getType());
            DatagramPacket datagram = new DatagramPacket(data, data.length, packet.getIpAddress(), packet.getPort());

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
