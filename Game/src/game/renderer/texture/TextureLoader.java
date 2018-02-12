package game.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import game.renderer.FrameBuffer;

public class TextureLoader {

	public static void saveBufferColorTexture(FrameBuffer framebuffer, int width, int height, String filePath, String format) throws IOException{
		framebuffer.bindBuffer(width, height);
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		
		File file = new File(filePath + "." + format);
		file.mkdirs();
		if(!file.exists()){
			file.createNewFile();
		}
		System.out.println(file.getAbsolutePath());
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				int a = buffer.get(i + 3) & 0xFF;
				image.setRGB(x, height - (y + 1), (a << 24) | (r << 16) | (g << 8) | b);
			}
		}

		ImageIO.write(image, format, file);
		framebuffer.unbindBuffer();
	}
}
