package game.client.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import caesar.util.Vector4f;
import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.gui.font.Font;
import game.client.renderer.model.StreamModel;
import game.client.renderer.shader.Shader;

public class GuiComponentDynamicText extends GuiComponentText{

	private StreamModel dynamicTextModel;
	
	public GuiComponentDynamicText(Gui gui, String text, Font font, int posX, int posY, GuiAnchor anchor) {
		super(gui, text, font, posX, posY, anchor);
	}

	public GuiComponentDynamicText(Gui gui, String text, Font font, int posX, int posY, GuiAnchor anchor, Vector4f color, float textSize){
		super(gui, text, font, posX, posY, anchor, color, textSize);
	}
	
	@Override
	public void onComponentAdded() {
		dynamicTextModel = gui.createDynamicTextMesh(font, text, anchor, textSize);
	}

	@Override
	public void onComponentDeleted() {
		dynamicTextModel.delete();
	}

	@Override
	public void renderText(Shader shader) {
		dynamicTextModel.bindModel();
		shader.enableAttribArrays();
		if(color != null) shader.loadVector4f("color", color);
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, dynamicTextModel.getSize());
		GL11.glDrawElements(GL11.GL_TRIANGLES, dynamicTextModel.getSize(), GL11.GL_UNSIGNED_INT, 0);
		if(color != null) shader.loadVector4f("color", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		shader.disableAttribArrays();
		dynamicTextModel.unbindModel();
	}
	
	public void updateText(String newText){
		if(!this.text.equals(newText)){
			this.text = newText;
			gui.updateText(dynamicTextModel, font, text, anchor, textSize);
		}
	}
}