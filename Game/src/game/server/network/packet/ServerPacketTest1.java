package game.server.network.packet;

import game.common.network.packet.PacketTest1;
import game.server.ConnectionClient;

public class ServerPacketTest1 extends PacketTest1 implements IServerPacket{

	@Override
	public void execute(ConnectionClient connection) {
		System.out.println("Test 1: " + flag);
	}

}