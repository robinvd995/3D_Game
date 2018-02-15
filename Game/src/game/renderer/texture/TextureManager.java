package game.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import game.block.Block;
import game.renderer.block.BlockRenderData;
import game.renderer.block.BlockRenderData.BlockRenderPart;
import game.renderer.block.BlockRenderRegistry;
import game.renderer.texture.TextureStitcher.StitchResult;
import game.util.EnumDirection;

public class TextureManager {

	private static final String BLOCK_TEXTURE_MAP_KEY = "blockTextureMap";

	private static final HashMap<String,LoadedTexture> TEXTURE_MAP = new HashMap<String,LoadedTexture>();
	private static HashMap<String,MappedTexture> blockTextures;

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

		LoadedTexture loadedTexture = TextureLoader.loadTexture(result.getImage());
		TEXTURE_MAP.put(BLOCK_TEXTURE_MAP_KEY, loadedTexture);
	}

	public static MappedTexture getMappedBlockTexture(String texture){
		return blockTextures.get(texture);
	}
	
	public static LoadedTexture getBlockTextureMap(){
		return TEXTURE_MAP.get(BLOCK_TEXTURE_MAP_KEY);
	}

	public static void bindBlockTextureMap(){
		bindTexture(BLOCK_TEXTURE_MAP_KEY);
	}
	
	public static void bindTexture(String texture){
		LoadedTexture loadedTexture = getTexture(texture);
		TextureLoader.bindTexture(loadedTexture);
	}
	
	private static LoadedTexture getTexture(String texture){
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
				loadedTexture = TextureLoader.loadTexture(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
			TEXTURE_MAP.put(texture, loadedTexture);
			return loadedTexture;
		}
	}
}
