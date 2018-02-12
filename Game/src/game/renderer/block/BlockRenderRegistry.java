package game.renderer.block;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

import converter.api.OIMModelLoader;
import converter.api.model.IndexedModel;
import game.block.Block;
import game.renderer.model.LoadedModel;
import game.renderer.model.ModelLoader;

public class BlockRenderRegistry {

	private static BlockRenderer[] blockRenderArray = new BlockRenderer[Block.BLOCK_AMOUNT];
	private static BlockRenderData[] blockRenderDataArray = new BlockRenderData[Block.BLOCK_AMOUNT];
	private static HashMap<String,LoadedModel> blockModelMap = new HashMap<String,LoadedModel>();
	
	private static BlockRenderer defaultRenderer = new BlockRenderer();
	
	public static void loadAllRenderData(){
		loadAllBlockRenderData();
		loadAllBlockModels();
		loadAllBlockTextures();
	}

	private static void loadAllBlockTextures() {
		
	}

	private static void loadAllBlockModels() {
		for(int i = 0; i < blockRenderDataArray.length; i++){
			BlockRenderData blockRenderData = blockRenderDataArray[i];
			if(blockRenderData == null) continue;

			String modelName = blockRenderData.getModel();
			if(blockModelMap.containsKey(modelName)) continue;

			try{
				IndexedModel im = OIMModelLoader.loadModel(new File("res/models/blocks/" + modelName + ".oim"));
				LoadedModel lm = ModelLoader.INSTANCE.loadIndexedModel(im);
				blockModelMap.put(modelName, lm);
			}
			catch(Exception e){
				System.err.println("Failed to load block model " + modelName + ", block will not be rendered correctly!");
			}
		}
	}

	public static void registerBlockRenderer(int blockId, BlockRenderer renderer){
		if(!(blockRenderArray.length < blockId)){
			throw new IllegalArgumentException("Block id " + blockId + " is out of range!");
		}
		if(blockRenderArray[blockId] != null){
			throw new IllegalArgumentException("There already is a block renderer registered for block with id " + blockId + "!");
		}
		blockRenderArray[blockId] = renderer;
	}

	public static void registerBlockRenderer(Block block, BlockRenderer renderer){
		registerBlockRenderer(block.getBlockId(), renderer);
	}

	private static void loadAllBlockRenderData(){
		for(int i = 0; i < Block.BLOCK_AMOUNT; i++){
			Block block = Block.getBlockFromId(i);
			if(block != null){
				blockRenderDataArray[i] = BlockRenderData.from(block);
			}
		}
	}

	public static BlockRenderData getBlockRenderData(Block block){
		return getBlockRenderData(block.getBlockId());
	}

	public static BlockRenderData getBlockRenderData(int blockId){
		return blockRenderDataArray[blockId];
	}

	public static BlockRenderer getBlockRenderer(Block block){
		return getBlockRenderer(block.getBlockId());
	}

	public static BlockRenderer getBlockRenderer(int blockId){
		return blockRenderArray[blockId] == null ? defaultRenderer : blockRenderArray[blockId];
	}

	public static LoadedModel getModel(String model){
		return blockModelMap.get(model);
	}
}
