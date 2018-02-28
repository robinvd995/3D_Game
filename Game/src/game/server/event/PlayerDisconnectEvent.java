package game.server.event;

public class PlayerDisconnectEvent {

	private String player;

	public PlayerDisconnectEvent(String player){
		this.player = player;
	}
}
