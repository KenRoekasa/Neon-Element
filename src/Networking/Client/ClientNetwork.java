package Networking.Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Networking.Packets.*;

public class ClientNetwork extends Thread {
    protected String name;
    protected boolean running;
    protected DatagramSocket socket;
    private ClientNetworkDispatcher dispatcher;

    public ClientNetwork() {
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.running = true;
        this.dispatcher = new ClientNetworkDispatcher(this.socket);
    }

    //This clinet cons takes in the serverAddresws and playerName in as an arg from the command line
    public ClientNetwork(String name, String serverAddress) {
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.running = true;
        this.dispatcher = new ClientNetworkDispatcher(this.socket, serverAddress);
        this.name = name;
    }
    
    public ClientNetworkDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void close() {
        this.running = false;
        this.dispatcher.close();
    }

    public void run() {
        while (this.running) {
            byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                this.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.parse(packet);
        }
    }

    protected void parse(DatagramPacket datagram) {
        Packet packet = Packet.createFromBytes(datagram.getData(), datagram.getAddress(), datagram.getPort());
        
        switch(packet.getType()) {
            case HELLO_ACK:
                this.dispatcher.receiveHelloAck((HelloAckPacket) packet);
                break;
            default:
                System.out.println("Invalid packet recieved");
        }
    }

}
