package game.common.network.io;

public interface IInputBuffer {

	byte[] read(int x);
	byte readByte();
	int readShort();
	float readFloat();
	double readDouble();
	long readLong();
	int readInt();
	String readString();
}
