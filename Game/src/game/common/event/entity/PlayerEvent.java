package game.common.event.entity;

import game.common.entity.Player;
import game.common.event.IEventCancellable;

public abstract class PlayerEvent {
	
	protected Player player;
	
	protected PlayerEvent(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public static class OnPlayerSwitchItemEvent extends PlayerEvent {

		private int prevHeldItemIndex;
		
		public OnPlayerSwitchItemEvent(Player player, int prev) {
			super(player);
			this.prevHeldItemIndex = prev;
		}
		
		public int getPrevHeldItemIndex(){
			return prevHeldItemIndex;
		}
	}
	
	public static class BeforePlayerSwitchItemEvent extends PlayerEvent implements IEventCancellable{

		private boolean isCancelled = false;
		
		public BeforePlayerSwitchItemEvent(Player player) {
			super(player);
		}

		@Override
		public void cancel() {
			isCancelled = true;
		}

		@Override
		public boolean isCanceled() {
			return isCancelled;
		}
		
	}
}
