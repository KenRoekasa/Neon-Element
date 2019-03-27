package networking.packets;

import engine.model.Map;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import networking.client.ClientNetworkHandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class InitialGameStateBroadcast extends Packet.PacketToClient {

    // Bytes required for packet data.
    // Ensure this at least one less than @link{Packet.PACKET_BYTES_LENGTH}
    // Common:
    // double + double + int
    // 8      + 8      + 4   = 20 bytes
    // Additional bytes for each player:
    // int + double + double
    // 4   + 8      + 8      = 20 bytes
    //
    // Maximum size = 20 + 4*20 = 100 bytes

    public Map map;
    public ArrayList<Integer> ids;
    public ArrayList<Point2D> locations;

    protected InitialGameStateBroadcast(ByteBuffer buffer, Sender sender) {
        super(sender);
        Rectangle rect = new Rectangle(buffer.getDouble(), buffer.getDouble());
        // TODO send Walls and respawn points
        this.map = new Map(rect, new ArrayList<>(), new ArrayList<>());
        this.ids = new ArrayList<>();
        this.locations = new ArrayList<>();
        int numPlayers = buffer.getInt();
        for (int i = 0; i < numPlayers; i++) {
            this.ids.add(i, buffer.getInt());
            double x = buffer.getDouble();
            double y = buffer.getDouble();
            this.locations.add(i, new Point2D(x, y));
        }
    }

    public InitialGameStateBroadcast(Map map, ArrayList<Integer> ids, ArrayList<Point2D> locations) {
        super();
        this.map = map;
        this.ids = ids;
        this.locations = locations;
    }

    @Override
    public PacketType getPacketType() {
        return PacketType.INITIAL_STATE_BCAST;
    }

    @Override
    public void handle(ClientNetworkHandler handler) {
        handler.receiveInitialGameStartStateBroadcast(this);
    }

    @Override
    public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();

        buffer.putDouble(this.map.getWidth());
        buffer.putDouble(this.map.getHeight());

        int numPlayers = this.ids.size();
        buffer.putInt(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            buffer.putInt(this.ids.get(i));
            buffer.putDouble(this.locations.get(i).getX());
            buffer.putDouble(this.locations.get(i).getY());
        }

        return Packet.getBytesFromBuffer(buffer);
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public ArrayList<Point2D> getLocations() {
        return locations;
    }

    public Map getMap() {
        return map;
    }

}
