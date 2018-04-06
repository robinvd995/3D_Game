package game.common.event;

public interface IEventCancellable {
	
	public void cancel();
	public boolean isCanceled();
}
