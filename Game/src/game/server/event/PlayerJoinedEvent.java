package game.server.event;

public class PlayerJoinedEvent {

	private String player;
	
	public PlayerJoinedEvent(String player){
		this.player = player;
	}
}
