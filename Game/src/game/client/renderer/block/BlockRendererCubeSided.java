package game.client.renderer.block;

import game.client.renderer.tessellation.Tessellator;
import game.client.renderer.texture.MappedTexture;
import game.client.renderer.texture.TextureManager;
import game.common.block.Block;
import game.common.physics.AxisAlignedBB;
import game.common.util.BlockPos;
import game.common.util.EnumDirection;
import game.common.world.World;

public class BlockRendererCubeSided implements IBlockRenderer{

	@Override
	public void tessellateBlock(Tessellator tessellator, World world, BlockRenderData blockRenderData, Block block, BlockPos pos) {
		
		AxisAlignedBB bounds = block.getBounds(world, pos);
	
		MappedTexture topTexture = TextureManager.getMappedBlockTexture(blockRenderData.getTexture("top"));
		MappedTexture sideTexture = TextureManager.getMappedBlockTexture(blockRenderData.getTexture("side"));
		MappedTexture bottomTexture = TextureManager.getMappedBlockTexture(blockRenderData.getTexture("bottom"));
		
		float minx = bounds.getMinX();
		float miny = bounds.getMinY();
		float minz = bounds.getMinZ();
		float maxx = bounds.getMaxX();
		float maxy = bounds.getMaxY();
		float maxz = bounds.getMaxZ();
		
		float minu = sideTexture.getU();
		float minv = sideTexture.getV();
		float maxu = minu + sideTexture.getWidth();
		float maxv = minv + sideTexture.getHeight();
		
		if(block.shouldRenderSide(world, pos, EnumDirection.FRONT)){
			tessellator.setNormal(EnumDirection.FRONT);
			tessellator.addVertex(maxx, miny, maxz, maxu, maxv);
			tessellator.addVertex(maxx, maxy, maxz, maxu, minv);
			tessellator.addVertex(minx, maxy, maxz, minu, minv);
			tessellator.addVertex(minx, miny, maxz, minu, maxv);
			tessellator.tessellate();
		}
		
		if(block.shouldRenderSide(world, pos, EnumDirection.BACK)){
			tessellator.setNormal(EnumDirection.BACK);
			tessellator.addVertex(minx, miny, minz, maxu, maxv);
			tessellator.addVertex(minx, maxy, minz, maxu, minv);
			tessellator.addVertex(maxx, maxy, minz, minu, minv);
			tessellator.addVertex(maxx, miny, minz, minu, maxv);
			tessellator.tessellate();
		}
		
		if(block.shouldRenderSide(world, pos, EnumDirection.UP)){
			tessellator.setNormal(EnumDirection.UP);
			tessellator.addVertex(minx, maxy, minz, topTexture.getMaxU(), topTexture.getMaxV());
			tessellator.addVertex(minx, maxy, maxz, topTexture.getMaxU(), topTexture.getV());
			tessellator.addVertex(maxx, maxy, maxz, topTexture.getU(), topTexture.getV());
			tessellator.addVertex(maxx, maxy, minz, topTexture.getU(), topTexture.getMaxV());
			tessellator.tessellate();
		}
		
		if(block.shouldRenderSide(world, pos, EnumDirection.DOWN)){
			tessellator.setNormal(EnumDirection.DOWN);
			tessellator.addVertex(maxx, miny, minz, bottomTexture.getU(), bottomTexture.getV());
			tessellator.addVertex(maxx, miny, maxz, bottomTexture.getU(), bottomTexture.getMaxV());
			tessellator.addVertex(minx, miny, maxz, bottomTexture.getMaxU(), bottomTexture.getMaxV());
			tessellator.addVertex(minx, miny, minz, bottomTexture.getMaxU(), bottomTexture.getV());
			tessellator.tessellate();
		}
		
		if(block.shouldRenderSide(world, pos, EnumDirection.LEFT)){
			tessellator.setNormal(EnumDirection.LEFT);
			tessellator.addVertex(minx, miny, minz, minu, maxv);
			tessellator.addVertex(minx, miny, maxz, maxu, maxv);
			tessellator.addVertex(minx, maxy, maxz, maxu, minv);
			tessellator.addVertex(minx, maxy, minz, minu, minv);
			tessellator.tessellate();
		}
		
		if(block.shouldRenderSide(world, pos, EnumDirection.RIGHT)){
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
		return "cube_sided";
	}
}