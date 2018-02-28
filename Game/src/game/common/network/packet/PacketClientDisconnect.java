package game.common.network.packet;

import game.common.network.io.IInputBuffer;
import game.common.network.io.IOutputBuffer;

public class PacketClientDisconnect implements IPacket{

	protected int flag;
	
	public PacketClientDisconnect(int flag){
		this.flag = flag;
	}
	
	@Override
	public void in(IInputBuffer in) {
		this.flag = in.readByte();
	}

	@Override
	public void out(IOutputBuffer out) {
		out.writeByte((byte) flag);
	}

	@Override
	public int getPacketSize() {
		return 1;
	}

	@Override
	public int getPacketId() {
		return 1;
	}

}
