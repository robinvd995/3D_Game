package game.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import game.client.display.DisplayManager;
import game.client.renderer.FrameBuffer;
import game.common.util.EnumDirection;

public class TextureLoader {

	public static void saveBufferColorTexture(FrameBuffer framebuffer, int width, int height, String filePath, String format) throws IOException{
		framebuffer.bindBuffer();
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
	
	public static void saveDepthBufferTexture(FrameBuffer framebuffer, int width, int height, String filePath, String format) throws IOException{
		framebuffer.bindBuffer();
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, buffer);
		
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
	
	public static LoadedTexture loadTextureToOpenGl(TextureData data, ByteBuffer buffer, int width, int height){
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		if(data.hasMinFilter()){
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, data.getMinFilter());
		}
		if(data.hasMagFilter()){
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, data.getMagFilter());
		}
		if(data.hasWrapS()){
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, data.getWrapS());
		}
		if(data.hasWrapT()){
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, data.getWrapT());
		}
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return new LoadedTexture(textureId);
	}
	
	public static void bindTexture(LoadedTexture texture){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}
	
	public static void takeScreenshot(){
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = DisplayManager.INSTANCE.getDisplayWidth();
		int height = DisplayManager.INSTANCE.getDisplayHeight();
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		String dir = "screenshots/";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		String filename = "screenshot" + dateFormat.format(date);
		try {
			saveImage((dir + filename), "png", width, height, buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveImage(String filePath, String format, int width, int height, ByteBuffer buffer) throws IOException{
		File file = new File(filePath + "." + format);
		file.mkdirs();
		System.out.println("Saved screenshot at: " + file.getAbsolutePath());
		if(!file.exists()){
			file.createNewFile();
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int i = (x + (width * y)) * 4;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				int a = buffer.get(i + 3) & 0xFF;
				image.setRGB(x, height - (y + 1), (a << 24) | (r << 16) | (g << 8) | b);
			}
		}

		ImageIO.write(image, format, file);
	}
	
	public static LoadedTexture loadCubeMap(String texture){
		int textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);
		
		for(EnumDirection dir : EnumDirection.getValidDirections()){
			String filePath = "res/textures/" + texture + "/" + dir.getUnlocalizedName() + ".png";
			BufferedImage image = null;
			try {
				image = ImageIO.read(new File(filePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			ByteBuffer buffer = decodePngImage(image);
			int target = getCubeMapTargetForDirection(dir);
			GL11.glTexImage2D(target, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		return new LoadedTexture(textureId);
	}
	
	private static int getCubeMapTargetForDirection(EnumDirection dir){
		switch(dir){
		case LEFT: return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
		case RIGHT: return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
		case UP: return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
		case DOWN: return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
		case FRONT: return GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
		case BACK: return GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
		default: return 0;
		}
	}
	
	public static LoadedTexture loadTexture(TextureData textureData, BufferedImage image){
		ByteBuffer buffer = decodePngImage(image);
		LoadedTexture loadedTexture = TextureLoader.loadTextureToOpenGl(textureData, buffer, image.getWidth(), image.getHeight());
		return loadedTexture;
	}
	
	public static ByteBuffer decodePngImage(BufferedImage image){
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int pixel = pixels[y * width + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		buffer.flip();
		return buffer;
	}
}
