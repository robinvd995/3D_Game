package game.input;

public class KeyTracker {

	private final Key key;
	
	private boolean isPressed = false;
	
	public KeyTracker(Key keyToTrack){
		this.key = keyToTrack;
	}
	
	public boolean isKeyAction(int action){
		if(!isPressed && InputManager.isKeyDown(key)){
			isPressed = true;
			return action == 0;
		}
		if(isPressed && !InputManager.isKeyDown(key)){
			isPressed = false;
			return action == 1;
		}
		return false;
	}
}
