package game.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import caesar.util.Vector4f;
import game.renderer.gui.Gui;
import game.renderer.gui.GuiAnchor;
import game.renderer.gui.font.Font;
import game.renderer.gui.font.FontManager;
import game.renderer.model.SimpleModel;
import game.renderer.shader.Shader;
import game.renderer.texture.TextureData;

public class GuiComponentCrosshair implements IGuiRenderComponent {

	private static final TextureData TEXTURE = new TextureData("gui/crosshair2").setMinFilter(GL11.GL_LINEAR).setMagFilter(GL11.GL_LINEAR);

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
		quad = gui.createQuad(-32, -32, 64, 64, 0, 0, 64, 64);
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
