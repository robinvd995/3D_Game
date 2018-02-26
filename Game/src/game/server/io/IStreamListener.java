package game.server.io;

public interface IStreamListener {

	void onWrite(int b);
	void onWrite(byte[] bytes, int off, int len);
	void onWrite(byte[] bytes);
}
