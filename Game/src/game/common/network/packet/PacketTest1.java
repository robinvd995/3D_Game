package game.common.network.packet;

import game.common.network.io.IInputBuffer;
import game.common.network.io.IOutputBuffer;

public class PacketTest1 implements IPacket{

	protected int flag = 17;
	
	@Override
	public void in(IInputBuffer in) {
		flag = in.readByte();
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
		return 3;
	}

}
