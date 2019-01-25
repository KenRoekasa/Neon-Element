package com.alien8.networking.test;

import com.alien8.networking.client.ClientNetworkDispatcher;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.alien8.networking.Constants;
import com.alien8.networking.client.ClientNetwork;

public class ManualTestClient {
    public static void main(String[] args) {
        
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            
            String str = "hi";
            byte[] data = str.getBytes();
            
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("192.168.43.60"), Constants.SERVER_LISTENING_PORT);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
