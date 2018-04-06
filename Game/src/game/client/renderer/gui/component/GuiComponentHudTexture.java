package game.client.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import game.client.renderer.gui.Gui;
import game.client.renderer.model.StreamModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;

public class GuiComponentHudTexture implements IGuiRenderComponent{

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/hud", 512, 512);

	private final Gui gui;
	
	private StreamModel quad;

	private int posX;
	private int posY;
	private int offX;
	private int offY;
	private int u;
	private int v;
	private int width;
	private int height;
	
	public GuiComponentHudTexture(Gui gui, int posX, int posY, int offX, int offY, int u, int v, int width, int height){
		this.gui = gui;
		this.posX = posX;
		this.posY = posY;
		this.offX = offX;
		this.offY = offY;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void onComponentAdded() {
		quad = gui.createDynamicQuad(offX, offY, width, height, u, v, TEXTURE);
	}

	@Override
	public void renderComponent(Shader shader) {
		gui.bindTexture(TEXTURE);
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
	
	public void updateTexture(int newU, int newV){
		this.u = newU;
		this.v = newV;
		gui.updateQuadTexture(quad, width, height, u, v, TEXTURE.getWidth(), TEXTURE.getHeight());
	}
}
