package game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.block.Block;
import game.renderer.block.BlockRenderData;
import game.renderer.block.BlockRenderRegistry;
import game.util.BlockPos;

public class ClientWorld {

	private HashMap<String,Set<Block>> modelsToRender;
	private HashMap<Block,List<BlockPos>> blocksToRender;
	
	private final World world;
	
	public ClientWorld(World world) {
		this.world = world;
		blocksToRender = new HashMap<Block,List<BlockPos>>();
		modelsToRender = new HashMap<String,Set<Block>>();
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
						addBlockToRender(block, pos);
					}
				}
			}
		}
	}
	
	private void addBlockToRender(Block block, BlockPos pos){
		BlockRenderData brd = BlockRenderRegistry.getBlockRenderData(block);
		if(brd == null) return;
		String model = brd.getModel();
		if(!modelsToRender.containsKey(model)){
			modelsToRender.put(model, new HashSet<Block>());
		}
		modelsToRender.get(model).add(block);
		
		if(!blocksToRender.containsKey(block)){
			blocksToRender.put(block, new ArrayList<BlockPos>());
		}
		blocksToRender.get(block).add(pos);
	}
	
	public World getWorldObj(){
		return world;
	}
	
	public Set<String> getModelsToRender(){
		return modelsToRender.keySet();
	}
	
	public List<BlockPos> getPositionsForBockToRender(Block block){
		return blocksToRender.get(block);
	}

	public Set<Block> getBlocksToRender(String model) {
		return modelsToRender.get(model);
	}
}
