package game.client.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import caesar.util.Vector2f;
import game.client.Game;
import game.client.display.DisplayManager;

public class InputManager {

	private static HashMap<Key,KeyState> keyStateMap = new HashMap<Key,KeyState>();
	
	private static double lastMousePositionX;
	private static double lastMousePositionY;

	private static double mouseDeltaX;
	private static double mouseDeltaY;

	private static List<IKeyListener> keyListeners = new ArrayList<IKeyListener>();
	private static List<IMouseListener> mouseListeners = new ArrayList<IMouseListener>();
	private static List<ICharListener> charListeners = new ArrayList<ICharListener>();
	private static List<IScrollListener> scrollListeners = new ArrayList<IScrollListener>();
	
	public static void invokeKey(int keycode, int scancode, int action, int mods) {
		Key key = Key.getKey(keycode);
		KeyState state = KeyState.getKeyState(action);
		if(state != null){
			keyStateMap.put(key, state);
		}

		for(IKeyListener listener : keyListeners){
			listener.onKeyAction(keycode, scancode, mods, action);
		}
	}

	public static boolean isKeyDown(Key key){
		return KeyState.PRESSED == keyStateMap.get(key);
	}


	public static void invokeMouseButton(int button, int action, int mods) {
		//Mouse button stuff
		
		System.out.println(button);
		
		for(IMouseListener listener : mouseListeners){
			listener.onMouseClicked(button, action, mods);
		}
	}
	
	public static void invokeCharType(char character){
		
		
		for(ICharListener listener : charListeners){
			listener.onCharTyped(character);
		}
	}
	
	public static void invokeMousePos(double mouseX, double mouseY){

		if(!Game.INSTANCE.isMouseDisabled()){
			mouseDeltaX += mouseX - lastMousePositionX;
			mouseDeltaY += mouseY - lastMousePositionY;

			Vector2f centerPos = DisplayManager.INSTANCE.getScreenCenterCoordinates();

			DisplayManager.INSTANCE.setMousePosition(centerPos);

			lastMousePositionX = centerPos.getX();
			lastMousePositionY = centerPos.getY();
		}
		for(IMouseListener listener : mouseListeners){
			listener.onMouseMoved((int)mouseX, (int)mouseY);
		}
	}
	

	public static void invokeScroll(double xoffset, double yoffset) {
		for(IScrollListener listener : scrollListeners){
			listener.onScroll(xoffset, yoffset);
		}
	}

	public static void end(){
		mouseDeltaX = 0;
		mouseDeltaY = 0;
	}
	
	public static void addCharListener(ICharListener listener){
		charListeners.add(listener);
	}

	public static void addKeyListener(IKeyListener listener){
		keyListeners.add(listener);
	}

	public static void addMouseListener(IMouseListener listener){
		mouseListeners.add(listener);
	}
	
	public static void addScrollListener(IScrollListener listener){
		scrollListeners.add(listener);
	}
	
	public static void removeCharListener(ICharListener listener){
		charListeners.remove(listener);
	}

	public static void removeKeyListener(IKeyListener listener){
		keyListeners.remove(listener);
	}

	public static void removeMouseListener(IMouseListener listener){
		keyListeners.remove(listener);
	}
	
	public static void removeScrollListener(IScrollListener listener){
		scrollListeners.remove(listener);
	}

	public static double getMouseDeltaX(){
		return mouseDeltaX;
	}

	public static double getMouseDeltaY(){
		return mouseDeltaY;
	}

	public static double getMousePosX(){
		return lastMousePositionX;
	}

	public static double getMousePosY(){
		return lastMousePositionY;
	}
}