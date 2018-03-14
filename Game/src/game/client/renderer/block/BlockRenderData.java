package game.client.renderer.block;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import game.common.block.Block;
import game.common.json.JsonLoader;

public class BlockRenderData {

	private String renderer;
	private HashMap<String,String> textures;
	
	private BlockRenderData(){}
	
	public String getTexture(String key){
		return textures.get(key);
	}
	
	@Override
	public String toString() {
		return "BlockRenderData [model=" + renderer + ", data=" + textures + "]";
	}
	
	public final String getRenderer(){
		return renderer;
	}
	
	public Collection<String> getTextures(){
		return textures.values();
	}

	public static BlockRenderData from(Block block){
		String path = "res/blocks/" + block.getUnlocalizedName();
		BlockRenderData data = new BlockRenderData();
		try {
			data = JsonLoader.loadClassFromJson(path, BlockRenderData.class);
		} catch (FileNotFoundException e) {
			System.err.println("Could not find block render data for block " + block.getUnlocalizedName() + ", this block will not be rendered!");
		} catch (IOException e) {
			System.err.println("Failed to parse the render data for block " + block.getUnlocalizedName() + ", this block will not be rendered!");
		}
		return data;
	}
}
