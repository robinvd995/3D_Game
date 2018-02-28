package game.server.command;

import java.util.Collection;

import game.common.network.packet.PacketChat;
import game.server.ConnectionClient;
import game.server.Server;
import game.server.io.Logger;

public class CommandChat implements ICommand{

	@Override
	public String[] getPrefix() {
		return new String[]{"chat", "c"};
	}

	@Override
	public void execute(String[] args) {
		String message = "";
		for(String s : args){
			message = message + s + " ";
		}
		Logger.logInfo(message);
		Collection<ConnectionClient> connections = Server.INSTANCE.getAllConnections();
		System.out.println(connections.size());
		for(ConnectionClient client : Server.INSTANCE.getAllConnections()){
			PacketChat packet = new PacketChat(message);
			client.sendPacket(packet);
		}
	}

	@Override
	public int[] argumentCounts() {
		return null;
	}

}
