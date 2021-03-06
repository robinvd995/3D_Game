package game.client.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;

public class GuiComponentBackground implements IGuiRenderComponent{

	//private static final TextureData TEXTURE = TextureManager.createDefaultGuiTexture("gui/background_menu");
	
	private final Gui gui;
	private SimpleModel quad;
	private TextureData texture;
	
	public GuiComponentBackground(Gui gui, TextureData texture){
		this.texture = texture;
		this.gui = gui;
	}
	
	@Override
	public void onComponentAdded() {
		quad = gui.createQuad(GuiAnchor.TOP_LEFT, gui.getGuiWidth(), gui.getGuiHeight(), 0, 0, texture);
	}

	@Override
	public void renderComponent(Shader shader) {
		gui.bindTexture(texture);
		quad.bindModel();
		shader.enableAttribArrays();
		GL11.glDrawElements(GL11.GL_TRIANGLES, quad.getSize(), GL11.GL_UNSIGNED_INT, 0);
		shader.disableAttribArrays();
		quad.unbindModel();
	}

	@Override
	public void onComponentDeleted() {
		quad.delete();
	}

	@Override
	public TextureData getTexture() {
		return texture;
	}

	@Override
	public int getRenderPosX() {
		return 0;
	}

	@Override
	public int getRenderPosY() {
		return 0;
	}

	
}
