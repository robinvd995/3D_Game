package game.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import game.client.renderer.block.BlockRenderData;
import game.client.renderer.block.BlockRenderData.BlockRenderPart;
import game.client.renderer.block.BlockRenderRegistry;
import game.client.renderer.texture.TextureStitcher.StitchResult;
import game.common.block.Block;
import game.common.util.EnumDirection;

public class TextureManager {

	private static final HashMap<String,TextureData> TEXTURE_DATA_MAP = new HashMap<String,TextureData>();
	private static final HashMap<String,LoadedTexture> TEXTURE_MAP = new HashMap<String,LoadedTexture>();
	private static HashMap<String,MappedTexture> blockTextures;

	private static final TextureData BLOCK_TEXTURE_DATA = new TextureData("blockTextureMap", TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS).setMinFilter(GL11.GL_NEAREST).setMagFilter(GL11.GL_NEAREST);
	
	public static void loadAllTextures(){
		Set<String> textures = new HashSet<String>();
		for(int i = 0; i < Block.BLOCK_AMOUNT; i++){
			BlockRenderData brd = BlockRenderRegistry.getBlockRenderData(i);
			if(brd == null) continue;
			for(EnumDirection dir : EnumDirection.values()){
				BlockRenderPart part = brd.getRenderPartFromSide(dir);
				if(part == null) continue;
				textures.add(part.getTexture());
			}
		}

		System.out.println("Found " + textures.size() + "unique textures to map!");
		StitchResult result = TextureStitcher.stitchTextures("res/textures/", "png", textures);

		blockTextures = result.getTextures();
		for(String s : blockTextures.keySet()){
			MappedTexture mappedTexture = blockTextures.get(s);
			System.out.println(s + ":" + mappedTexture.toVector());
		}

		LoadedTexture loadedTexture = TextureLoader.loadTexture(BLOCK_TEXTURE_DATA, result.getImage());
		TEXTURE_MAP.put(BLOCK_TEXTURE_DATA.getTexture(), loadedTexture);
	}

	public static MappedTexture getMappedBlockTexture(String texture){
		return blockTextures.get(texture);
	}
	
	public static LoadedTexture getBlockTextureMap(){
		return TEXTURE_MAP.get(BLOCK_TEXTURE_DATA.getTexture());
	}

	public static void bindBlockTextureMap(){
		bindTexture(BLOCK_TEXTURE_DATA);
	}
	
	public static void bindTexture(TextureData textureData){
		LoadedTexture loadedTexture = getTexture(textureData);
		TextureLoader.bindTexture(loadedTexture);
	}
	
	private static LoadedTexture getTexture(TextureData textureData){
		String texture = textureData.getTexture();
		if(TEXTURE_MAP.containsKey(texture)){
			return TEXTURE_MAP.get(texture);
		}
		else{
			//LoadedTexture loadedTexture = loadTexture(texture);
			File file = new File("res/textures/" + texture + ".png");
			LoadedTexture loadedTexture = null;
			try {
				System.out.println(file.getAbsolutePath());
				BufferedImage img = ImageIO.read(file);
				loadedTexture = TextureLoader.loadTexture(textureData, img);
			} catch (IOException e) {
				e.printStackTrace();
			}
			TEXTURE_MAP.put(texture, loadedTexture);
			return loadedTexture;
		}
	}
	
	public static TextureData getDefaultGuiTexture(String texture, int width, int height){
		if(TEXTURE_DATA_MAP.containsKey(texture)){
			return TEXTURE_DATA_MAP.get(texture);
		}
		else{
			TextureData data = new TextureData(texture, width, height).setMinFilter(GL11.GL_LINEAR).setMagFilter(GL11.GL_LINEAR);
			TEXTURE_DATA_MAP.put(texture, data);
			return data;
		}
	}

	public static void deleteTexture(TextureData textureData) {
		LoadedTexture texture = TEXTURE_MAP.get(textureData.getTexture());
		GL11.glDeleteTextures(texture.getTextureID());
	}
}
