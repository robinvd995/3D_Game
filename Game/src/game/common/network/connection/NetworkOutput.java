package game.common.network.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import game.common.network.io.DataBuffer;
import game.common.network.packet.IPacket;
import game.common.network.packet.PacketManager;

public class NetworkOutput<T extends IPacket> implements Runnable{

	private volatile Queue<T> outgoingPackets;
	
	private OutputStream outStream;
	
	private boolean isActive = false;
	
	private PacketManager<T> packetManager;
	
	public NetworkOutput(OutputStream outStream, PacketManager<T> packetManager){
		this.outStream = outStream;
		this.packetManager = packetManager;
		this.outgoingPackets = new ConcurrentLinkedQueue<T>();
	}
	
	public void addPacket(IPacket packet){
		outgoingPackets.add((T) packet);
		System.out.println("Adding packet! current in queue: " + outgoingPackets.size());
	}
	
	@Override
	public void run() {
		isActive = true;
		while(isActive){
			System.out.println("Looking for packets to send! current in queue: " + outgoingPackets.size());
			while(!outgoingPackets.isEmpty()){
				try {
					outStream.write(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Found packet, sending!");
				IPacket packet = outgoingPackets.poll();
				System.out.println("Sending packet: " + packet.getPacketId());
				DataBuffer buffer = packetManager.writePacket(packet);
				buffer.writeToStream(outStream);
			}
			synchronized(this){
				try {
					this.wait();
				} catch (InterruptedException e) {}
			}
		}

		System.out.println("Outgoing socket closing!");
	}
	
	public boolean isClosed(){
		return !isActive;
	}

	public void close(){
		isActive = false;
	}
}
