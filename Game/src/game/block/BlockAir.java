package game.block;

import game.physics.AxisAlignedBB;

public class BlockAir extends Block{

	public BlockAir(int id) {
		super(id);
		this.disableRendering();
	}

	@Override
	public AxisAlignedBB getBoundingBox(){
		return null;
	}
}
