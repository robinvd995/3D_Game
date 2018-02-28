package game.server.network.packet;

import game.common.network.packet.PacketPlayerConnect;
import game.server.ConnectionClient;
import game.server.Server;

public class ServerPacketPlayerConnect extends PacketPlayerConnect implements IServerPacket{

	public ServerPacketPlayerConnect() {
		super("");
	}

	@Override
	public void execute(ConnectionClient connection) {
		Server.INSTANCE.playerManager.addPlayer(playerName, connection);
	}
}
