package game.common.network.packet;

import game.common.network.io.IInputBuffer;
import game.common.network.io.IOutputBuffer;

public class PacketPlayerConnect implements IPacket{

	protected String playerName;
	
	public PacketPlayerConnect(String playerName){
		this.playerName = playerName;
	}
	
	@Override
	public void in(IInputBuffer in) {
		playerName = in.readString();
	}

	@Override
	public void out(IOutputBuffer out) {
		out.writeString(playerName);
	}

	@Override
	public int getPacketSize() {
		return playerName.length() + 1;
	}

	@Override
	public int getPacketId() {
		return 0;
	}
}