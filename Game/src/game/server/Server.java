package game.server;

import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import game.common.config.Configuration;
import game.common.network.packet.PacketManager;
import game.server.command.CommandContainer;
import game.server.command.CommandListener;
import game.server.command.CommandManager;
import game.server.io.Logger;
import game.server.network.packet.IServerPacket;
import game.server.network.packet.ServerPacketChat;
import game.server.network.packet.ServerPacketClientDisconnect;
import game.server.network.packet.ServerPacketTest1;
import game.server.network.packet.ServerPacketTest2;

public class Server {	

	public static final Server INSTANCE = new Server();

	private HashMap<UUID,ConnectionClient> connectionMap;

	private volatile Queue<Socket> clientsToAdd;
	private volatile Queue<ConnectionClient> clientsToDisconnect;

	private ConnectionListener connectionListener;

	private volatile boolean isRunning;

	private Queue<CommandContainer> commandQueue;
	
	public PacketManager<IServerPacket> packetManager = new PacketManager<IServerPacket>();

	private Configuration config;
	
	private Server(){
		connectionMap = new HashMap<UUID,ConnectionClient>();
		Thread.currentThread().setName("server");
	}

	public void init(){
		registerPackets();
		config = Configuration.loadConfig("server");
	}
	
	private void registerPackets(){
		packetManager.registerPacket(ServerPacketChat.class);
		packetManager.registerPacket(ServerPacketClientDisconnect.class);
		packetManager.registerPacket(ServerPacketTest1.class);
		packetManager.registerPacket(ServerPacketTest2.class);
	}

	protected int start(){
		isRunning = true;
		//Instantiate the queue for clients to add
		clientsToAdd = new ConcurrentLinkedQueue<Socket>();
		clientsToDisconnect = new ConcurrentLinkedQueue<ConnectionClient>();
		commandQueue = new ConcurrentLinkedQueue<CommandContainer>();
		//Instantiate the connection listener and listen for connections
		int port = config.getInteger("network", "port");
		connectionListener = new ConnectionListener(port);
		connectionListener.startListening();

		CommandListener.createCommandListener();
		return 0;
	}

	public void run(){
		checkConnectionsToAdd();
		checkCommands();
		try {
			connectionMap.values().forEach(client -> client.tick());
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
			connectionMap.put(id, connection);
			connection.startConnection();
		}
	}

	private void checkCommands(){
		while(!commandQueue.isEmpty()){
			CommandContainer container = commandQueue.poll();
			container.execute();
		}
	}

	public Collection<ConnectionClient> getAllConnections(){
		return connectionMap.values();
	}
	
	public void stopServer(){
		isRunning = false;
	}

	protected int stop(){
		connectionListener.stopListening();
		for(ConnectionClient client : connectionMap.values()){
			//client.stopConnection();
		}
		return 0;
	}

	public synchronized void addClientToConnect(Socket client){
		Logger.logInfo("Client connected!");
		clientsToAdd.add(client);
	}

	public synchronized void addClientToDisconnect(ConnectionClient client){
		clientsToDisconnect.add(client);
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
