package com.alien8.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.alien8.networking.packets.Packet;

public abstract class NetworkDispatcher {

    private DatagramSocket socket;

    protected NetworkDispatcher(DatagramSocket socket) {
        this.socket = socket;
    }
    
    protected void send(Packet packet) {
        if (packet.getDirection() == Packet.PacketDirection.OUTGOING) {
            byte[] data = packet.getRawBytes();
            System.out.println("" + packet.getIpAddress() + ":" + packet.getPort() + " <-- " + packet.getType() + " " + new String(data));
            DatagramPacket datagram = new DatagramPacket(data, data.length, packet.getIpAddress(), packet.getPort());
    
            try {
                this.socket.send(datagram);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Attempted to send a recived packet.");
        }
    }
}
