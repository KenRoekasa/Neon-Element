package com.alien8.networking.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.alien8.networking.Constants;
import com.alien8.networking.packets.Packet;

public class ClientTest {

	public static void main(String[] args) {
		try {
			DatagramSocket serverSock = new DatagramSocket();

			int i = 8;
			String data = String.valueOf(i);
			InetAddress ip;
			ip = InetAddress.getByName("192.168.43.199");
			//ip = InetAddress.getLocalHost();

			// byte data[] = new byte [Packet.PACKET_BYTES_LENGTH]; //create an empty packet
			DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), ip, Constants.SERVER_LISTENING_PORT);

			serverSock.send(dp);
			
			byte[] b1 = new byte[1024];
			DatagramPacket dp1 = new DatagramPacket(b1, b1.length);
			serverSock.receive(dp1);
			
			String str = new String(dp1.getData());
			System.out.println("Result is: "+str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // fill packet from input channel
	}

}
