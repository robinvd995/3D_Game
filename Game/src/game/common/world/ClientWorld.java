package game.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.client.renderer.block.BlockRenderData;
import game.client.renderer.block.BlockRenderRegistry;
import game.common.block.Block;
import game.common.util.BlockPos;

public class ClientWorld {

	private HashMap<String,Set<Block>> modelsToRender;
	private HashMap<Block,List<BlockPos>> blocksToRender;
	
	private HashMap<String,Set<Block>> transparentModelsToRender;
	private HashMap<Block,List<BlockPos>> transparentBlocksToRender;
	
	private final World world;
	
	public ClientWorld(World world) {
		this.world = world;
		blocksToRender = new HashMap<Block,List<BlockPos>>();
		modelsToRender = new HashMap<String,Set<Block>>();
		transparentModelsToRender = new HashMap<String,Set<Block>>();
		transparentBlocksToRender = new HashMap<Block,List<BlockPos>>();
	}
	
	public void checkBlocksToRender(){
		blocksToRender.clear();
		modelsToRender.clear();
		
		int maxX = world.getMaxX();
		int maxY = world.getMaxY();
		int maxZ = world.getMaxZ();
		
		for(int i = 0; i < maxX; i++){
			for(int j = 0; j < maxY; j++){
				for(int k = 0; k < maxZ; k++){
					BlockPos pos = new BlockPos(i, j, k);
					Block block = world.getBlock(pos);
					if(block.shouldBlockBeRendered(world, pos)){
						if(block.isTransparantBlock()){
							addBlockToRender(transparentModelsToRender, transparentBlocksToRender, block, pos);
						}else{
							addBlockToRender(modelsToRender, blocksToRender, block, pos);
						}
					}
				}
			}
		}
	}
	
	private void addBlockToRender(HashMap<String,Set<Block>> modelMap, HashMap<Block,List<BlockPos>> blockMap, Block block, BlockPos pos){
		BlockRenderData brd = BlockRenderRegistry.getBlockRenderData(block);
		if(brd == null) return;
		String model = brd.getModel();
		if(!modelMap.containsKey(model)){
			modelMap.put(model, new HashSet<Block>());
		}
		modelMap.get(model).add(block);
		
		if(!blockMap.containsKey(block)){
			blockMap.put(block, new ArrayList<BlockPos>());
		}
		blockMap.get(block).add(pos);
	}
	
	public World getWorldObj(){
		return world;
	}
	
	public Set<String> getModelsToRender(boolean transparent){
		return transparent ? transparentModelsToRender.keySet() : modelsToRender.keySet();
	}
	
	public List<BlockPos> getPositionsForBockToRender(Block block, boolean transparent){
		return transparent ? transparentBlocksToRender.get(block) : blocksToRender.get(block);
	}

	public Set<Block> getBlocksToRender(String model, boolean transparent) {
		return transparent ? transparentModelsToRender.get(model) : modelsToRender.get(model);
	}
}
