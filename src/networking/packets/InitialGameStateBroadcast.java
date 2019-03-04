package networking.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import engine.gameTypes.GameType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import networking.client.ClientNetworkHandler;

public class InitialGameStateBroadcast extends Packet.PacketToClient {

	public GameType gameType;
	public ArrayList<Integer> ids;
	public ArrayList<Point2D> locations;
	public Rectangle map;

	protected InitialGameStateBroadcast(ByteBuffer buffer, Sender sender) {
		super(sender);
		// Todo convert from buffer and set attributes
	}

	public InitialGameStateBroadcast(GameType gameType, ArrayList<Integer> ids, ArrayList<Point2D> locations, Rectangle map) {
		super();
		this.gameType = gameType;
		this.ids = ids;
		this.locations = locations;
		this.map = map;
	}

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
		// this identifier has been placed twice
		byte[] gTypeInIDs = convertGTypeToBytes(gameType);
		byte[] idsInByte = convertArrayToBytes(ids);
		byte[] locationsInByte = convertArrayToBytes(locations);
		byte[] mapInBytes = convertMapToBytes(map);
		buffer.put(gTypeInIDs);
		buffer.put(idsInByte);
		buffer.put(locationsInByte);
		buffer.put(mapInBytes);
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

//	public static <E> byte[] toByteArray(ArrayList<E> in) {
//		byte[] result = new byte[in.size()];
//		for (int i = 0; i < in.size(); i++) {
//			result[i] = ((byte) in.get(i));
//
//		}
//		return result;
//	}



	public <E> byte[] convertArrayToBytes(ArrayList<E> object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] yourBytes = null;

		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
			 yourBytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return yourBytes;
	}

	public byte[] convertGTypeToBytes(GameType object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] yourBytes = null;

		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
			yourBytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return yourBytes;
	}

	public byte[] convertMapToBytes(Rectangle object) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] yourBytes = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
			yourBytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return yourBytes;
	}
}