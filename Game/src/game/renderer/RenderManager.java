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
import game.renderer.block.BlockRenderManager;
import game.renderer.block.BlockRenderRegistry;
import game.renderer.entity.EntityRenderManager;
import game.renderer.model.ModelLoader;
import game.renderer.texture.TextureManager;
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
		BlockRenderRegistry.loadAllRenderData();
		TextureManager.loadAllTextures();
		
		entityRenderer.initRenderer();
		blockRenderer.initRenderer();
	}

	private void stitchTextures(){
		
	}
	
	private void prepare(){
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void renderWorld(Camera camera, ClientWorld world, double delta, Vector3f lightDir){
		//buffer.bindTextureBuffer();
		prepare();
		Matrix4f viewMatrix = camera.createViewMatrix();
		
		blockRenderer.prepare(lightDir, viewMatrix);
		blockRenderer.renderBlocks(world);
		blockRenderer.end();
		
		entityRenderer.prepare(lightDir, viewMatrix);
		entityRenderer.renderEntity(world.getWorldObj(), world.getWorldObj().getPlayer());
		entityRenderer.end();
		
		DebugRenderer.INSTANCE.prepare(viewMatrix);
		DebugRenderer.INSTANCE.renderAll();
		DebugRenderer.INSTANCE.end();
	}

	public void cleanUp() {
		/*try {
			//TextureLoader.saveBufferColorTexture(buffer, TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS, "buffer", "png");
			//TextureLoader.saveBufferColorTexture(buffer, DisplayManager.INSTANCE.getDisplayWidth(), DisplayManager.INSTANCE.getDisplayHeight(), "buffer", "png");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		System.out.println("cleanup");
		ModelLoader.INSTANCE.cleanUp();
	}
}
