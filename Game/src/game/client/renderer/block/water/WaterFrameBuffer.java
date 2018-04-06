package game.client.renderer.block.water;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import game.client.display.DisplayManager;
import game.client.display.IDisplaySizeListener;
import game.client.renderer.FrameBuffer;

public class WaterFrameBuffer extends FrameBuffer implements IDisplaySizeListener{

	private final int colorTexture;
	private final int depthTexture;
	
	private WaterFrameBuffer(int buffer, int width, int height) {
		super(buffer, width, height);
		this.colorTexture = this.createTextureAttachment();
		this.depthTexture = this.createDepthTextureAttachment();
		this.unbindBuffer();
	}
	
	public static WaterFrameBuffer createFrameBuffer(){
		int buffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return new WaterFrameBuffer(buffer, DisplayManager.INSTANCE.getDisplayWidth(), DisplayManager.INSTANCE.getDisplayHeight());
	}
	
	public int getColorTexture(){
		return colorTexture;
	}
	
	public void bindColorTexture(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
	}
	
	public void bindDepthTexture(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
	}

	@Override
	protected void onBufferDeleted() {
		GL11.glDeleteTextures(colorTexture);
		GL11.glDeleteTextures(depthTexture);
	}

	@Override
	public void onDisplaySizeChanged(DisplayManager displayManager) {
		//Change fbo width and height based on the new display size
	}

}
