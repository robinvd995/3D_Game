package game.block;

import caesar.util.Vector3f;
import game.physics.AxisAlignedBB;
import game.util.BlockPos;
import game.util.EnumDirection;
import game.world.World;

public class Block {

	public static final Block AIR = new BlockAir(0);
	public static final Block GRASS = new Block(1).setBlockColor(0.0f, 1.0f, 0.0f);
	public static final Block WATER = new Block(2).setBlockColor(0.0f, 0.0f, 1.0f);
	public static final Block STONE = new Block(3).setBlockColor(0.5f, 0.5f, 0.5f);
	
	private Vector3f blockColor = new Vector3f(1.0f, 1.0f, 1.0f);
	
	private boolean needsRendering = true;
	
	private final int blockId;
	
	public Block(int id){
		blockId = id;
	}
	
	public Block setBlockColor(float r, float g, float b){
		this.blockColor = new Vector3f(r, g, b);
		return this;
	}
	
	public Vector3f getBlockColor(){
		return blockColor;
	}
	
	public Block disableRendering(){
		this.needsRendering = false;
		return this;
	}
	
	public boolean shouldBlockBeRendered(World world, BlockPos pos){
		if(!needsRendering){
			return false;
		}
		for(EnumDirection dir : EnumDirection.getValidDirections()){
			Block block = world.getBlock(pos.move(dir));
			if(block.getBlockId() == 0){
				return true;
			}
		}
		return false;
	}
	
	public int getBlockId(){
		return blockId;
	}

	public AxisAlignedBB getBoundingBox(){
		return new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blockId;
		return result;
	}
}
