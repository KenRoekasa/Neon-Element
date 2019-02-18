package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import networking.Constants;

public class ServerMultiCast extends Thread {
	private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
 
    public void multicast(
      String multicastMessage) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName(Constants.GROUP_SERVER_ADDRESS);
        buf = multicastMessage.getBytes();
 
       
    }
    
    public void run() {
    	while(true) {
    		
    		//String randData = new String();
	    	 DatagramPacket packet 
	     	= new DatagramPacket(buf, buf.length, group, 4446);
	     try {
			this.socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally { socket.close();}
	     
    	}
    }
}
