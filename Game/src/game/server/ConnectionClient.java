package game.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConnectionClient {

	private Socket socket;
	private UUID clientId;
	
	private boolean connected = false;
	
	private volatile Queue<String> outgoingPackets = new ConcurrentLinkedQueue<String>();
	private volatile Queue<String> incomingPackets = new ConcurrentLinkedQueue<String>();
	
	public ConnectionClient(Socket socket, UUID clientId){
		this.socket = socket;
		this.clientId = clientId;
	}
	
	public void startConnection(){
		connected = true;
	}
	
	public void stopConnection(){
		connected = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class ConnectionClientInput implements Runnable{

		private InputStream inputStream;
		
		private ConnectionClientInput(InputStream in){
			this.inputStream = in;
		}
		
		@Override
		public void run() {
			
		}
		
	}
	
	public static class ConnectionClientOutput implements Runnable{

		private OutputStream outputStream;
		
		private ConnectionClientOutput(OutputStream out){
			this.outputStream = out;
		}
		
		@Override
		public void run() {
			
		}
		
	}
}
