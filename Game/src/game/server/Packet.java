package game.server;

import game.server.util.IInputBuffer;
import game.server.util.IOutputBuffer;

public class Packet {

	private String data;
	
	public Packet(){}
	
	public Packet(String data){
		this.data = data;
	}
	
	public void in(IInputBuffer in) {
		data = in.readString();
	}
	
	public void out(IOutputBuffer out) {
		out.writeString(data);
	}
	
	public int getPacketSize(){
		return data.length() + 1;
	}
	
	public void execute(){
		System.out.println(data);
	}
}
