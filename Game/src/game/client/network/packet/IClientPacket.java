package game.client.network.packet;

import game.common.network.packet.IPacket;

public interface IClientPacket extends IPacket{
	
	void execute();
}
