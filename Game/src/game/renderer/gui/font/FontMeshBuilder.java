package game.renderer.gui.font;

import java.util.ArrayList;
import java.util.List;

import game.renderer.gui.GuiAnchor;
import game.renderer.model.ModelLoader;
import game.renderer.model.SimpleModel;

public class FontMeshBuilder {

	private final Font font;
	private final float fontSize;
	private List<String> lines = new ArrayList<String>();

	private int vertexPos = 0;
	private int uvPos = 0;

	public FontMeshBuilder(Font font, float fontSize){
		this.font = font;
		this.fontSize = fontSize;
	}

	public void addLine(String line){
		lines.add(line);
	}

	/*public FontMesh build(int screenWidth, int screenHeight, GuiAnchor anchor){

		int bufferSize = 0;
		for(String line : lines){
			bufferSize += line.length() * 12;
		}

		int offY = (int) ((float)getTextHeight() * anchor.getOffY());

		float[] vertices = new float[bufferSize];
		float[] uvs = new float[bufferSize];

		int cursorPosY = 0;
		float normScreenWidth = screenWidth * 0.5f;
		float normScreenHeight = screenHeight * 0.5f;

		for(String line : lines){
			int cursorPosX = 0;
			char[] chars = line.toCharArray();

			int offX = (int) ((float)getLineWidth(chars) * anchor.getOffX());

			for(int i = 0; i < chars.length; i++){
				Glyph glyph = font.getGlyph(chars[i]);

				int posX0 = cursorPosX + glyph.getOffsetX() - offX;
				int posY0 = cursorPosY + glyph.getOffsetY() - offY;
				int posX1 = posX0 + glyph.getWidth();
				int posY1 = posY0 + glyph.getHeight();

				float x0 = (float)(posX0) / (float)(normScreenWidth);
				float y0 = (float)(posY0) / (float)(normScreenHeight);
				float x1 = (float)(posX1) / (float)(normScreenWidth);
				float y1 = (float)(posY1) / (float)(normScreenHeight);

				float u0 = glyph.getTexturePosX0();
				float v0 = glyph.getTexturePosY0();
				float u1 = glyph.getTexturePosX1();
				float v1 = glyph.getTexturePosY1();

				float fx0 = (x0 * fontSize);
				float fy0 = (-(y0 * fontSize));
				float fx1 = (x1 * fontSize);
				float fy1 = (-(y1 * fontSize));

				addVertexToList(vertices, uvs, fx0, fy0, u0, v0);
				addVertexToList(vertices, uvs, fx1, fy0, u1, v0);
				addVertexToList(vertices, uvs, fx1, fy1, u1, v1);
				addVertexToList(vertices, uvs, fx1, fy1, u1, v1);
				addVertexToList(vertices, uvs, fx0, fy1, u0, v1);
				addVertexToList(vertices, uvs, fx0, fy0, u0, v0);

				cursorPosX += glyph.getAdvance();
			}
			cursorPosY += font.getFontMeta().getLineHeight();
		}

		return new FontMesh(vertices, uvs);
		//return ModelLoader.INSTANCE.load2DSimpleModel(vertices, uvs);
	}*/

	public FontMesh build(int screenWidth, int screenHeight, GuiAnchor anchor){

		int bufferSize = 0;
		for(String line : lines){
			bufferSize += line.length();
		}

		int offY = (int) ((float)getTextHeight() * anchor.getOffY());

		int[] indices = new int[bufferSize * 12];
		float[] vertices = new float[bufferSize * 8];
		float[] uvs = new float[bufferSize * 8];

		int cursorPosY = 0;
		float normScreenWidth = screenWidth * 0.5f;
		float normScreenHeight = screenHeight * 0.5f;

		int curIndex = 0;
		int curVertex = 0;
		
		for(String line : lines){
			int cursorPosX = 0;
			char[] chars = line.toCharArray();

			int offX = (int) ((float)getLineWidth(chars) * anchor.getOffX());

			for(int i = 0; i < chars.length; i++){
				Glyph glyph = font.getGlyph(chars[i]);

				int posX0 = cursorPosX + glyph.getOffsetX() - offX;
				int posY0 = cursorPosY + glyph.getOffsetY() - offY;
				int posX1 = posX0 + glyph.getWidth();
				int posY1 = posY0 + glyph.getHeight();

				float x0 = (float)(posX0) / (float)(normScreenWidth);
				float y0 = (float)(posY0) / (float)(normScreenHeight);
				float x1 = (float)(posX1) / (float)(normScreenWidth);
				float y1 = (float)(posY1) / (float)(normScreenHeight);

				float u0 = glyph.getTexturePosX0();
				float v0 = glyph.getTexturePosY0();
				float u1 = glyph.getTexturePosX1();
				float v1 = glyph.getTexturePosY1();

				float fx0 = (x0 * fontSize);
				float fy0 = (-(y0 * fontSize));
				float fx1 = (x1 * fontSize);
				float fy1 = (-(y1 * fontSize));

				/*addVertexToList(vertices, uvs, fx0, fy0, u0, v0);
				addVertexToList(vertices, uvs, fx1, fy0, u1, v0);
				addVertexToList(vertices, uvs, fx1, fy1, u1, v1);
				addVertexToList(vertices, uvs, fx1, fy1, u1, v1);
				addVertexToList(vertices, uvs, fx0, fy1, u0, v1);
				addVertexToList(vertices, uvs, fx0, fy0, u0, v0);*/
				addVertexToList(vertices, uvs, fx0, fy0, u0, v0);
				addVertexToList(vertices, uvs, fx1, fy0, u1, v0);
				addVertexToList(vertices, uvs, fx1, fy1, u1, v1);
				addVertexToList(vertices, uvs, fx0, fy1, u0, v1);
				
				indices[curIndex++] = curVertex + 0;
				indices[curIndex++] = curVertex + 1;
				indices[curIndex++] = curVertex + 2;
				indices[curIndex++] = curVertex + 2;
				indices[curIndex++] = curVertex + 3;
				indices[curIndex++] = curVertex + 0;

				curVertex += 4;
				cursorPosX += glyph.getAdvance();
			}
			cursorPosY += font.getFontMeta().getLineHeight();
		}

		return new FontMesh(indices, vertices, uvs);
		//return ModelLoader.INSTANCE.load2DSimpleModel(vertices, uvs);
	}

	public int getLineWidth(String line){
		return getLineWidth(line.toCharArray());
	}

	public int getLineWidth(char[] chars){
		int width = 0;
		for(int i = 0; i < chars.length; i++){
			Glyph glyph = font.getGlyph(chars[i]);
			if(i + 1 < chars.length){
				width += glyph.getAdvance();
			}
			else{
				width += glyph.getWidth();
			}
		}
		return (int) (width);
	}

	public int getTextHeight(){
		return lines.size() * font.getFontMeta().getLineHeight();
	}

	private void addVertexToList(float[] vertices, float[] uvs, float x, float y, float u, float v){
		vertices[vertexPos+0] = x;
		vertices[vertexPos+1] = y;
		uvs[uvPos+0] = u;
		uvs[uvPos+1] = v;
		uvPos += 2;
		vertexPos += 2;
	}
}
