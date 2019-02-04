package networking.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Date;

import networking.packets.ConnectAckPacket;
import networking.packets.ConnectPacket;
import networking.packets.DisconnectAckPacket;
import networking.packets.HelloAckPacket;
import networking.packets.HelloPacket;
import networking.packets.Packet;
import networking.packets.Packet.PacketType;
import networking.Constants;

/**
 * A server which actively broadcasts its CONNECT_BCAST, DISCONNECT_BCAST,
 * READY_STATE, LOCATION_STATE, ELEMENT_STATE_BCAST, CAST_SPELL_BCAST ((byte)
 * 0xF5), POWERUP_PICKUP_BCAST, POWERUP_STATE_BCAST ((byte) 0xF7),
 * GAME_START_BCAST, GAME_OVER_BCAST and to a group
 */
public class MultiCastServerThread extends ServerNetwork {
	private InetAddress groupAddress;
	private MulticastSocket socket;
	private String ipAdrress;

	public MultiCastServerThread() {
		// create a s socket for broadcasting to the desired group and port
		/*
		 * The hard-coded port number is SERVER_LISTENING_PORT (the client must have a
		 * MulticastSocket bound to this port). The hard-coded InetAddress of the
		 * DatagramPacket is "" and is a group identifier, (rather than the Internet
		 * address of the machine on which a single client is running).
		 *
		 * Created in this way, the DatagramPacket is destined for all clients listening
		 * to port number Constants.SERVER_LISTENING_PORT who are member of the
		 * "172.20.10.3" group.
		 * 
		 * 
		 */
		try {
			groupAddress = InetAddress.getByName(Constants.SERVER_ADDRESS);
			socket = new MulticastSocket(Constants.SERVER_LISTENING_PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	 protected void broadcast(Packet packet) {
		 
		        if (packet.getDirection() == Packet.PacketDirection.OUTGOING) {
		            byte[] data = packet.getRawBytes();
		            DatagramPacket datagram = new DatagramPacket(data, data.length, groupAddress, Constants.SERVER_LISTENING_PORT);
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

