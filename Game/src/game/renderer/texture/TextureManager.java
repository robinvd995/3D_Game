package game.renderer.texture;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import game.block.Block;
import game.renderer.block.BlockRenderData;
import game.renderer.block.BlockRenderData.BlockRenderPart;
import game.renderer.block.BlockRenderRegistry;
import game.renderer.texture.TextureStitcher.StitchResult;
import game.util.EnumDirection;

public class TextureManager {

	private static int blockTextureMap;
	
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
		HashMap<String,MappedTexture> textureMap = result.getTextures();
		for(String s : textureMap.keySet()){
			MappedTexture mappedTexture = textureMap.get(s);
			System.out.println(s + ":" + mappedTexture.toVector());
		}
	}
	
	public static int getBlockTextureMap(){
		return blockTextureMap;
	}
}
