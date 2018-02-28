package game.common.network.packet;

import java.util.HashMap;

import game.common.network.io.DataBuffer;

public class PacketManager<T extends IPacket> {

	private HashMap<Integer,Class<? extends IPacket>> packetMap;

	public PacketManager(){
		packetMap = new HashMap<Integer,Class<? extends IPacket>>();
	}

	public void registerPacket(Class<? extends IPacket> clzz){
		IPacket packet = instantiatePacketFromClass(clzz);
		if(packet == null){
			throw new RuntimeException("Unable to register the packet for class " + clzz.getName() + "!");
		}
		else{
			packetMap.put(packet.getPacketId(), clzz);
		}
	}

	private IPacket instantiatePacketFromClass(Class<? extends IPacket> clzz){
		try {
			return clzz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public T getIncomingPacket(DataBuffer buffer) {
		int packetId = buffer.readByte();
		Class<? extends IPacket> packetClzz = packetMap.get(packetId);
		if(packetClzz == null){
			throw new RuntimeException("Could not find a packet with id: " + packetId);
		}
		T packet = (T) instantiatePacketFromClass(packetClzz);
		packet.in(buffer);
		return packet;
	}

	public DataBuffer writePacket(IPacket packet){
		int id = packet.getPacketId();
		int size = packet.getPacketSize() + 1;
		DataBuffer buffer = new DataBuffer(size);
		buffer.writeByte((byte) id);
		packet.out(buffer);
		return buffer;
	}
}
