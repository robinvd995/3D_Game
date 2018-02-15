package game.renderer.block;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import game.block.Block;
import game.display.DisplayManager;
import game.renderer.model.LoadedModel;
import game.renderer.shader.BlockShader;
import game.renderer.texture.TextureManager;
import game.util.BlockPos;
import game.world.ClientWorld;

public class BlockRenderManager {
	
	private BlockShader shader;
	
	public BlockRenderManager(){
		shader = new BlockShader();
	}

	public void initRenderer(){
		
	}
	
	public void prepare(Vector3f lightDir, Matrix4f viewMatrix) {
		Matrix4f projectionMatrix = DisplayManager.INSTANCE.getProjectionMatrix();

		shader.start();
		shader.loadViewMatrix(viewMatrix);
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadLightDirection(lightDir);
		TextureManager.bindBlockTextureMap();
	}

	public void renderBlocks(ClientWorld world, boolean renderTransparentBlocks){
		
		for(String modelName : world.getModelsToRender(renderTransparentBlocks)){
			LoadedModel model = BlockRenderRegistry.getModel(modelName);
			
			int vao = model.getVao();
			GL30.glBindVertexArray(vao);

			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			
			for(Block block : world.getBlocksToRender(modelName, renderTransparentBlocks)){
				BlockRenderer renderer = BlockRenderRegistry.getBlockRenderer(block);
				BlockRenderData brd = BlockRenderRegistry.getBlockRenderData(block);
				renderer.preRenderBlock(shader, world.getWorldObj(), block);
				for(BlockPos pos : world.getPositionsForBockToRender(block, renderTransparentBlocks)){
					renderer.renderBlock(shader, world.getWorldObj(), pos, block, model, brd);
				}
				renderer.postRenderBlock(shader, world.getWorldObj(), block);
			}
			
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);

			GL30.glBindVertexArray(0);
		}
	}

	public void end(){
		shader.stop();
	}
}
