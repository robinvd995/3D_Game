package game.client.renderer.gui.font;

public class FontMeta {

	private int lineHeight;
	private int base;
	private int textureWidth;
	private int textureHeight;
	
	protected FontMeta(){}

	protected void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	protected void setBase(int base) {
		this.base = base;
	}

	protected void setTextureWidth(int textureWidth) {
		this.textureWidth = textureWidth;
	}

	protected void setTextureHeight(int textureHeight) {
		this.textureHeight = textureHeight;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public int getBase() {
		return base;
	}

	public int getTextureWidth() {
		return textureWidth;
	}

	public int getTextureHeight() {
		return textureHeight;
	}
}
