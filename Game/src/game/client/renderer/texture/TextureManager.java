package game.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import game.client.renderer.RenderRegistry;
import game.client.renderer.block.BlockRenderData;
import game.client.renderer.texture.TextureStitcher.StitchResult;
import game.common.block.Block;
import game.common.json.JsonLoader;

public class TextureManager {

	private static final HashMap<String,TextureData> TEXTURE_DATA_MAP = new HashMap<String,TextureData>();
	private static final HashMap<String,LoadedTexture> TEXTURE_MAP = new HashMap<String,LoadedTexture>();

	private static HashMap<String,MappedTexture> blockTextures;

	private static final TextureData BLOCK_DIFFUSE_DATA = new TextureData("blockDiffuseMap", TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS).setMinFilter(GL11.GL_NEAREST).setMagFilter(GL11.GL_NEAREST);
	private static final TextureData BLOCK_SPECULAR_DATA = new TextureData("blockSpecularMap", TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS).setMinFilter(GL11.GL_NEAREST).setMagFilter(GL11.GL_NEAREST);
	private static final TextureData BLOCK_NORMAL_DATA = new TextureData("blockNormalMap", TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS).setMinFilter(GL11.GL_NEAREST).setMagFilter(GL11.GL_NEAREST);

	public static void loadBlockTextures(){
		List<String> diffuseMapList = new LinkedList<String>();
		List<String> specularMapList = new LinkedList<String>();
		List<String> normalMapList = new LinkedList<String>();

		for(int i = 0; i < Block.BLOCK_AMOUNT; i++){
			BlockRenderData brd = RenderRegistry.getBlockRenderData(i);
			if(brd == null) continue;
			for(String texture : brd.getTextures()){
				System.out.println(texture);
				BlockTextureData data = loadBlockTextureData("res/textures/block/" + texture);

				if(!diffuseMapList.contains(data.getDiffuseMap())){
					diffuseMapList.add(data.getDiffuseMap());
					specularMapList.add(data.getSpecularMap());
					normalMapList.add(data.getNormalMap());
				}
			}
		}

		StitchResult diffuseResult = TextureStitcher.stitchTextures("res/textures/block/diffuse/", "png", diffuseMapList, true);
		StitchResult specularResult = TextureStitcher.stitchTextures("res/textures/block/specular/", "png", specularMapList, false);
		StitchResult normalResult = TextureStitcher.stitchTextures("res/textures/block/normal/", "png", normalMapList, false);

		blockTextures = diffuseResult.getTextures();

		LoadedTexture diffuseTexture = TextureLoader.loadTexture(BLOCK_DIFFUSE_DATA, diffuseResult.getImage());
		LoadedTexture specularTexture = TextureLoader.loadTexture(BLOCK_SPECULAR_DATA, specularResult.getImage());
		LoadedTexture normalTexture = TextureLoader.loadTexture(BLOCK_NORMAL_DATA, normalResult.getImage());

		TEXTURE_MAP.put(BLOCK_DIFFUSE_DATA.getTexture(), diffuseTexture);
		TEXTURE_MAP.put(BLOCK_SPECULAR_DATA.getTexture(), specularTexture);
		TEXTURE_MAP.put(BLOCK_NORMAL_DATA.getTexture(), normalTexture);
	}

	private static BlockTextureData loadBlockTextureData(String filepath){
		try {
			return JsonLoader.loadClassFromJson(filepath, BlockTextureData.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BlockTextureData();
	}

	public static MappedTexture getMappedBlockTexture(String texture){
		return blockTextures.get(texture);
	}

	public static void bindBlockDiffuseMap(){
		bindTexture(BLOCK_DIFFUSE_DATA);
	}
	
	public static void bindBlockSpecularMap(){
		bindTexture(BLOCK_SPECULAR_DATA);
	}
	
	public static void bindBlockNormalMap(){
		bindTexture(BLOCK_NORMAL_DATA);
	}
	
	public static LoadedTexture getBlockDiffuseMap(){
		return TEXTURE_MAP.get(BLOCK_DIFFUSE_DATA.getTexture());
	}

	public static LoadedTexture getBlockSpecularMap(){
		return TEXTURE_MAP.get(BLOCK_SPECULAR_DATA);
	}

	public static LoadedTexture getBlockNormalMap(){
		return TEXTURE_MAP.get(BLOCK_NORMAL_DATA);
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
