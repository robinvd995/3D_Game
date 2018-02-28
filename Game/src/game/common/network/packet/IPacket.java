package game.common.network.packet;

import game.common.network.io.IInputBuffer;
import game.common.network.io.IOutputBuffer;

public interface IPacket {
	
	void in(IInputBuffer in);
	void out(IOutputBuffer out);
	int getPacketSize();
	int getPacketId();
}
