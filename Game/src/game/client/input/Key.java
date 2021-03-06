package game.client.input;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

public class Key {

	private static final HashMap<Integer,Key> KEY_MAP = new HashMap<Integer,Key>();
	
	public static final Key RIGHT = new Key("Right", GLFW.GLFW_KEY_D);
	public static final Key LEFT = new Key("Left", GLFW.GLFW_KEY_A);
	public static final Key DOWN = new Key("Down", GLFW.GLFW_KEY_S);
	public static final Key UP = new Key("Up", GLFW.GLFW_KEY_W);
	
	public static final Key MOVE_UP = new Key("MU", GLFW.GLFW_KEY_Z);
	public static final Key MOVE_DOWN = new Key("MD", GLFW.GLFW_KEY_X);
	
	public static final Key E = new Key("E", GLFW.GLFW_KEY_E);
	
	public static final Key ESC = new Key("Escape", GLFW.GLFW_KEY_ESCAPE);
	public static final Key SPACE = new Key("Space", GLFW.GLFW_KEY_SPACE);
	public static final Key LEFT_SHIFT = new Key("Left_Shift", GLFW.GLFW_KEY_LEFT_SHIFT);
	public static final Key LEFT_CTRL = new Key("Left_Ctrl", GLFW.GLFW_KEY_LEFT_CONTROL);
	
	public static final Key F12 = new Key("Screenshot", GLFW.GLFW_KEY_F12);
	
	private final String name;
	private int keyCode;
	
	public Key(String name, int keyCode){
		this.name = name;
		this.keyCode = keyCode;
		KEY_MAP.put(keyCode, this);
	}
	
	public String getName(){
		return name;
	}
	
	public int getKeyCode(){
		return keyCode;
	}
	
	public void setKeyCode(int keyCode){
		this.keyCode = keyCode;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		return o instanceof Key ? ((Key)o).name.equals(name) : false;
	}
	
	public static Key getKey(int keyCode){
		return KEY_MAP.get(keyCode);
	}
}
