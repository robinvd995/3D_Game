package game.client.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread{

	private static Client theClient;
	
	private final String ip;
	private final int port;
	private final int timeout;

	private static volatile ConnectionStatus status = ConnectionStatus.DISCONNECTED;

	private Socket socket;

	private Client(String ip, int port, int timeout){
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
	}

	@Override
	public void run(){
		tryConnecting();

		while(status == ConnectionStatus.CONNECTED){
			
		}
		
		cleanUp();
	}

	private void tryConnecting(){

		status = ConnectionStatus.CONNECTING;

		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(ip, port), timeout);
		} catch (IOException e) {
			e.printStackTrace();
			status = ConnectionStatus.CONNECTION_ERROR;
			return;
		}

		try {
			socket.setKeepAlive(true);
		} catch (SocketException e) {
			e.printStackTrace();
			status = ConnectionStatus.CONNECTION_ERROR;
			return;
		}

		if(socket.isConnected()){
			status = ConnectionStatus.CONNECTED;
		}
	}

	private void cleanUp(){
		//theClient = null;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized ConnectionStatus getConnectionStatus(){
		return status;
	}

	public static void createClient(String ip, int port, int timeout){
		theClient = new Client(ip, port, timeout);
		status = ConnectionStatus.DISCONNECTED;
		theClient.start();
	}
	
	public static void stopClient(){
		status = ConnectionStatus.DISCONNECTED;
	}
	
	public static boolean hasClient(){
		return theClient != null;
	}
}
