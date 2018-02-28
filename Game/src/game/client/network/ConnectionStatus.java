package game.client.network;

public enum ConnectionStatus {

	DISCONNECTED,
	DISCONNECTING,
	CONNECTING,
	CONNECTION_ERROR,
	CONNECTED;
	
	private ConnectionStatus(){
		
	}
}
