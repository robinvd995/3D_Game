package game.input;

import java.util.HashMap;

import caesar.util.Vector2f;
import game.display.DisplayManager;

public class InputManager {

	private static HashMap<Key,KeyState> keyStateMap = new HashMap<Key,KeyState>();

	private static double lastMousePositionX;
	private static double lastMousePositionY;

	private static double mouseDeltaX;
	private static double mouseDeltaY;

	public static void invoke(int keycode, int scancode, int action, int mods) {
		Key key = Key.getKey(keycode);
		KeyState state = KeyState.getKeyState(action);
		if(state != null){
			keyStateMap.put(key, state);
		}
	}

	public static boolean isKeyDown(Key key){
		return KeyState.PRESSED == keyStateMap.get(key);
	}

	public static void invokeMousePos(double mouseX, double mouseY){
		mouseDeltaX += mouseX - lastMousePositionX;
		mouseDeltaY += mouseY - lastMousePositionY;

		Vector2f centerPos = DisplayManager.INSTANCE.getScreenCenterCoordinates();
		if(DisplayManager.INSTANCE.isCursorsDisabled()){
			DisplayManager.INSTANCE.setMousePosition(centerPos);
		}
		
		lastMousePositionX = centerPos.getX();
		lastMousePositionY = centerPos.getY();
	}

	public static void end(){
		mouseDeltaX = 0;
		mouseDeltaY = 0;
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