package game.server.network.packet;

import game.common.network.packet.PacketClientDisconnect;
import game.server.ConnectionClient;

public class ServerPacketClientDisconnect extends PacketClientDisconnect implements IServerPacket{

	public ServerPacketClientDisconnect(){
		super(0);
	}
	
	public ServerPacketClientDisconnect(int flag) {
		super(flag);
	}

	@Override
	public void execute(ConnectionClient client) {
		System.out.println("Client disconnect: " + flag);
		
		switch(flag){
		default: System.out.println("Client disconnected with an unknown code!");
		case 1:
			//client.stopConnection();
			break;
		}
	}
}
