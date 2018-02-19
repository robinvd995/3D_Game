package game.renderer.gui.font;

import game.renderer.texture.TextureData;
import game.renderer.texture.TextureManager;

public class Font {

	private final String fontName;
	private final FontMeta fontMeta;
	private final Glyph[] glyphs;
	private TextureData textureData;
	
	public Font(String name, String image, FontMeta meta, Glyph[] glyphs){
		this.fontName = name;
		this.textureData = TextureManager.createDefaultGuiTexture(image);
		this.fontMeta = meta;
		this.glyphs = glyphs;
	}

	public String getFontName() {
		return fontName;
	}

	public FontMeta getFontMeta() {
		return fontMeta;
	}

	public Glyph getGlyph(int charcode) {
		return glyphs[charcode];
	}
	
	public boolean equals(Object other){
		return other instanceof Font ? ((Font)other).getFontName().equals(fontName) : false;
	}
	
	public TextureData getTextureData(){
		return textureData;
	}
}
