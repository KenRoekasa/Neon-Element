package com.alien8.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import com.alien8.networking.Constants;

public class ServerNetwork extends Thread {
    private ArrayList<PlayerConnection> connections;
    private DatagramSocket socket;
    
    ServerNetwork() {
        this.connections = new ArrayList<>();
        
        try {
            this.socket = new DatagramSocket(Constants.SERVER_LISTENING_PORT);
            System.out.println("Server is running...");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        while (true) {
            byte[] data = new byte[1];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            
            try {
                this.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            this.parse(packet);
        }
    }
    
    private void parse(DatagramPacket packet) {
        
    }
}
