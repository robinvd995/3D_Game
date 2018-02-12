package game.renderer.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import game.renderer.FrameBuffer;

public class TextureFrameBuffer extends FrameBuffer{
	
	private int colorTexture;
	
	private TextureFrameBuffer(int buffer) {
		super(buffer);
		this.colorTexture = createColorTexture(TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS);
		this.unbindBuffer();
	}
	
	public static TextureFrameBuffer createFrameBuffer(){
		int buffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return new TextureFrameBuffer(buffer);
	}
	
	public int getColorTexture(){
		return colorTexture;
	}
}
