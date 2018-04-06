package game.common.block;

import game.common.physics.AxisAlignedBB;
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
	public AxisAlignedBB getBounds(World world, BlockPos pos){
		Block block = world.getBlock(pos.move(EnumDirection.UP));
		if(block == Block.WATER){
			return new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		}
		return new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 0.875f, 1.0f);
	}
	
	@Override
	public boolean shouldRenderSide(World world, BlockPos pos, EnumDirection side){
		if(side != EnumDirection.UP) return false;
		Block block = world.getBlock(pos.move(side));
		return !block.isFullCube() && block != Block.WATER;
	}
}
