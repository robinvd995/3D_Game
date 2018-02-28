package game.common.network.connection;

import java.net.InetSocketAddress;

public class ConnectionDetails {

	private final String ip;
	private final int port;
	
	private int timeout = 2000;
	
	public ConnectionDetails(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public ConnectionDetails setTimeout(int timeout){
		this.timeout = timeout;
		return this;
	}
	
	public InetSocketAddress getConnectionAddress(){
		return new InetSocketAddress(ip, port);
	}
	
	public int getTimeout(){
		return timeout;
	}
}
