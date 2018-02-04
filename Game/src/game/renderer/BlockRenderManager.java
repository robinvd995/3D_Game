package game.renderer;

import java.io.File;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import converter.api.OIMModelLoader;
import converter.api.model.IndexedModel;
import game.block.Block;
import game.display.DisplayManager;
import game.renderer.model.LoadedModel;
import game.renderer.model.ModelLoader;
import game.renderer.shader.BlockShader;
import game.util.BlockPos;
import game.world.World;

public class BlockRenderManager {

	private BlockShader shader;
	private LoadedModel model;
	
	public BlockRenderManager(){
		shader = new BlockShader();
		IndexedModel indexedModel = OIMModelLoader.loadModel(new File("res/models/block.oim"));
		model = ModelLoader.INSTANCE.loadIndexedModel(indexedModel);
	}
	
	public void prepare(Vector3f lightDir, Matrix4f viewMatrix) {
		Matrix4f projectionMatrix = DisplayManager.INSTANCE.getProjectionMatrix();
		
		shader.start();
		shader.loadViewMatrix(viewMatrix);
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadLightDirection(lightDir);
		
		int vao = model.getVao();
		
		GL30.glBindVertexArray(vao);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

	}
	
	public void renderBlocks(World world, Block block, List<BlockPos> positions){
		shader.loadBlockColor(block.getBlockColor());
		for(BlockPos pos : positions){
			renderBlock(block, pos.getX(), pos.getY(), pos.getZ());
		}
	}
	
	private void renderBlock(Block block, int x, int y, int z){
		Matrix4f modelMatrix = MathHelper.createTransformationMatrix(new Vector3f(x + 0.5F, y + 0.5F, z + 0.5F), 1.0F, 1.0F, 1.0F, Quaternion.fromVector(new Vector3f()));
		shader.loadModelMatrix(modelMatrix);
		
		model.renderAll();
	}
	
	public void end(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);

		GL30.glBindVertexArray(0);
		
		shader.stop();
	}
}
