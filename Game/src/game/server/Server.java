package game.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import game.server.command.CommandContainer;
import game.server.command.CommandListener;
import game.server.command.CommandManager;
import game.server.io.Logger;

public class Server {	

	public static final Server INSTANCE = new Server();

	private HashMap<UUID,ConnectionClient> connectionMap;

	private volatile Queue<Socket> clientsToAdd;

	private ConnectionListener connectionListener;

	private volatile boolean isRunning;

	private Queue<CommandContainer> commandQueue;

	private Server(){
		connectionMap = new HashMap<UUID,ConnectionClient>();
		Thread.currentThread().setName("server");
	}

	public void init(){
		isRunning = true;
	}

	protected int start(){
		//Instantiate the queue for clients to add
		clientsToAdd = new ConcurrentLinkedQueue<Socket>();
		commandQueue = new ConcurrentLinkedQueue<CommandContainer>();
		//Instantiate the connection listener and listen for connections
		connectionListener = new ConnectionListener(25565);
		connectionListener.startListening();

		CommandListener.createCommandListener();
		return 0;
	}

	public void run(){
		checkConnectionsToAdd();
		checkCommands();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void checkConnectionsToAdd(){
		while(!clientsToAdd.isEmpty()){
			Socket clientSocket = clientsToAdd.poll();
			UUID id = UUID.randomUUID();
			ConnectionClient connection = new ConnectionClient(clientSocket, id);
			connection.startConnection();
		}
	}

	private void checkCommands(){
		while(!commandQueue.isEmpty()){
			CommandContainer container = commandQueue.poll();
			container.execute();
		}
	}

	public void stopServer(){
		isRunning = false;
	}

	protected int stop(){
		connectionListener.stopListening();
		for(ConnectionClient client : connectionMap.values()){
			client.stopConnection();
		}
		return 0;
	}

	public synchronized void addClient(Socket client){
		Logger.logInfo("Client connected!");
		clientsToAdd.add(client);
	}

	public synchronized void addCommand(String line){
		CommandContainer container = CommandManager.INSTANCE.getCommand(line);
		if(container.isValid()){
			commandQueue.add(container);
		}
		else{
			container.logError();
		}
	}

	public boolean isRunning(){
		return isRunning;
	}
}
