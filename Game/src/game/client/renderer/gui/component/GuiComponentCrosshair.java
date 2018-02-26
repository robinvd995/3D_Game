package game.client.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import game.client.renderer.gui.Gui;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;

public class GuiComponentCrosshair implements IGuiRenderComponent {

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/crosshair2", 64, 64);

	private final Gui gui;
	
	private SimpleModel quad;

	private final int posX;
	private final int posY;
	
	public GuiComponentCrosshair(Gui gui, int posX, int posY){
		this.gui = gui;
		this.posX = posX;
		this.posY = posY;
	}
	
	@Override
	public void onComponentAdded() {
		quad = gui.createQuad(-32, -32, 64, 64, 0, 0, TEXTURE);
	}

	@Override
	public void renderComponent(Shader shader) {
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
	public int getRenderPosX() {
		return posX;
	}

	@Override
	public int getRenderPosY() {
		return posY;
	}

	@Override
	public TextureData getTexture() {
		return TEXTURE;
	}
}
