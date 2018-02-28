package game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import game.server.io.Logger;

public class ConnectionListener extends Thread{

	private volatile boolean isRunning = false;

	private final int serverPort;
	
	private ServerSocket socket;
	
	public ConnectionListener(int serverPort){
		this.serverPort = serverPort;
		this.setName("connection_listener");
	}
	
	public int startListening(){
		try {
			socket = new ServerSocket(serverPort);
			isRunning = true;
			start();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 1;
	}
	public void run(){
		Logger.logInfo("Started Listening for clients!");
		while(isRunning){
			try {
				Socket clientSocket = socket.accept();
				Logger.logInfo("Client trying to connect...");
				Server.INSTANCE.addClientToConnect(clientSocket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopListening(){
		isRunning = false;
	}
}
