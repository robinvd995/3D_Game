package game.client.renderer.block;

import game.client.renderer.tessellation.Tessellator;
import game.client.renderer.texture.MappedTexture;
import game.client.renderer.texture.TextureManager;
import game.common.block.Block;
import game.common.physics.AxisAlignedBB;
import game.common.util.BlockPos;
import game.common.util.EnumDirection;
import game.common.world.World;

public class BlockRendererCube implements IBlockRenderer{

	@Override
	public void tessellateBlock(Tessellator tessellator, World world, BlockRenderData blockRenderData, Block block, BlockPos pos) {

		AxisAlignedBB bounds = block.getBounds(world, pos);
		MappedTexture texture = TextureManager.getMappedBlockTexture(blockRenderData.getTexture("texture"));

		float minx = bounds.getMinX();
		float miny = bounds.getMinY();
		float minz = bounds.getMinZ();
		float maxx = bounds.getMaxX();
		float maxy = bounds.getMaxY();
		float maxz = bounds.getMaxZ();

		float minu = texture.getU();
		float minv = texture.getV();
		float maxu = minu + texture.getWidth();
		float maxv = minv + texture.getHeight();

		boolean renderFront = block.shouldRenderSide(world, pos, EnumDirection.FRONT);
		boolean renderBack = block.shouldRenderSide(world, pos, EnumDirection.BACK);
		boolean renderUp = block.shouldRenderSide(world, pos, EnumDirection.UP);
		boolean renderDown = block.shouldRenderSide(world, pos, EnumDirection.DOWN);
		boolean renderLeft = block.shouldRenderSide(world, pos, EnumDirection.LEFT);
		boolean renderRight = block.shouldRenderSide(world, pos, EnumDirection.RIGHT);
		
		if(renderFront){
			tessellator.setNormal(EnumDirection.FRONT);
			tessellator.addVertex(maxx, miny, maxz, maxu, maxv);
			tessellator.addVertex(maxx, maxy, maxz, maxu, minv);
			tessellator.addVertex(minx, maxy, maxz, minu, minv);
			tessellator.addVertex(minx, miny, maxz, minu, maxv);
			tessellator.tessellate();
		}

		if(renderBack){
			tessellator.setNormal(EnumDirection.BACK);
			tessellator.addVertex(minx, miny, minz, maxu, maxv);
			tessellator.addVertex(minx, maxy, minz, maxu, minv);
			tessellator.addVertex(maxx, maxy, minz, minu, minv);
			tessellator.addVertex(maxx, miny, minz, minu, maxv);
			tessellator.tessellate();
		}

		if(renderUp){
			tessellator.setNormal(EnumDirection.UP);
			tessellator.addVertex(minx, maxy, minz, maxu, maxv);
			tessellator.addVertex(minx, maxy, maxz, maxu, minv);
			tessellator.addVertex(maxx, maxy, maxz, minu, minv);
			tessellator.addVertex(maxx, maxy, minz, minu, maxv);
			tessellator.tessellate();
		}

		if(renderDown){
			tessellator.setNormal(EnumDirection.DOWN);
			tessellator.addVertex(maxx, miny, minz, minu, minv);
			tessellator.addVertex(maxx, miny, maxz, minu, maxv);
			tessellator.addVertex(minx, miny, maxz, maxu, maxv);
			tessellator.addVertex(minx, miny, minz, maxu, minv);
			tessellator.tessellate();
		}

		if(renderLeft){
			tessellator.setNormal(EnumDirection.LEFT);
			tessellator.addVertex(minx, miny, minz, minu, maxv);
			tessellator.addVertex(minx, miny, maxz, maxu, maxv);
			tessellator.addVertex(minx, maxy, maxz, maxu, minv);
			tessellator.addVertex(minx, maxy, minz, minu, minv);
			tessellator.tessellate();
		}

		if(renderRight){
			tessellator.setNormal(EnumDirection.RIGHT);
			tessellator.addVertex(maxx, maxy, minz, maxu, minv);
			tessellator.addVertex(maxx, maxy, maxz, minu, minv);
			tessellator.addVertex(maxx, miny, maxz, minu, maxv);
			tessellator.addVertex(maxx, miny, minz, maxu, maxv);
			tessellator.tessellate();
		}
	}

	@Override
	public String renderId() {
		return "cube";
	}
}
