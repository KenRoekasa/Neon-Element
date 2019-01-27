package com.alien8.networking.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.alien8.networking.Constants;

public class ServerTest {
		public static void main(String[] args)  {
			try {
				
			DatagramSocket ds = new DatagramSocket(Constants.SERVER_LISTENING_PORT);
			byte[] b1 = new byte[1024];
			DatagramPacket dp = new DatagramPacket(b1, b1.length);
			
				ds.receive(dp);
				String str = new String(dp.getData());
				int num = Integer.parseInt(str.trim());
				int result = num*num;
				InetAddress ia = dp.getAddress();
				byte[] b2 = (result + "").getBytes();
				//byte[] b2 = ("Victor your machine has been connected to the server").getBytes();

				
				DatagramPacket dp1 = new DatagramPacket(b2, b2.length,ia,dp.getPort());
				ds.send(dp1);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
}
