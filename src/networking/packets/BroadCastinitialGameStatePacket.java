package networking.packets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import engine.gameTypes.GameType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import networking.packets.Packet.PacketDirection;
import networking.packets.Packet.PacketType;
import networking.server.PlayerConnection;

public class BroadCastinitialGameStatePacket extends Packet{
	
	
	public GameType gameType;
	public ArrayList<Integer> ids;
	public ArrayList<Point2D> locations;
	public ArrayList<PlayerConnection> playersInfo;
	public Rectangle map;
	
	
	public BroadCastinitialGameStatePacket(GameType gameType,  Rectangle map, ArrayList<PlayerConnection> playersInfo) {
		super(PacketDirection.OUTGOING, PacketType.INITIAL_STATE_BCAST);
		this.gameType = gameType;
		//this.ids = ids;
		this.playersInfo = playersInfo;
		//this.locations = locations;
		this.map = map;
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<PlayerConnection> getPlayersInfo() {
		return playersInfo;
	}

	public void setPlayersInfo(ArrayList<PlayerConnection> playersInfo) {
		this.playersInfo = playersInfo;
	}

	@Override
	 public byte[] getRawBytes() {
        ByteBuffer buffer = this.getByteBuffer();
        	//this identifier has been placed twice
       byte[] idsInByte = toByteArray(ids);
       byte[] locationsInByte = toByteArray(locations);
       buffer.put(idsInByte);
       buffer.put(locationsInByte);
        return Packet.getBytesFromBuffer(buffer);
    }
	
	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	public ArrayList<Integer> getIds() {
		return ids;
	}

	public void setIds(ArrayList<Integer> ids) {
		this.ids = ids;
	}

	public ArrayList<Point2D> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Point2D> locations) {
		this.locations = locations;
	}

	public Rectangle getMap() {
		return map;
	}

	public void setMap(Rectangle map) {
		this.map = map;
	}

	public static <E> byte[] toByteArray(ArrayList<E> in) {
		byte[] result = new byte[in.size()];
		for(int i = 0; i < in.size(); i++) {
		    result[i] = ((byte) in.get(i));
		
	    }
	    return result;
	}

}
