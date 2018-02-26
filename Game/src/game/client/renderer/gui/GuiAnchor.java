package game.client.renderer.gui;

public enum GuiAnchor {

	LEFT(0.0f, 0.5f),
	RIGHT(1.0f, 0.5f),
	TOP(0.5f, 0.0f),
	BOTTOM(0.5f, 1.0f),
	TOP_LEFT(0.0f, 0.0f),
	TOP_RIGHT(1.0f, 0.0f),
	BOTTOM_LEFT(0.0f, 1.0f),
	BOTTOM_RIGHT(1.0f, 1.0f),
	CENTER(0.5f, 0.5f);
	
	private final float offX;
	private final float offY;
	
	private GuiAnchor(float offX, float offY){
		this.offX = offX;
		this.offY = offY;
	}
	
	public float getOffX(){
		return offX;
	}
	
	public float getOffY(){
		return offY;
	}
}
