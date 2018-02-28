package game.common.network.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class DataBuffer implements IInputBuffer, IOutputBuffer{

	private byte[] byteArray;

	private int writePosition = 0;
	private int readPosition = 0;

	public DataBuffer(int size){
		byteArray = new byte[size];
	}

	public DataBuffer(byte[] array){
		byteArray = array;
	}

	public byte[] read(int x){
		byte[] b = new byte[x];
		for(int i = 0; i < x; i++){
			b[i] = byteArray[readPosition++];
		}
		return b;
	}

	public byte readByte() {
		//checkReadingBufferSize(1);
		return 	byteArray[readPosition++];
	}

	public int readShort() {
		//checkReadingBufferSize(2);
		return 	(byteArray[readPosition++] & 0xFF) << 8 | 
				(byteArray[readPosition++] & 0xFF);
	}

	public float readFloat() {
		//checkReadingBufferSize(4);
		return Float.intBitsToFloat(
				(byteArray[readPosition++] & 0xFF) << 24 |
				(byteArray[readPosition++] & 0xFF) << 16 |
				(byteArray[readPosition++] & 0xFF) << 8 |
				(byteArray[readPosition++] & 0xFF));
	}

	public double readDouble() {
		//checkReadingBufferSize(8);
		return ByteBuffer.wrap(read(8)).getDouble();
	}

	public long readLong() {
		//checkReadingBufferSize(8);
		return ByteBuffer.wrap(read(8)).getLong();
	}

	public int readInt() {
		//checkReadingBufferSize(4);
		return 	(byteArray[readPosition++] & 0xFF) << 24 |
				(byteArray[readPosition++] & 0xFF) << 16 |
				(byteArray[readPosition++] & 0xFF) << 8 |
				(byteArray[readPosition++] & 0xFF);
	}

	public String readString() {
		StringBuilder bldr = new StringBuilder();
		char c;
		while(readPosition < byteArray.length && (c = (char) byteArray[readPosition++]) != 0){
			bldr.append(c);
		}
		return bldr.toString();
	}

	public void write(byte[] x) {
		//checkWritingBufferSize(x.length);
		for(int i = 0; i < x.length; i++){
			byteArray[writePosition++] = x[i];
		}
	}

	public void writeByte(byte x) {
		//checkWritingBufferSize(1);
		byteArray[writePosition++] = x;
	}

	public void writeShort(int x) {
		writeShort((short)x);
	}

	public void writeShort(short x) {
		//checkWritingBufferSize(2);
		byteArray[writePosition++] = (byte) (x >> 8);
		byteArray[writePosition++] = (byte) x;
	}

	public void writeFloat(float x) {
		//checkWritingBufferSize(4);
		int i = Float.floatToIntBits(x);
		byteArray[writePosition++] = (byte) (i >> 24);
		byteArray[writePosition++] = (byte) (i >> 16);
		byteArray[writePosition++] = (byte) (i >> 8);
		byteArray[writePosition++] = (byte) i;
	}

	public void writeDouble(double x) {
		//checkWritingBufferSize(8);
		write(ByteBuffer.allocate(8).putDouble(x).array());
	}

	public void writeLong(long x) {
		//checkWritingBufferSize(8);
		write(ByteBuffer.allocate(8).putLong(x).array());
	}

	public void writeInt(int x) {
		//checkWritingBufferSize(4);
		byteArray[writePosition++] = (byte) (x >> 24);
		byteArray[writePosition++] = (byte) (x >> 16);
		byteArray[writePosition++] = (byte) (x >> 8);
		byteArray[writePosition++] = (byte) x;
	}

	public void writeString(String x) {
		//checkWritingBufferSize(x.length() + 1);
		for(int i = 0; i < x.length(); i++){
			byteArray[writePosition++] = (byte) x.charAt(i);
		}
		byteArray[writePosition++] = (byte) 0;
	}

	public byte[] getByteArray(){
		return byteArray;
	}

	public static int getAllocatedStringSize(String s){
		return s.length() + 1;
	}

	public void reallocateBuffer(int size){
		byteArray = new byte[size];
		writePosition = 0;
		readPosition = 0;
	}

	/*private void checkWritingBufferSize(int required) {
		if(required + writePosition > byteArray.length){
			throw new BufferOutOfBoundsException("Not enoguh space while writing on the buffer! Required: " + (required + writePosition) + ", Allocated: " + byteArray.length);
		}
	}

	private void checkReadingBufferSize(int required) {
		if(required + readPosition > byteArray.length){
			throw new BufferOutOfBoundsException("Not enough space while reading from the buffer!");
		}
	}*/

	public void writeToStream(OutputStream out){
		try {
			out.write(byteArray.length);
			out.write(byteArray);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DataBuffer readFromStream(InputStream in) {
		try {
			int size = in.read();
			byte[] array = new byte[size];
			in.read(array);
			return new DataBuffer(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DataBuffer(0);
	}
}
