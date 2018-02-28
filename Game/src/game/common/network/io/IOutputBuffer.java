package game.common.network.io;

public interface IOutputBuffer {

	void write(byte[] x);
	void writeByte(byte x);
	void writeShort(int x);
	void writeShort(short x);
	void writeFloat(float x);
	void writeDouble(double x);
	void writeLong(long x);
	void writeInt(int x);
	void writeString(String x);
}
