package game.common.network.packet;

import game.common.network.io.IInputBuffer;
import game.common.network.io.IOutputBuffer;

public class PacketChat implements IPacket{

	protected String message;
	
	public PacketChat(){}
	
	public PacketChat(String message){
		this.message = message;
	}
	
	@Override
	public void in(IInputBuffer in) {
		message = in.readString();
	}

	@Override
	public void out(IOutputBuffer out) {
		out.writeString(message);
	}

	@Override
	public int getPacketSize() {
		return message.length() + 1;
	}

	@Override
	public int getPacketId() {
		return 0;
	}
}
