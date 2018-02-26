package game.client.renderer.gui.font;

import java.util.HashMap;

public class FontManager {

	private static final HashMap<String,Font> FONT_MAP = new HashMap<String,Font>();
	
	public static void loadFont(String fontname){
		Font font = FontFactory.createFont(fontname);
		FONT_MAP.put(fontname, font);
	}
	
	public static Font getFont(String fontname){
		return FONT_MAP.get(fontname);
	}
}
