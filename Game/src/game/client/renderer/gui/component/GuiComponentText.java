package game.client.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import caesar.util.Vector4f;
import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.gui.font.Font;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.shader.Shader;

public class GuiComponentText implements IGuiTextComponent{

	protected final Gui gui;
	
	protected String text;
	protected int posX;
	protected int posY;
	protected final GuiAnchor anchor;
	protected final Font font;
	
	protected float textSize = 1.0f;
	protected Vector4f color = null;
	
	private SimpleModel textModel;
	
	public GuiComponentText(Gui gui, String text, Font font, int posX, int posY, GuiAnchor anchor){
		this.gui = gui;
		this.text = text;
		this.font = font;
		this.posX = posX;
		this.posY = posY;
		this.anchor = anchor;
	}
	
	public GuiComponentText(Gui gui, String text, String font, int posX, int posY, GuiAnchor anchor){
		this(gui, text, FontManager.getFont(font), posX, posY, anchor);
	}
	
	public GuiComponentText(Gui gui, String text, String font, int posX, int posY, GuiAnchor anchor, float textSize){
		this(gui, text, font, posX, posY, anchor);
		this.textSize = textSize;
	}
	
	public GuiComponentText(Gui gui, String text, Font font, int posX, int posY, GuiAnchor anchor, Vector4f color, float textSize){
		this(gui, text, font, posX, posY, anchor);
		this.textSize = textSize;
		this.color = color;
	}
	
	@Override
	public void onComponentAdded() {
		textModel = gui.createTextMesh(font, text, anchor, textSize);
	}

	@Override
	public void onComponentDeleted() {
		textModel.delete();
	}

	@Override
	public void renderText(Shader shader) {
		textModel.bindModel();
		shader.enableAttribArrays();
		if(color != null) shader.loadVector4f("color", color);
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, textModel.getSize());
		GL11.glDrawElements(GL11.GL_TRIANGLES, textModel.getSize(), GL11.GL_UNSIGNED_INT, 0);
		if(color != null) shader.loadVector4f("color", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		shader.disableAttribArrays();
		textModel.unbindModel();
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public int getTextPosX() {
		return posX;
	}

	@Override
	public int getTextPosY() {
		return posY;
	}

}
