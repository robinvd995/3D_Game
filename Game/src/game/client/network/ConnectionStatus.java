package game.client.network;

public enum ConnectionStatus {

	DISCONNECTED,
	CONNECTING,
	CONNECTION_ERROR,
	CONNECTED;
	
	private ConnectionStatus(){
		
	}
}
