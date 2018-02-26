package game.server.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class OutStream extends PrintStream {

	private List<IStreamListener> listeners;
	
	public OutStream(OutputStream out) {
		super(out);
		listeners = new ArrayList<IStreamListener>();
	}
	
	public void addStreamListener(IStreamListener listener){
		listeners.add(listener);
	}
	
	@Override
	public void write(int b){
		super.write(b);
	}
	
	@Override
	public void write(byte[] bytes, int off, int len ){
		super.write(bytes, off, len);
		for(IStreamListener listener : listeners){
			listener.onWrite(bytes, off, len);
		}
	}
	
	@Override
	public void write(byte[] bytes){
		try {
			super.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
