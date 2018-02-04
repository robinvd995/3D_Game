package game.display;

public enum EnumDisplaySize {

	SIZE_1024_768(1024, 768),
	SIZE_1366_768(1366, 768),
	SIZE_1280_1024(1280, 1024),
	SIZE_1440_900(1440, 900),
	SIZE_1600_900(1600, 900),
	SIZE_1920_1080(1920, 1080);
	
	public final int displayWidth;
	public final int displayHeight;
	
	private EnumDisplaySize(int width, int height){
		this.displayWidth = width;
		this.displayHeight = height;
	}
	
	public EnumDisplaySize next(){
		int i = (this.ordinal() + 1) % values().length;
		return values()[i];
	}
	
	public float getAspectRatio(){
		return (float)displayWidth / (float)displayHeight;
	}
}