package networking.packets;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ConnectPacket extends Packet {
	String playerName;
    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    //
    // = 0 bytes

    protected ConnectPacket(ByteBuffer buffer, InetAddress ipAddress, int port) {
        super(PacketDirection.INCOMING, PacketType.CONNECT, ipAddress, port);
        this.playerName = buffer.toString();
    }

    public ConnectPacket(InetAddress ipAddress, int port) {
        super(PacketDirection.OUTGOING, PacketType.CONNECT, ipAddress, port);
    }

    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.put(getPlayerName().getBytes());
        return Packet.getBytesFromBuffer(buffer);
    }
    
    public String getPlayerName() {
    	return this.playerName;
    }
    
    

}
