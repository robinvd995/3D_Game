package game.client.renderer.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import game.client.renderer.FrameBuffer;

public class GuiFrameBuffer extends FrameBuffer{

	private final int colorTexture;
	
	private GuiFrameBuffer(int buffer, int width, int height) {
		super(buffer, width, height);
		this.colorTexture = this.createTextureAttachment();
		this.unbindBuffer();
	}
	
	public static GuiFrameBuffer createFrameBuffer(Gui gui){
		int buffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return new GuiFrameBuffer(buffer, gui.getGuiWidth(), gui.getGuiHeight());
	}
	
	public int getColorTexture(){
		return colorTexture;
	}
	
	public void bindColorTexture(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
	}

	@Override
	protected void onBufferDeleted() {
		GL11.glDeleteTextures(colorTexture);
	}
}
