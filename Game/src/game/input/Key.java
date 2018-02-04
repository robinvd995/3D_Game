package game.input;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

public class Key {

	private static final HashMap<Integer,Key> KEY_MAP = new HashMap<Integer,Key>();
	
	public static final Key RIGHT = new Key("Right", GLFW.GLFW_KEY_RIGHT);
	public static final Key LEFT = new Key("Left", GLFW.GLFW_KEY_LEFT);
	public static final Key DOWN = new Key("Down", GLFW.GLFW_KEY_DOWN);
	public static final Key UP = new Key("Up", GLFW.GLFW_KEY_UP);
	
	public static final Key W = new Key("W", GLFW.GLFW_KEY_W);
	public static final Key S = new Key("S", GLFW.GLFW_KEY_S);
	public static final Key A = new Key("A", GLFW.GLFW_KEY_A);
	public static final Key D = new Key("D", GLFW.GLFW_KEY_D);
	
	public static final Key SPACE = new Key("Space", GLFW.GLFW_KEY_SPACE);
	public static final Key LEFT_SHIFT = new Key("Left_Shift", GLFW.GLFW_KEY_LEFT_SHIFT);
	public static final Key LEFT_CTRL = new Key("Left_Ctrl", GLFW.GLFW_KEY_LEFT_CONTROL);
	
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
