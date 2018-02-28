package game.client.network.packet;

import game.common.network.packet.PacketChat;

public class ClientPacketChat extends PacketChat implements IClientPacket{

	public ClientPacketChat(){}
	
	public ClientPacketChat(String s){
		super(s);
	}
	
	@Override
	public void execute() {
		System.out.println(message);
	}
}