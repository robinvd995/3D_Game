package game.client.renderer.block;

import game.client.renderer.tessellation.Tessellator;
import game.common.block.Block;
import game.common.util.BlockPos;
import game.common.world.World;

public interface IBlockRenderer {

	void tessellateBlock(Tessellator tessellator, World world, BlockRenderData renderData, Block block, BlockPos pos);
	String renderId();
}
