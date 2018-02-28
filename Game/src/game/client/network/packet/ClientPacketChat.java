package game.client.network.packet;

import game.client.network.Client;
import game.common.network.packet.PacketChat;

public class ClientPacketChat extends PacketChat implements IClientPacket{

	public ClientPacketChat(){}
	
	public ClientPacketChat(String s){
		super(s);
	}
	
	@Override
	public void execute(Client client) {
		System.out.println(message);
	}
}