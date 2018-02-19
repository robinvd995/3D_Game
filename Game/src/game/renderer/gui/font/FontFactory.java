package game.renderer.gui.font;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FontFactory {

	public static Font createFont(String font){
		String fontDataFile = "res/font/" + font + ".fnt";
		String fontImageFile = "font/" + font;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fontDataFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(reader == null) return null;
		
		FontMeta meta = null;
		Glyph[] glyphs = new Glyph[256];
		
		String line;
		while((line = readLine(reader)) != null){
			if(line.startsWith("common")){
				meta = createFontMetaFromString(line.substring(7));
			}
			else if(line.startsWith("char")){
				Glyph glyph = createGlyphFromString(meta, line.substring(5));
				glyphs[glyph.getCharCode()] = glyph;
			}
		}
		
		return new Font(font, fontImageFile, meta, glyphs);
	}
	
	private static Glyph createGlyphFromString(FontMeta meta, String data){
		int id = 0;
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		int xoffset = 0;
		int yoffset = 0;
		int xadvance = 0;
		String[] split = data.split(" ");
		for(String arg : split){
			String[] args = arg.split("=");
			switch(args[0]){
			case "id":	id = Integer.parseInt(args[1]);
			case "x": x = Integer.parseInt(args[1]);
			case "y": y = Integer.parseInt(args[1]);
			case "width": width = Integer.parseInt(args[1]);
			case "height": height = Integer.parseInt(args[1]);
			case "xoffset": xoffset = Integer.parseInt(args[1]);
			case "yoffset": yoffset = Integer.parseInt(args[1]);
			case "xadvance": xadvance = Integer.parseInt(args[1]);
			}
		}
		
		float textureWidth = meta.getTextureWidth();
		float textureHeight = meta.getTextureHeight();
		float x0 = (float) x / textureWidth;
		float y0 = (float) y / textureHeight;
		float x1 = ((float)(x + width) / textureWidth);
		float y1 = ((float)(y + height) / textureHeight);
		return new Glyph(id, x0, y0, x1, y1, width, height, xoffset, yoffset, xadvance);
	}
	
	private static FontMeta createFontMetaFromString(String data){
		FontMeta meta = new FontMeta();
		String[] split = data.split(" ");
		for(String arg : split){
			String[] args = arg.split("=");
			int value = Integer.parseInt(args[1]);
			switch(args[0]){
			case "lineHeight":
				meta.setLineHeight(value);
				break;
			case "base":
				meta.setBase(value);
				break;
			case "scaleW":
				meta.setTextureWidth(value);
				break;
			case "scaleH":
				meta.setTextureHeight(value);
				break;
			}
		}
		return meta;
	}
	
	private static String readLine(BufferedReader reader){
		try {
			return reader.readLine();
		} catch (IOException e) {
			System.err.println("Error while reading line from font file!");
			//e.printStackTrace();
		}
		return null;
	}
}
