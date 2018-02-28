package game.server.network.packet;

import game.common.network.packet.IPacket;
import game.server.ConnectionClient;

public interface IServerPacket extends IPacket{
	
	void execute(ConnectionClient connection);
}
