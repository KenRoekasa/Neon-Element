package Networking.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Date;

import Networking.Constants;
import Networking.Packets.Packet;

/** A server which actively broadcasts its 
 * 		 CONNECT_BCAST, DISCONNECT_BCAST,
        READY_STATE, LOCATION_STATE,
        ELEMENT_STATE_BCAST, CAST_SPELL_BCAST     ((byte) 0xF5),
        POWERUP_PICKUP_BCAST, POWERUP_STATE_BCAST  ((byte) 0xF7),
        GAME_START_BCAST, GAME_OVER_BCAST and to a  group */
public class MultiCastServerThread extends ServerNetwork {
	private InetAddress groupAddress;
	private MulticastSocket socket;
	private String ipAdrress;
	
	public MultiCastServerThread() {
		//create a s socket for broadcasting to the desired group and port
		/*The hard-coded port number is SERVER_LISTENING_PORT (the client must have a MulticastSocket bound to this port). 
         * The hard-coded InetAddress of the DatagramPacket is "" and is a group identifier,
         *  (rather than the Internet address of the machine on which a single client is running). 
         *
         * Created in this way, the DatagramPacket is destined for all clients listening to port number 
         * Constants.SERVER_LISTENING_PORT 
         * who are member of the "172.20.10.3" group.
         * 
         * 
         * */
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
	
	public void run() {
       /*do some broadcasting and then it sleeps or
        *  can wait for the next processing*/
		
		try {
			sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }
	
	

	
}
