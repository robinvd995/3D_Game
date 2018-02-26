package game.common.block;

import game.common.util.BlockPos;
import game.common.util.EnumDirection;
import game.common.world.World;

public class BlockWater extends Block {

	public BlockWater(int id){
		super(id);
	}
	
	@Override
	public boolean isFullCube(){
		return false;
	}
	
	@Override
	public boolean isTransparantBlock(){
		return true;
	}
	
	@Override
	public boolean shouldRenderSide(World world, BlockPos pos, EnumDirection side){
		Block block = world.getBlock(pos.move(side));
		return !block.isFullCube() && block != Block.WATER;
	}
}
