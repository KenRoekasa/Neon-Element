package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import networking.Constants;

//import networking.Constants;

//import networking.Packets.Packet;

public class MultiCastClient extends ClientNetwork{

	private InetAddress groupAddress;
	private MulticastSocket socket;

	public MultiCastClient() {

		/*
		 * To become a member of the "Constants.SERVER_ADDRESS" group, the client calls
		 * the MulticastSocket's joinGroup method with the InetAddress that identifies
		 * the group. Now, the client is set up to receive DatagramPackets destined for
		 * the port and group specified.
		 */
		InetAddress group;
		try {
			group = InetAddress.getByName(Constants.SERVER_ADDRESS);
			socket = new MulticastSocket(Constants.SERVER_LISTENING_PORT);
			this.socket.joinGroup(group);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public void run() {
		
		
		byte[] data = new byte[16];
		DatagramPacket packet;

		while (true) {

			byte[] buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		

	}

}
