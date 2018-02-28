package game.server.network.packet;

import game.common.network.packet.PacketTest2;
import game.server.ConnectionClient;

public class ServerPacketTest2 extends PacketTest2 implements IServerPacket{

	@Override
	public void execute(ConnectionClient connection) {
		System.out.println("Test 2: " + flag);
	}

}