package game.server.network.packet;

import game.common.network.packet.PacketChat;
import game.server.ConnectionClient;
import game.server.io.Logger;

public class ServerPacketChat extends PacketChat implements IServerPacket{

	@Override
	public void execute(ConnectionClient client) {
		Logger.logInfo(this.message);
	}
}
