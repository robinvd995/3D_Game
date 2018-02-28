package game.client.network.packet;

import game.common.network.packet.PacketTest2;

public class ClientPacketTest2 extends PacketTest2 implements IClientPacket{

	@Override
	public void execute() {
		System.out.println("Test 2: " + flag);
	}

}