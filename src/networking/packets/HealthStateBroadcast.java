package networking.packets;

import networking.client.ClientNetworkHandler;

import java.nio.ByteBuffer;

public class HealthStateBroadcast extends Packet.PacketToClient {


    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // int + double + doubles
    // 4   + 8      + 8      = 20 bytes
    private int id;
    private float playerHealth;

    protected HealthStateBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
        this.id = buffer.getInt();
        this.playerHealth = buffer.getFloat();
    }

    public HealthStateBroadcast(int id, float playerHealth) {
        super();
        this.playerHealth = playerHealth;
        this.id = id;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.HEALTH_STATE_BCAST;
    }

    public int getId() {
        return id;
    }

    public float getPlayerHealth() {
        return playerHealth;
    }


    @Override
    public void handle(ClientNetworkHandler handler) {
        handler.recieveHealthStateBroadcast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        buffer.putInt(this.id);
        buffer.putFloat(this.playerHealth);
        return Packet.getBytesFromBuffer(buffer);
    }

}
