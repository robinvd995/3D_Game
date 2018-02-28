package game.client.network;

import game.client.network.packet.ClientPacketChat;
import game.client.network.packet.ClientPacketDisconnect;
import game.client.network.packet.ClientPacketTest1;
import game.client.network.packet.ClientPacketTest2;
import game.client.network.packet.IClientPacket;
import game.common.config.Configuration;
import game.common.config.ConfigurationException;
import game.common.network.connection.Connection;
import game.common.network.connection.ConnectionDetails;
import game.common.network.packet.IPacket;
import game.common.network.packet.PacketManager;
import game.common.network.packet.PacketPlayerConnect;

public class Client {

	private static Client theClient;

	private ConnectionStatus status = ConnectionStatus.DISCONNECTED;

	private Connection<IClientPacket> connection;
	private PacketManager<IClientPacket> packetManager = new PacketManager<IClientPacket>();

	private Configuration config;

	private Client(String ip, int port, int timeout){
		this.connection = new Connection<IClientPacket>(new ConnectionDetails(ip, port).setTimeout(timeout));
		try {
			this.config = Configuration.loadConfig("client");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void registerPackets(){
		packetManager.registerPacket(ClientPacketChat.class);
		packetManager.registerPacket(ClientPacketDisconnect.class);
		packetManager.registerPacket(ClientPacketTest1.class);
		packetManager.registerPacket(ClientPacketTest2.class);
	}

	private void connect(){
		status = ConnectionStatus.CONNECTING;
		connection.connect();
		if(connection.isConnected()){
			status = ConnectionStatus.CONNECTED;
		}
		else{
			status = ConnectionStatus.CONNECTION_ERROR;
		}
	}

	private void start(){
		this.connection.startConnection(packetManager);

		String playerName = "ERROR";
		try {
			playerName = config.getString("player", "name");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		sendPacket(new PacketPlayerConnect(playerName));
	}

	public void tick(){
		connection.tick();
		if(connection.isConnected()){
			while(connection.hasPacket()){
				IClientPacket packet = connection.getNextPacket();
				packet.execute(this);
			}
		}
	}

	public static synchronized ConnectionStatus getConnectionStatus(){
		return theClient.status;
	}

	public static void createClient(String ip, int port, int timeout){
		theClient = new Client(ip, port, timeout);
		theClient.registerPackets();
		theClient.connect();
		if(theClient.status == ConnectionStatus.CONNECTED)
			theClient.start();
	}

	public static void stopClient(){
		System.out.println("Stopping client!");
		theClient.connection.closeConnection();
	}

	public static boolean hasClient(){
		return theClient != null;
	}

	public static void sendPacket(IPacket packet){
		theClient.connection.sendPacket(packet);
	}

	public static void tickClient(){
		if(theClient != null){
			theClient.tick();
		}
	}


	/*private final String ip;
	private final int port;
	private final int timeout;*/

	//private volatile Queue<IPacket> outgoingPackets = new ConcurrentLinkedQueue<IPacket>();
	//private volatile Queue<IClientPacket> incomingPackets = new ConcurrentLinkedQueue<IClientPacket>();

	//private ClientInput clientInput;
	//private ClientOutput clientOutput;

	/*private NetworkInput<IClientPacket> clientInput;
	private NetworkOutput<IClientPacket> clientOutput;

	private Thread clientInputThread;
	private Thread clientOutputThread;*/

	//private Socket socket;

	/*public static void sendPacket(IPacket packet){
		/*System.out.println("Sending packet to server!");
		theClient.clientOutput.addPacket(packet);
		synchronized(theClient.clientOutput){
			theClient.clientOutput.notify();
		}
	}*/

	/*private void start(){
		tryConnecting();
		if(status == ConnectionStatus.CONNECTED){

			try {
				clientOutput = new NetworkOutput<IClientPacket>(socket.getOutputStream(), packetManager);
				clientInput = new NetworkInput<IClientPacket>(socket.getInputStream(), packetManager);

				clientInputThread = new Thread(clientInput);
				clientOutputThread = new Thread(clientOutput);

				clientInputThread.start();
				clientOutputThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	/*private void tryConnecting(){

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
	}*/

	/*private class ClientInput implements Runnable{

		@Override
		public void run() {
			while(status == ConnectionStatus.CONNECTED){
				try {
					listenToServer();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Catch!");
				}
			}

			System.out.println("Client input thread stopped!");
		}

		private void listenToServer() throws IOException{
			InputStream inStream = socket.getInputStream();
			int size = 0;
			try{
				size = inStream.read();
			}
			catch(SocketException e){
				return;
			}
			DataBuffer buffer = DataBuffer.readFromStream(inStream, size);
			IClientPacket packet = packetManager.getIncomingPacket(buffer);
			incomingPackets.add(packet);
			packet.execute();
		}
	}

	public class ClientOutput implements Runnable{

		@Override
		public void run() {
			while(status == ConnectionStatus.CONNECTED){

				while(!outgoingPackets.isEmpty()){
					IPacket packet = outgoingPackets.poll();
					System.out.println("Sending packet: " + packet.getPacketId());
					DataBuffer buffer = packetManager.writePacket(packet);
					try {
						buffer.writeToStream(socket.getOutputStream());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				synchronized(this){
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			System.out.println("Outgoing socket closing!");
		}

	}*/
}
