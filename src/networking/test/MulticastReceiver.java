package networking.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import networking.Constants;

public class MulticastReceiver extends Thread {
	
	public static void main(String[] args) {
    	System.setProperty("java.net.preferIPv4Stack", "true");
    	
	MulticastReceiver m = new MulticastReceiver();
	try {
		m.multicast("hi");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
 
    public void multicast(
      String multicastMessage) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        buf = multicastMessage.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }
}
