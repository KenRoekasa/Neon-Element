package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

//import networking.Constants;

//import networking.Packets.Packet;

public class MultiCastClient  {
	
	public static void main(String[] args) throws IOException {
		
		

			
				/*To become a member of the "Constants.SERVER_ADDRESS" group, the client calls the MulticastSocket's joinGroup method
				 *  with 
				 * the InetAddress that identifies the group. 
				 * Now, the client is set up to receive DatagramPackets destined for the port and group specified. 
				  */
				MulticastSocket socket = new MulticastSocket(8888);
				InetAddress group = InetAddress.getByName("224.0.0.8");
				socket.joinGroup(group);

				byte[] data = new byte[16];
				DatagramPacket packet;
				
				 while(true) {

				    byte[] buf = new byte[256];
			            packet = new DatagramPacket(buf, buf.length);
			            socket.receive(packet);

				 String received = new String(packet.getData(), 0, packet.getLength());
		         System.out.println("Quote of the Moment: " + received);

				}
				
				/*socket.leaveGroup(group);
				socket.close();*/
	
}
	

	

}
