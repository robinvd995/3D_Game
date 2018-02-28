package game.client.network.packet;

import game.client.network.Client;
import game.common.network.packet.PacketClientDisconnect;

public class ClientPacketDisconnect extends PacketClientDisconnect implements IClientPacket{

	public ClientPacketDisconnect(){
		super(0);
	}
	
	public ClientPacketDisconnect(int flag) {
		super(flag);
	}

	@Override
	public void execute(Client client) {
		switch(flag){
		case 2:
			
			break;
		}
	}

}
