package game.common.block;

import game.common.physics.AxisAlignedBB;
import game.common.util.BlockPos;
import game.common.world.World;

public class BlockAir extends Block{

	public BlockAir(int id) {
		super(id);
		this.disableRendering();
	}

	public boolean isFullCube(){
		return false;
	}
	
	@Override
	public AxisAlignedBB getBounds(World world, BlockPos pos){
		return null;
	}
}
