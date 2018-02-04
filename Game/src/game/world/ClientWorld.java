package game.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import game.block.Block;
import game.util.BlockPos;

public class ClientWorld {

	private HashMap<Block,List<BlockPos>> blocksToRender;
	
	private final World world;
	
	public ClientWorld(World world) {
		this.world = world;
		blocksToRender = new HashMap<Block,List<BlockPos>>();
	}
	
	public void checkBlocksToRender(){
		blocksToRender.clear();
		
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
		if(!blocksToRender.containsKey(block)){
			blocksToRender.put(block, new ArrayList<BlockPos>());
		}
		blocksToRender.get(block).add(pos);
	}
	
	public World getWorldObj(){
		return world;
	}
	
	public Set<Block> getBlocksToRender(){
		return blocksToRender.keySet();
	}
	
	public List<BlockPos> getPositionsForBockToRender(Block block){
		return blocksToRender.get(block);
	}
}
