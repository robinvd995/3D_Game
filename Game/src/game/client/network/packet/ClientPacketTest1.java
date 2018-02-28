package game.client.network.packet;

import game.client.network.Client;
import game.common.network.packet.PacketTest1;

public class ClientPacketTest1 extends PacketTest1 implements IClientPacket{

	@Override
	public void execute(Client client) {
		System.out.println("Test 1: " + flag);
	}

}
