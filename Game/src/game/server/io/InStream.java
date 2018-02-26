package game.server.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

public class InStream extends InputStream{

	private Queue<Byte> byteQueue = new LinkedList<Byte>();
	
	@Override
	public int read() throws IOException {
		return byteQueue.isEmpty() ? -1 : byteQueue.poll();
	}

}
