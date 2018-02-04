package game.renderer;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.opengl.GL;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import game.block.Block;
import game.renderer.model.ModelLoader;
import game.world.ClientWorld;

public class RenderManager {

	private BlockRenderManager blockRenderer;
	private EntityRenderManager entityRenderer;

	public RenderManager(){
		blockRenderer = new BlockRenderManager();
		entityRenderer = new EntityRenderManager();
		DebugRenderer.INSTANCE.init();
	}

	public void initOpenGL(){
		GL.createCapabilities();
	}
	
	public void initRenderer(){
		entityRenderer.initRenderer();
	}

	private void prepare(){
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void renderWorld(Camera camera, ClientWorld world, double delta, Vector3f lightDir){
		prepare();
		Matrix4f viewMatrix = camera.createViewMatrix();
		
		blockRenderer.prepare(lightDir, viewMatrix);
		for(Block block : world.getBlocksToRender()){
			blockRenderer.renderBlocks(world.getWorldObj(), block, world.getPositionsForBockToRender(block));
		}
		blockRenderer.end();
		
		entityRenderer.prepare(lightDir, viewMatrix);
		entityRenderer.renderEntity(world.getWorldObj(), world.getWorldObj().getPlayer());
		entityRenderer.end();
		
		DebugRenderer.INSTANCE.prepare(viewMatrix);
		DebugRenderer.INSTANCE.renderAll();
		DebugRenderer.INSTANCE.end();
	}

	public void cleanUp() {
		ModelLoader.INSTANCE.cleanUp();
	}
}
