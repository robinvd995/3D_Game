package game.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import game.renderer.gui.Gui;
import game.renderer.model.SimpleModel;
import game.renderer.shader.Shader;
import game.renderer.texture.TextureData;

public class GuiComponentHudTexture implements IGuiRenderComponent{

	private static final TextureData TEXTURE = new TextureData("gui/hud").setMinFilter(GL11.GL_LINEAR).setMagFilter(GL11.GL_LINEAR);

	private final Gui gui;
	
	private SimpleModel quad;

	private final int posX;
	private final int posY;
	private final int offX;
	private final int offY;
	private final int u;
	private final int v;
	private final int width;
	private final int height;
	
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
		quad = gui.createQuad(offX, offY, width, height, u, v, 512, 512);
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
}
