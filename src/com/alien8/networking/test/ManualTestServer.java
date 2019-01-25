package com.alien8.networking.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.alien8.networking.Constants;
import com.alien8.networking.packets.Packet;
import com.alien8.networking.server.ServerNetwork;

public class ManualTestServer {
    public static void main(String[] args) {
        DatagramSocket socket;
        try {
            socket = new DatagramSocket(Constants.SERVER_LISTENING_PORT);
   
            try {
                while(true) {
                    byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    
                    socket.receive(packet);
                    
                    String str = new String(data);
                    System.out.println(str);
                }
            } finally {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
}
