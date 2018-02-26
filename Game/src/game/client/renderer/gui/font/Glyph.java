package game.client.renderer.gui.font;

public class Glyph {

	private int charCode;
	private int offsetX;
	private int offsetY;
	private int advance;
	private int width;
	private int height;
	private float texturePosX0;
	private float texturePosY0;
	private float texturePosX1;
	private float texturePosY1;
	
	public Glyph(int charCode, float textureposx0, float textureposy0, float textureposx1, float textureposy1, int width, int height, int offX, int offY, int advance){
		this.charCode = charCode;
		this.texturePosX0 = textureposx0;
		this.texturePosY0 = textureposy0;
		this.texturePosX1 = textureposx1;
		this.texturePosY1 = textureposy1;
		this.width = width;
		this.height = height;
		this.offsetX = offX;
		this.offsetY = offY;
		this.advance = advance;
	}
	
	public float getTexturePosX0() {
		return texturePosX0;
	}

	public float getTexturePosY0() {
		return texturePosY0;
	}

	public float getTexturePosX1() {
		return texturePosX1;
	}

	public float getTexturePosY1() {
		return texturePosY1;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public int getCharCode(){
		return charCode;
	}
	
	public int getOffsetX(){
		return offsetX;
	}
	
	public int getOffsetY(){
		return offsetY;
	}
	
	public int getAdvance(){
		return advance;
	}
	
	public boolean equals(Object other){
		return other instanceof Glyph ? ((Glyph)other).getCharCode() == charCode : false;
	}
	
	public String toString(){
		return "Glyph: " + (char) charCode;
	}
}
