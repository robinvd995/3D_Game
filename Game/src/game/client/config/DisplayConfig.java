package game.client.config;

import game.client.display.EnumDisplayMode;
import game.client.display.EnumDisplaySize;

public class DisplayConfig {

	private EnumDisplaySize resolution = EnumDisplaySize.SIZE_1024_768;
	private EnumDisplayMode displayMode = EnumDisplayMode.WINDOWED;
	private int fov = 65;
	private int fps = 60;
	
	public EnumDisplaySize getDisplaySize(){
		return resolution;
	}
	
	public EnumDisplayMode getDisplayMode(){
		return displayMode;
	}
	
	public int getFov(){
		return fov;
	}
	
	public void setDisplaySize(EnumDisplaySize size){
		this.resolution = size;
	}
	
	public void setDisplayMode(EnumDisplayMode mode){
		this.displayMode = mode;
	}
	
	public void setFov(int fov){
		this.fov = fov;
	}
	
	public int getFps(){
		return fps;
	}
}
