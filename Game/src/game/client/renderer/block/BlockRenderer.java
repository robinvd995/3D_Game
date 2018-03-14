/*package game.client.renderer.block;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import game.client.renderer.model.LoadedModel;
import game.client.renderer.shader.BlockShader;
import game.client.renderer.texture.MappedTexture;
import game.client.renderer.texture.TextureManager;
import game.common.block.Block;
import game.common.util.BlockPos;
import game.common.util.EnumDirection;
import game.common.world.World;

public class BlockRenderer {

	public BlockRenderer() {}
	
	public void preRenderBlock(BlockShader shader, World world, Block block) {
		shader.loadBlockColor(block.getBlockColor(world, new BlockPos(0,0,0)));
	}
	
	public void renderBlock(BlockShader shader, World world, BlockPos pos, Block block, LoadedModel model, BlockRenderData brd) {
		Matrix4f modelMatrix = MathHelper.createTransformationMatrix(new Vector3f(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F), 1.0F, 1.0F, 1.0F, Quaternion.fromVector(new Vector3f()));
		shader.loadModelMatrix(modelMatrix);
		for(EnumDirection dir : EnumDirection.getValidDirections()){
			if(block.shouldRenderSide(world, pos, dir)){
				String texture = brd.getRenderPartFromSide(dir).getTexture();
				MappedTexture mappedTexture = TextureManager.getMappedBlockTexture(texture);
				shader.loadTextureCoords(mappedTexture.toVector());
				model.renderSection(dir.getUnlocalizedName());
			}
		}
	}
	
	public void postRenderBlock(BlockShader shader, World world, Block block) {
		
	}
}
*/