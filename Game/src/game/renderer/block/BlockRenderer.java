package game.renderer.block;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import game.block.Block;
import game.renderer.model.LoadedModel;
import game.renderer.shader.BlockShader;
import game.util.BlockPos;
import game.world.World;

public class BlockRenderer {

	public BlockRenderer() {}
	
	public void preRenderBlock(BlockShader shader, World world, Block block) {
		shader.loadBlockColor(block.getBlockColor(world, new BlockPos(0,0,0)));
	}
	
	public void renderBlock(BlockShader shader, World world, BlockPos pos, Block block, LoadedModel model) {
		Matrix4f modelMatrix = MathHelper.createTransformationMatrix(new Vector3f(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F), 1.0F, 1.0F, 1.0F, Quaternion.fromVector(new Vector3f()));
		shader.loadModelMatrix(modelMatrix);
		model.renderAll();
	}
	
	public void postRenderBlock(BlockShader shader, World world, Block block) {
		
	}
}
