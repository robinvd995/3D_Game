package game.client.network.packet;

import game.client.network.Client;
import game.common.network.packet.IPacket;

public interface IClientPacket extends IPacket{
	
	void execute(Client client);
}
