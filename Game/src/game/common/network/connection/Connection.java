package game.common.network.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import game.common.network.packet.IPacket;
import game.common.network.packet.PacketManager;

public class Connection<T extends IPacket> {

	public Socket socket;
	
	private NetworkInput<T> input;
	private NetworkOutput<T> output;
	
	private Thread inputThread;
	private Thread outputThread;
	
	private ConnectionDetails connectionDetails;
	
	private volatile boolean needsDisconnect = false;
	
	//private volatile boolean isConnected = false;
	
	public Connection(Socket socket){
		this.socket = socket;
		//this.isConnected = socket.isConnected();
	}
	
	public Connection(ConnectionDetails connectionDetails){
		this.socket = new Socket();
		this.connectionDetails = connectionDetails;
	}
	
	/**
	 * Tries to connect to the Server Socket, this can only be called when the connection details have been set
	 */
	public void connect(){
		if(connectionDetails == null){
			throw new RuntimeException("No connection details have been set for this connection!");
		}
		
		try {
			socket.connect(connectionDetails.getConnectionAddress(), connectionDetails.getTimeout());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			socket.setKeepAlive(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		//isConnected = socket.isConnected();
	}
	
	/**
	 * Start the connection by creating and invoking the listener and writer threads
	 */
	public void startConnection(PacketManager<T> packetManager){
		try {
			input = new NetworkInput<T>(socket.getInputStream(), packetManager, this);
			output = new NetworkOutput<T>(socket.getOutputStream(), packetManager);
			
			System.out.println("Out initialized!");
			
			inputThread = new Thread(input);
			outputThread = new Thread(output);
			
			inputThread.start();
			outputThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ticks the connection, this is only used to check if the connection needs to be terminated
	 */
	public void tick(){
		if(needsDisconnect){
			closeConnection();
		}
	}
	
	/**
	 * Queues up a packet to be send, also notifies the thread that a packet is ready to be send
	 * @param packet the packet to send
	 */
	public void sendPacket(IPacket packet){
		output.addPacket(packet);
		synchronized(output){
			output.notify();
		}
	}
	
	/**
	 * Checks if there are any packets queued up to be read
	 * @return true if there are packets to be read, false otherwise
	 */
	public boolean hasPacket(){
		return input.hasNextPacket();
	}
	
	/**
	 * Retrieves the next packet in queue and removes it from the queue
	 * @return the next packet in the queue
	 */
	public T getNextPacket(){
		return input.getNextPacket();
	}
	
	/**
	 * Checks whether the socket is connected or not
	 * @return true if the socket is connected
	 */
	public boolean isConnected(){
		return socket.isConnected() && !needsDisconnect;
	}
	
	/**
	 * Closes the socket and stops the writing/listening threads
	 */
	public void closeConnection(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		input.close();
		output.close();
		
		outputThread.interrupt();
		
		try {
			inputThread.join();
			outputThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connection closed!");
	}

	public synchronized void disconnect() {
		needsDisconnect = true;
	}
}
