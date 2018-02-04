package game.input;

public class KeyState {

	private static final KeyState[] STATES = new KeyState[2];
	
	public static final KeyState PRESSED = new KeyState(1);
	public static final KeyState RELEASED = new KeyState(0);
	
	private KeyState(int id){
		STATES[id] = this;
	}
	
	public static KeyState getKeyState(int action){
		return action >= STATES.length ? null : STATES[action];
	}
}
