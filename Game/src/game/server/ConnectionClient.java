package game.server;

import java.net.Socket;
import java.util.UUID;

import game.common.network.connection.Connection;
import game.common.network.packet.IPacket;
import game.server.network.packet.IServerPacket;

public class ConnectionClient {

	private UUID clientId;

	private Connection<IServerPacket> connection;

	public ConnectionClient(Socket socket, UUID clientId){
		this.connection = new Connection<IServerPacket>(socket);
		this.clientId = clientId;
	}

	public void startConnection(){
		connection.startConnection(Server.INSTANCE.packetManager);
	}

	public void tick(){
		while(connection.hasPacket()){
			IServerPacket packet = connection.getNextPacket();
			packet.execute(this);
		}

		if(!connection.isConnected()){
			stop();
		}
	}

	public void sendPacket(IPacket packet){
		connection.sendPacket(packet);
	}

	public void stop(){
		Server.INSTANCE.addClientToDisconnect(this);
	}

	public UUID getClientId(){
		return clientId;
	}

	/*public void stopConnection(){
	System.out.println("Client disconnected!");
	connected = false;
	try {
		socket.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}*/
	
	//private Socket socket;

	//private volatile boolean connected = false;

	//private volatile Queue<IPacket> outgoingPackets = new ConcurrentLinkedQueue<IPacket>();
	//private volatile Queue<IServerPacket> incomingPackets = new ConcurrentLinkedQueue<IServerPacket>();

	/*private NetworkInput<IServerPacket> clientInput;
		private NetworkOutput<IServerPacket> clientOutput;

		private Thread clientInputThread;
		private Thread clientOutputThread;*/

	/*while(!incomingPackets.isEmpty()){
	IServerPacket packet = incomingPackets.poll();
	packet.execute(this);
}*/
	
	/*clientOutput.addPacket(packet);
	synchronized(clientOutput){
		clientOutput.notify();
	}*/

	/*try {
	clientInput = new ConnectionClientInput(socket.getInputStream());
	clientOutput = new ConnectionClientOutput(socket.getOutputStream());

	clientInput.start();
	clientOutput.start();
} catch (IOException e) {
	e.printStackTrace();
}*/

	/*try {
	clientOutput = new NetworkOutput<IServerPacket>(socket.getOutputStream(), Server.INSTANCE.packetManager);
	clientInput = new NetworkInput<IServerPacket>(socket.getInputStream(), Server.INSTANCE.packetManager);

	clientInputThread = new Thread(clientInput);
	clientOutputThread = new Thread(clientOutput);

	clientInputThread.start();
	clientOutputThread.start();
} catch (IOException e) {
	e.printStackTrace();
}*/

	/*public class ConnectionClientInput extends Thread{

		private volatile boolean isClosing = false;

		private InputStream inputStream;

		private ConnectionClientInput(InputStream in){
			this.inputStream = in;
		}

		@Override
		public void run() {
			while(connected && !isClosing){
				int size = 0;
				try {
					size = inputStream.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				DataBuffer buffer = DataBuffer.readFromStream(inputStream, size);
				if(buffer != null){
					IServerPacket packet = Server.INSTANCE.packetManager.getIncomingPacket(buffer);
					incomingPackets.add(packet);
				}
				else{
					System.out.println("Is closinmgh@");
					isClosing = true;
				}
			}
		}

	}

	public class ConnectionClientOutput extends Thread{

		private OutputStream outputStream;

		private ConnectionClientOutput(OutputStream out){
			this.outputStream = out;
		}

		@Override
		public void run() {
			System.out.println("Start");
			while(connected){
				System.out.println("Checking packets to send!");
				while(!outgoingPackets.isEmpty()){
					IPacket packet = outgoingPackets.poll();
					DataBuffer buffer = Server.INSTANCE.packetManager.writePacket(packet);
					buffer.writeToStream(outputStream);
					System.out.println("Sending packet!");
				}
				synchronized(this){
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}*/
}
