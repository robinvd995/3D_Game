package game.client.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import game.client.display.DisplayManager;
import game.client.renderer.RenderManager;
import game.client.renderer.gui.Gui;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;

public class GuiComponentOpenGLTexture implements IGuiRenderComponent{

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/background_menu", 256, 256);
	
	private final Gui gui;
	private SimpleModel quad;
	
	public GuiComponentOpenGLTexture(Gui gui){
		this.gui = gui;
	}
	
	@Override
	public void onComponentAdded() {
		quad = gui.createQuad(60, 60, gui.getGuiWidth(), gui.getGuiHeight(), 0, 0, DisplayManager.INSTANCE.getDisplayWidth(), DisplayManager.INSTANCE.getDisplayHeight(), gui.getGuiWidth(), gui.getGuiHeight());
	}

	@Override
	public void renderComponent(Shader shader) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, RenderManager.waterRenderer.getBuffer().getColorTexture());
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
		return TEXTURE;
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
