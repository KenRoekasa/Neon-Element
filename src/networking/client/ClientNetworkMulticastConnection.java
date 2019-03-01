package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import networking.Constants;
import networking.packets.Packet;

public class ClientNetworkMulticastConnection extends Thread {

    private ClientNetwork net;
    private boolean running;

    private InetAddress groupAddress;
    private MulticastSocket socket;

    public ClientNetworkMulticastConnection(ClientNetwork net) {
        super();
        this.net = net;
        
        /*
         * To become a member of the "Constants.SERVER_ADDRESS" group, the client calls
         * the MulticastSocket's joinGroup method with the InetAddress that identifies
         * the group. Now, the client is set up to receive DatagramPackets destined for
         * the port and group specified.
         */
        InetAddress group;
        try {
            group = InetAddress.getByName(Constants.GROUP_SERVER_ADDRESS);
            socket = new MulticastSocket(Constants.BROADCASTING_PORT);
            this.socket.joinGroup(group);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected MulticastSocket getSocket() {
        return this.socket;
    }
    
    protected InetAddress getGroupAddress() {
        return this.groupAddress;
    }
	
	 public void close() {
	        try {
	            this.socket.leaveGroup(groupAddress);
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
            this.socket.close();
            this.running = false;
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
