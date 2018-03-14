package game.client.renderer;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

import caesar.util.Matrix4f;
//import game.client.renderer.block.BlockRenderManager;
import game.client.renderer.entity.EntityRenderManager;
import game.client.renderer.gui.GuiRenderManager;
import game.client.renderer.model.ModelLoader;
import game.client.renderer.texture.TextureManager;
import game.client.renderer.world.ClusterRenderManager;
import game.client.renderer.world.SkyRenderManager;
import game.client.renderer.world.WorldClient;

public class RenderManager {

	//private BlockRenderManager blockRenderer;
	private ClusterRenderManager clusterRenderer;
	private EntityRenderManager entityRenderer;
	private GuiRenderManager guiRenderer;
	private SkyRenderManager skyboxRenderer;
	
	public RenderManager(){
		//blockRenderer = new BlockRenderManager();
		clusterRenderer = new ClusterRenderManager();
		entityRenderer = new EntityRenderManager();
		guiRenderer = new GuiRenderManager();
		skyboxRenderer = new SkyRenderManager();
		DebugRenderer.INSTANCE.init();
	}

	public void initOpenGL(){
		GL.createCapabilities();
	}
	
	public void initRenderer(){
		//BlockRenderRegistry.loadAllRenderData();
		RenderRegistry.registerBlockRenderers();
		RenderRegistry.loadBlockRenderData();
		TextureManager.loadBlockTextures();
		
		clusterRenderer.initRenderer();
		entityRenderer.initRenderer();
		//blockRenderer.initRenderer();
		guiRenderer.initRenderer();
		skyboxRenderer.initRenderer();
	}
	
	public void preRender(){
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void renderWorld(Camera camera, WorldClient world, double delta){
		Matrix4f viewMatrix = camera.createViewMatrix();
		
		skyboxRenderer.renderSkybox(viewMatrix);
		skyboxRenderer.renderSun(world, viewMatrix);
		
		clusterRenderer.prepare(world, viewMatrix);
		clusterRenderer.renderBlocks(world);
		clusterRenderer.end();
		
		/*blockRenderer.prepare(lightDir, viewMatrix);
		blockRenderer.renderBlocks(world, false);
		
		blockRenderer.renderBlocks(world, true);
		blockRenderer.end();*/
		
		entityRenderer.prepare(world, viewMatrix);
		entityRenderer.renderEntity(world, world.getPlayer());
		entityRenderer.end();
		
		DebugRenderer.INSTANCE.prepare(viewMatrix);
		DebugRenderer.INSTANCE.renderAll();
		DebugRenderer.INSTANCE.end();
	}
	
	public void renderGuis(){
		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		guiRenderer.renderGuis();
	}
	
	public void cleanUp() {
		/*try {
			//TextureLoader.saveBufferColorTexture(buffer, TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS, "buffer", "png");
			//TextureLoader.saveBufferColorTexture(buffer, DisplayManager.INSTANCE.getDisplayWidth(), DisplayManager.INSTANCE.getDisplayHeight(), "buffer", "png");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		System.out.println("cleanup renderers");
		ModelLoader.INSTANCE.cleanUp();
	}
	
	public GuiRenderManager getGuiRenderer(){
		return guiRenderer;
	}
}
