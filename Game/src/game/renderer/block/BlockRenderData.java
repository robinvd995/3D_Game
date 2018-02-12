package game.renderer.block;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;

import game.block.Block;
import game.util.EnumDirection;

public class BlockRenderData {

	private String model;
	private HashMap<String,BlockRenderPart> parts;
	
	private BlockRenderData(){}
	
	@Override
	public String toString() {
		return "BlockRenderData [model=" + model + ", parts=" + parts + "]";
	}
	
	public final String getModel(){
		return model;
	}

	public static BlockRenderData from(Block block){
		String path = "res/blocks/" + block.getUnlocalizedName() + ".json";
		File file = new File(path);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("Could not find block render data for block " + block.getUnlocalizedName() + ", this block will not be rendered!");
		}
		
		String jsonData = "";
		if(reader != null){
			String line = "";
			try {
				while((line = reader.readLine()) != null){
					jsonData = jsonData + line.trim();
				}
			} catch (IOException e) {
				System.err.println("Failed to parse the render data for block " + block.getUnlocalizedName() + ", this block will not be rendered!");
			}
		}
		
		Gson gson = new Gson();
		BlockRenderData data = gson.fromJson(jsonData, BlockRenderData.class);
		return data;
	}
	
	public BlockRenderPart getRenderPartFromSide(EnumDirection side){
		return parts.get(side.getUnlocalizedName());
	}
	
	public static class BlockRenderPart{
		
		private String texture;
		
		@Override
		public String toString() {
			return "BlockRenderPart [texture=" + texture + "]";
		}
		
		public String getTexture(){
			return texture;
		}
	}
}
