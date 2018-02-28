package game.common.network.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import game.common.network.io.DataBuffer;
import game.common.network.packet.IPacket;
import game.common.network.packet.PacketManager;

public class NetworkInput<T extends IPacket> implements Runnable{

	private volatile Queue<T> incomingPackets;
	
	private InputStream inStream;
	
	private boolean isClosed = false;
	
	private PacketManager<T> packetManager;
	
	public NetworkInput(InputStream inStream, PacketManager<T> packetManager){
		this.inStream = inStream;
		this.packetManager = packetManager;
		this.incomingPackets = new ConcurrentLinkedQueue<T>();
	}
	
	@Override
	public void run() {
		while(!isClosed){
			listenToServer();
		}

		System.out.println("Client input thread stopped!");
	}

	private void listenToServer(){
		int flag = readDataFromStream();
		if(flag == -1){
			System.out.println("closing the input stream!");
			isClosed = true;
		}
		else{
			DataBuffer buffer = DataBuffer.readFromStream(inStream);
			T packet = packetManager.getIncomingPacket(buffer);
			incomingPackets.add(packet);
		}
	}
	
	private int readDataFromStream(){
		try {
			return inStream.read();
		} catch (IOException e) {
			System.out.println("Catchin!");
		}
		return -1;
	}
	
	public synchronized boolean hasNextPacket(){
		return !incomingPackets.isEmpty();
	}
	
	public synchronized T getNextPacket(){
		return incomingPackets.poll();
	}
	
	public boolean isClosed(){
		return isClosed;
	}
	
	public void close(){
		isClosed = true;
	}
}
