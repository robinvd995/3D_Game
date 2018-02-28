package game.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import game.common.event.EventManager;
import game.server.event.PlayerDisconnectEvent;
import game.server.event.PlayerJoinedEvent;

public class PlayerManager {
	
	private HashMap<UUID,String> playerMap = new HashMap<UUID,String>();
	
	public void addPlayer(String player, ConnectionClient client){
		System.out.println("Add player");
		playerMap.put(client.getClientId(), player);
		EventManager.postEvent(new PlayerJoinedEvent(player));
	}
	
	public Collection<String> getAllPlayers(){
		return playerMap.values();
	}

	public void removePlayer(UUID clientId) {
		String player = playerMap.get(clientId);
		playerMap.remove(clientId);
		EventManager.postEvent(new PlayerDisconnectEvent(player));
	}
}
