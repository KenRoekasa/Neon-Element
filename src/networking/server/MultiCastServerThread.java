package networking.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import networking.packets.Packet;
import networking.Constants;

public class MultiCastServerThread extends ServerNetwork{
    protected BufferedReader in = null;
    protected boolean moreQuotes = true;

    
    public MultiCastServerThread() {
    	try {
			in = new BufferedReader(new FileReader("/home/students/kxf672/work/2ndYear/TeamProject/alien8/src/Networking/Test/one-liners.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
	public void run() {
		
        while (this.running) {
        	try {
        		
        		
            byte[] data = new byte[Packet.PACKET_BYTES_LENGTH];
            
            String dString = null;
            if (in == null)
                dString = new Date().toString();
            else
                dString = getNextQuote();
            data = dString.getBytes();
			
			
			
			
			
			
			/*The hard-coded port number is SERVER_LISTENING_PORT (the client must have a MulticastSocket bound to this port). 
             * The hard-coded InetAddress of the DatagramPacket is "172.20.10.3" and is a group identifier,
             *  (rather than the Internet address of the machine on which a single client is running). 
             *
             * Created in this way, the DatagramPacket is destined for all clients listening to port number 
             * Constants.SERVER_LISTENING_PORT 
             * who are member of the "172.20.10.3" group.
             * 
             * 
             * */
			InetAddress group = InetAddress.getByName("224.0.0.8");
            DatagramPacket packet = new DatagramPacket(data, data.length, group, Constants.SERVER_LISTENING_PORT);
          
            try {
    			in = new BufferedReader(new FileReader("/home/students/kxf672/work/2ndYear/TeamProject/alien8/src/Networking/Test/one-liners.txt"));
    		} catch (FileNotFoundException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
            this.socket.send(packet);
               // this.socket.receive(packet);
                
                //this.parse(packet);
                try {
                    sleep((long)Math.random() * (Constants.FIVE_SECONDS));
                } 
                catch (InterruptedException e) { }	
                
            } catch (IOException e ) {
                e.printStackTrace();
            }

        }
        this.socket.close();
    }
	
	protected String getNextQuote() {
        String returnValue = null;
        try {
        	returnValue = in.readLine();
            if ((returnValue) == null) {
                in.close();
		moreQuotes = false;
                returnValue = "No more quotes. Goodbye.";
            }
        } catch (IOException e) {
        	
        	System.out.println(e.getLocalizedMessage());
            returnValue = "IOException occurred in server.";
        }
        return returnValue;
    }
		
}
